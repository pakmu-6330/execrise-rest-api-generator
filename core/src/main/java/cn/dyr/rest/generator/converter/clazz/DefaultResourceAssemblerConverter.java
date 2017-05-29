package cn.dyr.rest.generator.converter.clazz;

import cn.dyr.rest.generator.converter.ConvertDataContext;
import cn.dyr.rest.generator.converter.ConverterConfig;
import cn.dyr.rest.generator.converter.ConverterInject;
import cn.dyr.rest.generator.converter.ConverterInjectType;
import cn.dyr.rest.generator.converter.DataInject;
import cn.dyr.rest.generator.converter.DataInjectType;
import cn.dyr.rest.generator.converter.name.INameConverter;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.entity.EntityRelationship;
import cn.dyr.rest.generator.entity.RelationshipType;
import cn.dyr.rest.generator.framework.jdk.JDKAnnotationFactory;
import cn.dyr.rest.generator.framework.spring.SpringFrameworkAnnotationFactory;
import cn.dyr.rest.generator.framework.spring.SpringFrameworkTypeFactory;
import cn.dyr.rest.generator.framework.spring.hateoas.SpringHATEOASConstant;
import cn.dyr.rest.generator.framework.spring.hateoas.SpringHATEOASTypeFactory;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.ConstructorMethodInfo;
import cn.dyr.rest.generator.java.meta.FieldInfo;
import cn.dyr.rest.generator.java.meta.MethodInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.InstructionFactory;
import cn.dyr.rest.generator.java.meta.factory.ParameterFactory;
import cn.dyr.rest.generator.java.meta.factory.ParameterValueFactory;
import cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.util.ClassInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_CONTROLLER_CLASS;
import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_ENTITY_CLASS;
import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_RESOURCE_CLASS;
import static cn.dyr.rest.generator.entity.RelationshipType.MANY_TO_MANY;
import static cn.dyr.rest.generator.entity.RelationshipType.MANY_TO_ONE;
import static cn.dyr.rest.generator.entity.RelationshipType.ONE_TO_MANY;

