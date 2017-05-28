package cn.dyr.rest.generator.converter.clazz;

import cn.dyr.rest.generator.converter.ConvertDataContext;
import cn.dyr.rest.generator.converter.ConverterConfig;
import cn.dyr.rest.generator.converter.ConverterInject;
import cn.dyr.rest.generator.converter.ConverterInjectType;
import cn.dyr.rest.generator.converter.DataInject;
import cn.dyr.rest.generator.converter.DataInjectType;
import cn.dyr.rest.generator.converter.name.INameConverter;
import cn.dyr.rest.generator.entity.AttributeInfo;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.entity.EntityRelationship;
import cn.dyr.rest.generator.framework.jdk.CollectionsTypeFactory;
import cn.dyr.rest.generator.framework.jpa.JPAConstant;
import cn.dyr.rest.generator.framework.jpa.JPQLGenerator;
import cn.dyr.rest.generator.framework.spring.data.SpringDataAnnotationFactory;
import cn.dyr.rest.generator.framework.spring.data.SpringDataConstant;
import cn.dyr.rest.generator.framework.spring.data.SpringDataTypeFactory;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.FieldInfo;
import cn.dyr.rest.generator.java.meta.MethodInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.ParameterFactory;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.java.meta.parameters.Parameter;
import cn.dyr.rest.generator.util.ClassInfoUtils;
import cn.dyr.rest.generator.util.StringUtils;

import java.util.Iterator;
import java.util.List;

import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_ENTITY_CLASS;