/**
 * 这个资源装配类为默认的资源装配类实现
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DefaultResourceAssemblerConverter implements IResourceAssemblerConverter {

    private static Logger logger;

    static {
        logger = LoggerFactory.getLogger(DefaultResourceAssemblerConverter.class);
    }

    @DataInject(DataInjectType.DATA_CONTEXT)
    private ConvertDataContext context;

    @DataInject(DataInjectType.CONFIG)
    private ConverterConfig config;

    @ConverterInject(ConverterInjectType.NAME)
    private INameConverter nameConverter;

    @DataInject(DataInjectType.HATEOAS_RESOURCE_ASSEMBLER_PACKAGE_NAME)
    private String assemblerPackageName;

    @Override
    public ClassInfo toResourceAssembler(EntityInfo entityInfo) {
        IValueExpression entityValue = ValueExpressionFactory.variable("entity");

        String entityName = entityInfo.getName();

        ClassInfo entityClass = this.context.getClassByEntityAndType(entityName, TYPE_ENTITY_CLASS);
        ClassInfo resourceClass = this.context.getClassByEntityAndType(entityName, TYPE_RESOURCE_CLASS);
        ClassInfo controllerClass = this.context.getClassByEntityAndType(entityName, TYPE_CONTROLLER_CLASS);

        TypeInfo resourceClassType = resourceClass.getType();

        // 1. 生成类的基本信息
        String className = this.nameConverter.hateoasResourceAssemblerNameFromEntityName(entityName);
        ClassInfo assemblerClassInfo = new ClassInfo()
                .setClassName(className)
                .setPackageName(assemblerPackageName)
                .addAnnotation(SpringFrameworkAnnotationFactory.component())
                .extendClass(SpringHATEOASTypeFactory.resourceAssemblerSupport(entityClass.getType(), resourceClassType))
                .implementInterface(SpringFrameworkTypeFactory.converter(entityClass.getType(), resourceClassType));

        // 2. 创建构造方法
        {
            ConstructorMethodInfo assemblerConstructorMethod = new ConstructorMethodInfo(assemblerClassInfo.getType());
            IInstruction constructorInstruction = InstructionFactory.superConstructor(
                    ParameterValueFactory.fromObjects(new Object[]{
                            controllerClass.getType(), resourceClassType
                    })
            );
            assemblerConstructorMethod.setRootInstruction(constructorInstruction);
            assemblerClassInfo.addMethod(assemblerConstructorMethod);
        }

        // 3. instantiateResource 方法
        {
            MethodInfo instantiateResourceMethod = new MethodInfo()
                    .addAnnotationInfo(JDKAnnotationFactory.override())
                    .setName("instantiateResource")
                    .setReturnValueType(resourceClassType)
                    .addParameter(ParameterFactory.create(entityClass.getType(), "entity"))
                    .setProtected();
            assemblerClassInfo.addMethod(instantiateResourceMethod);

            IValueExpression resourceValue = ValueExpressionFactory.variable("resource");

            // #1 创建字段对象
            IInstruction resourceDeclarationInstruction = InstructionFactory.variableDeclaration(
                    resourceClassType, "resource", ValueExpressionFactory.invokeConstructor(resourceClassType));

            // #2 调用 BeanUtils 复制字段
            IInstruction copyPropertiesInstruction = InstructionFactory.staticInvoke(
                    SpringFrameworkTypeFactory.beanUtils(), false, "copyProperties",
                    ParameterValueFactory.fromObjects(new Object[]{entityValue, resourceValue}));

            // #3 添加相关的关联关系
            // 获得本实体的 id
            List<IInstruction> instructions = new ArrayList<>();

            FieldInfo entityIdField = ClassInfoUtils.findSingleId(entityClass);
            MethodInfo entityIdGetterMethod = entityClass.getterMethod(entityIdField.getName());
            IValueExpression entityIdValue = entityValue.invokeMethod(entityIdGetterMethod.getName());

            List<ConvertDataContext.RelationshipHandler> relationships = this.context.findByHandler(entityInfo.getName());
            for (ConvertDataContext.RelationshipHandler handler : relationships) {
                MethodInfo relatedEntityGetter = entityClass.getterMethod(handler.getHandlerFieldName());

                IValueExpression methodOnExpression = InstructionFactory
                        .invokeStaticMethod(
                                SpringHATEOASTypeFactory.controllerLinkBuilderType(), true,
                                SpringHATEOASConstant.CONTROLLER_LINK_BUILDER_METHOD_ON, new Object[]{
                                        controllerClass.getType()
                                }).toValueExpression();

                // 寻找这个类背后的关联关系信息
                FieldInfo fieldInfo = entityClass.findByPropertyMethodName(relatedEntityGetter.getName());
                if (fieldInfo == null) {
                    logger.warn("failed to handle relationship on method: {}", relatedEntityGetter.getName());
                    continue;
                }

                EntityRelationship relationship = context.getRelationship(entityClass, fieldInfo);
                if (relationship == null) {
                    logger.warn("failed to handle relationship on method: {}", relatedEntityGetter.getName());
                    continue;
                }

                boolean thisAsEntityA = (entityInfo.getName().equals(relationship.getEndA().getName()));
                boolean thisAsEntityB = (entityInfo.getName().equals(relationship.getEndB().getName()));
                boolean oppositeAsManyEnd =
                        (thisAsEntityA && relationship.getType() == ONE_TO_MANY || relationship.getType() == MANY_TO_MANY) ||
                                (thisAsEntityB && relationship.getType() == MANY_TO_ONE || relationship.getType() == MANY_TO_MANY);

                IValueExpression linkToExpression = null;
                if (oppositeAsManyEnd && config.isPagingEnabled()) {
                    linkToExpression = methodOnExpression
                            .invokeMethod(relatedEntityGetter.getName(), new Object[]{
                                    entityIdValue, ValueExpressionFactory.nullExpression()
                            });
                } else {
                    linkToExpression = methodOnExpression
                            .invokeMethod(relatedEntityGetter.getName(), new Object[]{
                                    entityIdValue
                            });
                }

                IValueExpression addLinkExpression = InstructionFactory
                        .invokeStaticMethod(
                                SpringHATEOASTypeFactory.controllerLinkBuilderType(), true,
                                SpringHATEOASConstant.CONTROLLER_LINK_BUILDER_LINK_TO, new Object[]{
                                        linkToExpression
                                }).toValueExpression()
                        .invokeMethod("withRel", new Object[]{handler.getHandlerFieldName()});

                IInstruction addInstruction = InstructionFactory.invoke(
                        ValueExpressionFactory.variable("resource"),
                        "add", new Object[]{
                                addLinkExpression
                        }
                );

                instructions.add(addInstruction);
            }

            if (instructions.size() != 0) {
                instructions.add(InstructionFactory.emptyInstruction());
            }

            IInstruction relatedResourceInstruction = InstructionFactory.sequence(instructions);

            // #X 返回
            IInstruction returnInstruction = InstructionFactory.returnInstruction(resourceValue);
            IInstruction methodInstruction = InstructionFactory.sequence(
                    resourceDeclarationInstruction,
                    copyPropertiesInstruction,
                    InstructionFactory.emptyInstruction(),
                    relatedResourceInstruction,
                    returnInstruction
            );

            instantiateResourceMethod.setRootInstruction(methodInstruction);
        }

        // 4. toResource 方法
        {
            MethodInfo toResourceMethod = new MethodInfo()
                    .addAnnotationInfo(JDKAnnotationFactory.override())
                    .setName("toResource")
                    .setReturnValueType(resourceClassType)
                    .addParameter(ParameterFactory.create(entityClass.getType(), "entity"));
            assemblerClassInfo.addMethod(toResourceMethod);

            FieldInfo entityIdField = ClassInfoUtils.findSingleId(entityClass);
            MethodInfo getIdMethod = entityClass.getterMethod(entityIdField.getName());
            IValueExpression firstParameter = entityValue.invokeMethod(getIdMethod.getName());

            IValueExpression returnValue = ValueExpressionFactory.thisReference().invokeMethod("createResourceWithId", new Object[]{firstParameter, entityValue});
            IInstruction toResourceInstruction = InstructionFactory.returnInstruction(returnValue);
            toResourceMethod.setRootInstruction(toResourceInstruction);
        }

        // 5. convert 方法
        {
            MethodInfo convertMethod = new MethodInfo()
                    .addAnnotationInfo(JDKAnnotationFactory.override())
                    .setName("convert")
                    .setReturnValueType(resourceClassType)
                    .addParameter(ParameterFactory.create(entityClass.getType(), "entity"));
            IInstruction convertMethodInstruction = InstructionFactory.returnInstruction(ValueExpressionFactory.thisReference().invokeMethod("instantiateResource", new Object[]{entityValue}));
            convertMethod.setRootInstruction(convertMethodInstruction);

            assemblerClassInfo.addMethod(convertMethod);
        }

        return assemblerClassInfo;
    }
}