/**
 * DAO 类转换器的默认实现
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DefaultDAOConverter implements IDAOConverter {

    @ConverterInject(ConverterInjectType.NAME)
    private INameConverter nameConverter;

    @DataInject(DataInjectType.CONFIG)
    private ConverterConfig converterConfig;

    @DataInject(DataInjectType.DATA_CONTEXT)
    private ConvertDataContext convertDataContext;

    @DataInject(DataInjectType.DAO_PACKAGE_NAME)
    private String daoPackageName;

    @Override
    public ClassInfo fromEntity(EntityInfo entityInfo) {
        ClassInfo entityClass = this.convertDataContext.getClassByEntityAndType(entityInfo.getName(), TYPE_ENTITY_CLASS);

        // 1. 提取实体类的 id
        List<FieldInfo> idFieldList =
                entityClass.findFieldsByAnnotationType(TypeInfoFactory.fromClass(JPAConstant.ID_ANNOTATION));
        if (idFieldList.size() == 0) {
            throw new IllegalStateException(String.format("id field not found in class: %s for entity %s",
                    entityClass.getFullName(),
                    entityInfo.getName()));
        } else if (idFieldList.size() > 1) {
            // TODO 暂不支持联合主键的处理，日后版本加入
            throw new IllegalStateException(String.format("duplicated id field in class: %s for entity %s",
                    entityClass.getFullName(),
                    entityInfo.getName()));
        }

        // 2. 生成一些数据
        // 2.1. 生成父接口的类型
        FieldInfo idField = idFieldList.get(0);
        TypeInfo parentTypeInfo;

        if (this.converterConfig.isPagingEnabled()) {
            parentTypeInfo = TypeInfoFactory.fromClass(SpringDataConstant.INTERFACE_PAGING_AND_SORTING_REPOSITORY);
        } else {
            parentTypeInfo = TypeInfoFactory.fromClass(SpringDataConstant.INTERFACE_CRUD_REPOSITORY);
        }

        TypeInfo idType = idField.getType();
        if (idType.isPrimitiveType()) {
            idType = TypeInfoFactory.primitiveWrapper(idType);
        }

        parentTypeInfo = TypeInfoFactory.wrapGenerics(parentTypeInfo, new TypeInfo[]{entityClass.getType(), idType});

        String interfaceName = this.nameConverter.daoNameFromEntityName(entityInfo.getName());
        ClassInfo interfaceClass = new ClassInfo()
                .setClassName(interfaceName)
                .setPackageName(daoPackageName)
                .extendClass(parentTypeInfo)
                .setInterface(true);

        // 2.2. 生成单向关联关系当中反向的关联查询
        List<ConvertDataContext.RelationshipHandler> handlers = convertDataContext.findByHandler(entityInfo.getName());
        TypeInfo listReturnType = CollectionsTypeFactory.listWithGeneric(entityClass.getType());

        for (ConvertDataContext.RelationshipHandler relationship : handlers) {
            // 判断是否为单向
            String handledFieldName = relationship.getHandledFieldName();
            if (handledFieldName != null && !"".equals(handledFieldName.trim())) {
                continue;
            }

            // 寻找另外一方的实体
            String handledEntityName = relationship.getToBeHandled();

            ClassInfo handledClassInfo = this.convertDataContext.getClassByEntityAndType(handledEntityName, TYPE_ENTITY_CLASS);
            FieldInfo handledClassIdField = ClassInfoUtils.findSingleId(handledClassInfo);
            TypeInfo handledIdType = handledClassIdField.getType();

            String handlerFieldName = relationship.getHandlerFieldName();

            String jpql =
                    JPQLGenerator.getByAnotherEntityInRelationship(entityInfo.getName(), handledEntityName, handlerFieldName, handledClassIdField.getName());

            // 创建方法
            String methodName = this.nameConverter.daoMethodNameFindByAnotherID(handlerFieldName, handledClassIdField.getName());
            Parameter idParameter = new Parameter().setTypeInfo(idType).setName("id");

            MethodInfo queryMethod = new MethodInfo()
                    .setReturnValueType(listReturnType)
                    .addParameter(idParameter)
                    .setName(methodName)
                    .setDefineOnly(true)
                    .addAnnotationInfo(SpringDataAnnotationFactory.query(jpql));

            interfaceClass.addMethod(queryMethod);
        }

        // 2.3. 从方实体双向关系的配置
        for (ConvertDataContext.RelationshipHandler relationshipHandler : handlers) {
            // 只有双向关系才会生成相应的方法，如果是单向的关联关系，直接跳过
            String handledFieldName = relationshipHandler.getHandledFieldName();
            if (!relationshipHandler.isBidirectional()) {
                continue;
            }

            // 获得对方实体的类类元数据
            ClassInfo handledEntityClass =
                    convertDataContext.getClassByEntityAndType(relationshipHandler.getToBeHandled(), ConvertDataContext.TYPE_ENTITY_CLASS);

            // 生成方法信息
            MethodInfo methodInfo = new MethodInfo();
            methodInfo.setDefineOnly(true);

            // 生成返回值
            TypeInfo returnType = null;
            if (converterConfig.isPagingEnabled()) {
                returnType = SpringDataTypeFactory.pageTypeWithGeneric(entityClass.getType());
            } else {
                returnType = CollectionsTypeFactory.listWithGeneric(entityClass.getType());
            }

            methodInfo.setReturnValueType(returnType);

            // 生成方法的名称
            FieldInfo handledIdField = ClassInfoUtils.findSingleId(handledEntityClass);

            String methodName = String.format("findBy%s%s",
                    StringUtils.upperFirstLatter(relationshipHandler.getHandlerFieldName()),
                    StringUtils.upperFirstLatter(handledIdField.getName()));
            methodInfo.setName(methodName);

            // 增加参数
            Parameter idParameter = ParameterFactory.create(handledIdField.getType(), "id");
            methodInfo.addParameter(idParameter);

            if (converterConfig.isPagingEnabled()) {
                Parameter pageable = ParameterFactory.create(SpringDataTypeFactory.pageable(), "pageable");
                methodInfo.addParameter(pageable);
            }

            interfaceClass.addMethod(methodInfo);
        }

        // 2.4. 对于在属性信息当中配置好在数据库中会用于数据库查询条件的字段生成相应的方法
        Iterator<FieldInfo> infoIterator = entityClass.iterateFields();

        while (infoIterator.hasNext()) {
            FieldInfo fieldInfo = infoIterator.next();

            AttributeInfo attribute = this.convertDataContext.getAttribute(entityClass, fieldInfo);
            EntityRelationship relationship = this.convertDataContext.getRelationship(entityClass, fieldInfo);

            if (attribute != null && !attribute.isPrimaryIdentifier() && attribute.isAsSelectCondition()) {
                String methodName = "findBy" + StringUtils.upperFirstLatter(fieldInfo.getName());
                Parameter parameter = ParameterFactory.create(fieldInfo.getType(), fieldInfo.getName());

                MethodInfo queryMethodInfo = new MethodInfo()
                        .setPublic()
                        .setReturnValueType(listReturnType)
                        .setName(methodName)
                        .addParameter(parameter)
                        .setDefineOnly(true);
                interfaceClass.addMethod(queryMethodInfo);
            }
        }

        return interfaceClass;
    }
}
