package cn.dyr.rest.generator.converter.method;

import cn.dyr.rest.generator.converter.ConvertDataContext;
import cn.dyr.rest.generator.converter.ConverterConfig;
import cn.dyr.rest.generator.converter.ConverterContext;
import cn.dyr.rest.generator.converter.ConverterInject;
import cn.dyr.rest.generator.converter.DataInject;
import cn.dyr.rest.generator.converter.name.INameConverter;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.entity.RelationshipType;
import cn.dyr.rest.generator.framework.jdk.CollectionsTypeFactory;
import cn.dyr.rest.generator.framework.spring.data.SpringDataParameterFactory;
import cn.dyr.rest.generator.framework.spring.data.SpringDataTypeFactory;
import cn.dyr.rest.generator.framework.spring.mvc.SpringMVCAnnotationFactory;
import cn.dyr.rest.generator.framework.spring.mvc.SpringMVCTypeFactory;
import cn.dyr.rest.generator.framework.spring.mvc.SpringMVCValueExpressionFactory;
import cn.dyr.rest.generator.framework.swagger.DocumentGeneratorUtils;
import cn.dyr.rest.generator.framework.swagger.SwaggerAnnotationFactory;
import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.FieldInfo;
import cn.dyr.rest.generator.java.meta.MethodInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.ChoiceFlowBuilder;
import cn.dyr.rest.generator.java.meta.factory.InstructionFactory;
import cn.dyr.rest.generator.java.meta.factory.ParameterFactory;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.Parameter;
import cn.dyr.rest.generator.util.ClassInfoUtils;
import cn.dyr.rest.generator.util.StringUtils;
import net.oschina.util.Inflector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_ENTITY_CLASS;
import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_RESOURCE_CLASS;
import static cn.dyr.rest.generator.converter.ConverterInjectType.NAME;
import static cn.dyr.rest.generator.converter.DataInjectType.CONFIG;
import static cn.dyr.rest.generator.converter.DataInjectType.DATA_CONTEXT;

/**
 * 控制器方法转换的默认实现类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DefaultControllerMethodConverter implements IControllerMethodConverter {

    @DataInject(DATA_CONTEXT)
    private ConvertDataContext context;

    @DataInject(CONFIG)
    private ConverterConfig converterConfig;

    @ConverterInject(NAME)
    private INameConverter nameConverter;

    @Override
    public MethodInfo getPagedList(String entityName, int pageSize) {
        EntityInfo entityInfo = this.context.getEntityByName(entityName);

        // 方法本身的信息
        ClassInfo entityClass = this.context.getClassByEntityAndType(entityName, TYPE_ENTITY_CLASS);
        TypeInfo entityType = entityClass.getType();

        ClassInfo resourceClass = this.context.getClassByEntityAndType(entityName, TYPE_RESOURCE_CLASS);
        TypeInfo resourceType = resourceClass.getType();

        ClassInfo pagedResourceClass = this.context.getCommonClass(ConverterContext.KEY_PAGED_RESOURCE);
        TypeInfo pagedResourceType = pagedResourceClass.getType();

        TypeInfo retBodyType = TypeInfoFactory.wrapGenerics(pagedResourceType, resourceType);
        TypeInfo returnType = SpringMVCTypeFactory.httpEntity(retBodyType);

        Parameter pageableParameter = new Parameter()
                .setTypeInfo(SpringDataTypeFactory.pageable())
                .addAnnotationInfo(SpringMVCAnnotationFactory.pageableDefault(5))
                .setName("pageable");

        // 方法当中的指令
        // #1 从数据库中查询出原始的实体对象
        String serviceFieldName = this.context.getServiceDefaultFieldName(entityName);
        String assemblerFieldName = this.context.getAssemblerDefaultFieldName(entityName);

        IValueExpression serviceValueExpression = ValueExpressionFactory.variable(serviceFieldName);

        String rawVariableName = StringUtils.lowerFirstLatter(Inflector.getInstance().pluralize(entityName));
        IInstruction queryInstruction =
                InstructionFactory.variableDeclaration(SpringDataTypeFactory.pageTypeWithGeneric(entityType),
                        rawVariableName, serviceValueExpression.invokeMethod("find", new Object[]{
                                ValueExpressionFactory.variable("pageable")
                        }));

        // #2 将原始的实体对象转换成 HATEOAS 的资源对象
        String pagedVariableName = "paged" + StringUtils.upperFirstLatter(rawVariableName);
        IInstruction mapInstruction = InstructionFactory.variableDeclaration(
                SpringDataTypeFactory.pageTypeWithGeneric(resourceType),
                pagedVariableName,
                ValueExpressionFactory.variable(rawVariableName).invokeMethod("map", new Object[]{
                        ValueExpressionFactory.variable(assemblerFieldName)
                })
        );

        // #3 将这些资源打包成分页的资源
        String packagedName = (pagedVariableName.equals("pagedResource") ? "pagedResult" : "pagedResource");
        IInstruction packageInstruction = InstructionFactory.variableDeclaration(retBodyType,
                packagedName, ValueExpressionFactory.invokeConstructor(retBodyType, new Object[]{
                        ValueExpressionFactory.variable(pagedVariableName)
                }));

        // #4 返回包装好的资源指令
        IValueExpression returnValueExpression = ValueExpressionFactory.invokeConstructor(
                SpringMVCTypeFactory.responseEntity(retBodyType),
                new Object[]{
                        ValueExpressionFactory.variable(packagedName),
                        SpringMVCValueExpressionFactory.httpStatusOK()
                });
        IInstruction returnInstruction = InstructionFactory.returnInstruction(returnValueExpression);

        // 将上面这些的指令拼在一起
        IInstruction rootInstruction = InstructionFactory.sequence(
                queryInstruction,
                mapInstruction,
                InstructionFactory.emptyInstruction(),
                packageInstruction,
                returnInstruction);

        // 创建 Swagger 注解
        AnnotationInfo apiOperationAnnotation =
                SwaggerAnnotationFactory.apiOperation(DocumentGeneratorUtils.listGotten(entityInfo.getDescription()));
        AnnotationInfo pageParameterAnnotation = SwaggerAnnotationFactory.springDataPageableParameter(converterConfig.getDefaultPageSize());

        AnnotationInfo successGetAnnotation = SwaggerAnnotationFactory.createHttpOkResponse(DocumentGeneratorUtils.gotten(entityInfo.getDescription()));
        AnnotationInfo responsesAnnotation = SwaggerAnnotationFactory.apiResponses(successGetAnnotation);

        return new MethodInfo()
                .setName("get")
                .addAnnotationInfo(SpringMVCAnnotationFactory.getMapping(""))
                .addParameter(pageableParameter)
                .setReturnValueType(returnType)
                .setRootInstruction(rootInstruction)
                .addAnnotationInfo(apiOperationAnnotation)
                .addAnnotationInfo(pageParameterAnnotation)
                .addAnnotationInfo(responsesAnnotation);
    }

    @Override
    public MethodInfo getById(String entityName) {
        ClassInfo resourceClass = this.context.getClassByEntityAndType(entityName, TYPE_RESOURCE_CLASS);
        ClassInfo entityClass = this.context.getClassByEntityAndType(entityName, TYPE_ENTITY_CLASS);
        EntityInfo entityInfo = this.context.getEntityByName(entityName);

        String entityVariableName = StringUtils.lowerFirstLatter(entityClass.getClassName());

        // 寻找实体唯一标示符
        FieldInfo idFieldInfo = ClassInfoUtils.findSingleId(entityClass);

        // 创建返回值
        TypeInfo returnType = SpringMVCTypeFactory.httpEntity(resourceClass.getType());
        TypeInfo actualType = SpringMVCTypeFactory.responseEntity(resourceClass.getType());

        // 参数
        Parameter idParameter = new Parameter()
                .setName("id")
                .setTypeInfo(idFieldInfo.getType())
                .addAnnotationInfo(SpringMVCAnnotationFactory.pathVariable("id"));

        // 生成函数的指令
        // #1 从数据库中查询原始对象的指令
        String serviceFieldName = this.context.getServiceDefaultFieldName(entityName);
        String assemblerFieldName = this.context.getAssemblerDefaultFieldName(entityName);

        IValueExpression serviceValueExpression = ValueExpressionFactory.variable(serviceFieldName);
        IInstruction queryInstruction =
                InstructionFactory.variableDeclaration(entityClass.getType(), entityVariableName,
                        serviceValueExpression.invokeMethod("find", new Object[]{
                                ValueExpressionFactory.variable("id")
                        }));

        // #3 当数据库查不到的时候，返回没找到的指令
        IValueExpression notFoundResourceValueExpression = ValueExpressionFactory.invokeConstructor(resourceClass.getType());
        IValueExpression notFoundHttpStatusValueExpression = SpringMVCValueExpressionFactory.httpStatusNotFound();

        IValueExpression returnNotFoundValue = ValueExpressionFactory.invokeConstructor(actualType, new Object[]{
                notFoundResourceValueExpression, notFoundHttpStatusValueExpression
        });
        IInstruction returnNotFoundInstruction = InstructionFactory.returnInstruction(returnNotFoundValue);

        // #4 如果数据库查找到了资源，则返回这个资源
        IValueExpression resourceConvertedExpression =
                ValueExpressionFactory.variable(assemblerFieldName).invokeMethod("toResource", new Object[]{
                        ValueExpressionFactory.variable(entityVariableName)
                });
        IValueExpression okStatusValueExpression = SpringMVCValueExpressionFactory.httpStatusOK();
        IValueExpression okReturnValue = ValueExpressionFactory.invokeConstructor(actualType, new Object[]{
                resourceConvertedExpression, okStatusValueExpression
        });
        IInstruction returnResource = InstructionFactory.returnInstruction(okReturnValue);

        // #3 if 分支指令
        IValueExpression ifCondition = ValueExpressionFactory.logicalEqual(
                ValueExpressionFactory.variable(entityVariableName),
                ValueExpressionFactory.nullExpression());
        IInstruction ifInstruction =
                InstructionFactory.choiceBuilder(ifCondition, returnNotFoundInstruction).setElse(returnResource).build();

        // Swagger 注解的生成
        AnnotationInfo apiOperationAnnotation =
                SwaggerAnnotationFactory.apiOperation(DocumentGeneratorUtils.getById(entityInfo.getDescription()));
        AnnotationInfo apiParam =
                SwaggerAnnotationFactory.apiParam("id", "唯一标识符");
        AnnotationInfo notFoundResponseAnnotation =
                SwaggerAnnotationFactory.createNotFoundResponse(DocumentGeneratorUtils.notFound(entityInfo.getDescription()));
        AnnotationInfo successGettingAnnotation =
                SwaggerAnnotationFactory.createHttpOkResponse(DocumentGeneratorUtils.gotten(entityInfo.getDescription()));
        AnnotationInfo responsesAnnotation = SwaggerAnnotationFactory.apiResponses(notFoundResponseAnnotation, successGettingAnnotation);

        return new MethodInfo()
                .setName("get")
                .setReturnValueType(returnType)
                .addAnnotationInfo(SpringMVCAnnotationFactory.getMapping("/{id}"))
                .addParameter(idParameter)
                .setRootInstruction(InstructionFactory.sequence(queryInstruction, ifInstruction))
                .addAnnotationInfo(apiOperationAnnotation)
                .addAnnotationInfo(apiParam)
                .addAnnotationInfo(responsesAnnotation);
    }

    @Override
    public MethodInfo delete(String entityName) {
        ClassInfo resourceClass = this.context.getClassByEntityAndType(entityName, TYPE_RESOURCE_CLASS);
        ClassInfo entityClass = this.context.getClassByEntityAndType(entityName, TYPE_ENTITY_CLASS);

        EntityInfo entityInfo = this.context.getEntityByName(entityName);

        String serviceFieldName = this.context.getServiceDefaultFieldName(entityName);
        String assemblerFieldName = this.context.getAssemblerDefaultFieldName(entityName);
        String entityVariableName = StringUtils.lowerFirstLatter(entityClass.getClassName());

        // 寻找实体的唯一标识符
        FieldInfo idFieldInfo = ClassInfoUtils.findSingleId(entityClass);

        // 创建返回值
        TypeInfo returnType = SpringMVCTypeFactory.httpEntity(resourceClass.getType());

        // 参数
        Parameter idParameter = new Parameter()
                .setName("id")
                .setTypeInfo(idFieldInfo.getType())
                .addAnnotationInfo(SpringMVCAnnotationFactory.pathVariable("id"));

        // #1 执行 service 类的删除方法
        IInstruction executeInstruction = InstructionFactory.variableDeclaration(
                entityClass.getType(), entityVariableName,
                ValueExpressionFactory.variable(serviceFieldName).invokeMethod("delete", new Object[]{
                        ValueExpressionFactory.variable("id")
                }));

        // #3 如果特定编号的资源不存在，则返回 404 的错误
        IValueExpression notFoundReturnValue =
                ValueExpressionFactory.invokeConstructor(
                        SpringMVCTypeFactory.responseEntity(resourceClass.getType()), new Object[]{
                                ValueExpressionFactory.invokeConstructor(resourceClass.getType()),
                                SpringMVCValueExpressionFactory.httpStatusNotFound()
                        });
        IInstruction notFoundReturnInstruction = InstructionFactory.returnInstruction(notFoundReturnValue);

        // #4 如果特定编号对应的资源已经删除，则返回这个被删除的资源，返回 200 的状态码
        IValueExpression deletedReturnValue =
                ValueExpressionFactory.invokeConstructor(
                        SpringMVCTypeFactory.responseEntity(resourceClass.getType()), new Object[]{
                                ValueExpressionFactory.variable(assemblerFieldName).invokeMethod("toResource", new Object[]{
                                        ValueExpressionFactory.variable(entityVariableName)
                                }),
                                SpringMVCValueExpressionFactory.httpStatusOK()
                        });
        IInstruction deletedReturnInstruction = InstructionFactory.returnInstruction(deletedReturnValue);

        // #2 分支判断
        IValueExpression ifCondition = ValueExpressionFactory.logicalEqual(
                ValueExpressionFactory.variable(entityVariableName),
                ValueExpressionFactory.nullExpression());
        IInstruction ifInstruction = InstructionFactory
                .choiceBuilder(ifCondition, notFoundReturnInstruction)
                .setElse(deletedReturnInstruction).build();

        // Swagger 相关的注解
        AnnotationInfo apiOperation =
                SwaggerAnnotationFactory.apiOperation(DocumentGeneratorUtils.deletedById(entityInfo.getDescription()));
        AnnotationInfo ok =
                SwaggerAnnotationFactory.createHttpOkResponse(DocumentGeneratorUtils.deleteSuccess(entityInfo.getDescription()));
        AnnotationInfo notFound =
                SwaggerAnnotationFactory.createNotFoundResponse(DocumentGeneratorUtils.notFound(entityInfo.getDescription()));
        AnnotationInfo badRequest =
                SwaggerAnnotationFactory.createBadRequestResponse(DocumentGeneratorUtils.getRelatedEntityExists(entityInfo.getDescription()));
        AnnotationInfo apiResponses = SwaggerAnnotationFactory.apiResponses(ok, badRequest, notFound);

        return new MethodInfo()
                .setName("delete")
                .setReturnValueType(returnType)
                .addParameter(idParameter)
                .addAnnotationInfo(SpringMVCAnnotationFactory.deleteMapping("/{id}"))
                .setRootInstruction(InstructionFactory.sequence(
                        executeInstruction,
                        ifInstruction
                ))
                .addAnnotationInfo(apiOperation)
                .addAnnotationInfo(apiResponses);
    }

    @Override
    public MethodInfo newResource(String entityName) {
        EntityInfo entityInfo = this.context.getEntityByName(entityName);
        Objects.requireNonNull(entityInfo, "entity not found: " + entityName);

        ClassInfo entityClass = this.context.getClassByEntityAndType(entityName, TYPE_ENTITY_CLASS);
        ClassInfo resourceClass = this.context.getClassByEntityAndType(entityName, TYPE_RESOURCE_CLASS);

        String serviceFieldName = this.context.getServiceDefaultFieldName(entityName);
        String assemblerFieldName = this.context.getAssemblerDefaultFieldName(entityName);

        // 创建方法的名称
        String methodName = "post";

        // 创建返回值
        TypeInfo returnType = SpringMVCTypeFactory.httpEntity(resourceClass.getType());

        // 创建参数
        String variableName = this.nameConverter.defaultNameOfVariableName(entityClass.getClassName());
        Parameter parameter = new Parameter()
                .setName(variableName)
                .setTypeInfo(entityClass.getType())
                .addAnnotationInfo(SpringMVCAnnotationFactory.requestBody());

        // #1 移除资源原有的 ID
        FieldInfo idFieldInfo = ClassInfoUtils.findSingleId(entityClass);
        MethodInfo idSetterMethod = entityClass.setterMethod(idFieldInfo.getName());
        TypeInfo idType = idFieldInfo.getType();

        IInstruction removeIdInstruction = InstructionFactory.invoke(
                ValueExpressionFactory.variable(variableName), idSetterMethod.getName(), new Object[]{
                        ValueExpressionFactory.typeDefaultValueExpression(idType)
                });

        // #2 调用 service 的方法
        String savedVariable = "saved" + StringUtils.upperFirstLatter(variableName);
        IInstruction executeInstruction = InstructionFactory.variableDeclaration(
                entityClass.getType(), savedVariable,
                ValueExpressionFactory.variable(serviceFieldName).invokeMethod("save", new Object[]{
                        ValueExpressionFactory.variable(variableName)
                }));

        // #3 返回已经插入的资源
        IValueExpression returnValue = ValueExpressionFactory.invokeConstructor(
                SpringMVCTypeFactory.responseEntity(resourceClass.getType()), new Object[]{
                        ValueExpressionFactory.variable(assemblerFieldName).invokeMethod("toResource", new Object[]{
                                ValueExpressionFactory.variable(savedVariable)
                        }),
                        SpringMVCValueExpressionFactory.httpStatusCreated()
                });
        IInstruction returnInstruction = InstructionFactory.returnInstruction(returnValue);

        // 创建 Swagger 相关注解
        AnnotationInfo apiOperationAnnotation =
                SwaggerAnnotationFactory.apiOperation(DocumentGeneratorUtils.created(entityInfo.getDescription()));
        AnnotationInfo createdResponse =
                SwaggerAnnotationFactory.createHttpCreatedResponse(DocumentGeneratorUtils.created(entityInfo.getDescription()));
        AnnotationInfo apiResponses = SwaggerAnnotationFactory.apiResponses(createdResponse);

        return new MethodInfo()
                .setReturnValueType(returnType)
                .setName(methodName)
                .addParameter(parameter)
                .addAnnotationInfo(SpringMVCAnnotationFactory.postMapping(""))
                .setRootInstruction(InstructionFactory.sequence(
                        removeIdInstruction,
                        executeInstruction,
                        InstructionFactory.emptyInstruction(),
                        returnInstruction
                ))
                .addAnnotationInfo(apiOperationAnnotation)
                .addAnnotationInfo(apiResponses);
    }

    @Override
    public MethodInfo updateResource(String entityName) {
        ClassInfo entityClass = this.context.getClassByEntityAndType(entityName, TYPE_ENTITY_CLASS);
        ClassInfo resourceClass = this.context.getClassByEntityAndType(entityName, TYPE_RESOURCE_CLASS);

        EntityInfo entityInfo = this.context.getEntityByName(entityName);

        String serviceFieldName = this.context.getServiceDefaultFieldName(entityName);
        String assemblerFieldName = this.context.getAssemblerDefaultFieldName(entityName);

        // 创建返回值
        TypeInfo returnType = SpringMVCTypeFactory.httpEntity(resourceClass.getType());

        // 获得唯一标识符
        FieldInfo idFieldInfo = ClassInfoUtils.findSingleId(entityClass);

        // 创建方法的参数
        String entityVariableName = this.nameConverter.defaultNameOfVariableName(entityClass.getClassName());
        Parameter objectParameter = new Parameter()
                .setName(entityVariableName)
                .setTypeInfo(entityClass.getType())
                .addAnnotationInfo(SpringMVCAnnotationFactory.requestBody());

        Parameter idParameter = new Parameter()
                .setName("id")
                .setTypeInfo(idFieldInfo.getType())
                .addAnnotationInfo(SpringMVCAnnotationFactory.pathVariable("id"));

        // 创建方法的指令
        // #1 执行指令
        IInstruction executeInstruction =
                InstructionFactory.variableDeclaration(TypeInfoFactory.booleanType(), "updated",
                        ValueExpressionFactory.variable(serviceFieldName).invokeMethod("update", new Object[]{
                                ValueExpressionFactory.variable(entityVariableName)
                        }));

        // #3 如果修改成功，则返回修改后的资源和 200
        IValueExpression successReturnValue = ValueExpressionFactory.invokeConstructor(
                SpringMVCTypeFactory.responseEntity(resourceClass.getType()), new Object[]{
                        ValueExpressionFactory.variable(assemblerFieldName).invokeMethod("toResource", new Object[]{
                                ValueExpressionFactory.variable(entityVariableName),
                        }),
                        SpringMVCValueExpressionFactory.httpStatusOK()
                });
        IInstruction returnUpdateSuccessInstruction = InstructionFactory.returnInstruction(successReturnValue);

        // #4 如果指定的资源不存在，则创建资源，并返回 201
        IValueExpression createReturnValue = ValueExpressionFactory.invokeConstructor(
                SpringMVCTypeFactory.responseEntity(resourceClass.getType()), new Object[]{
                        ValueExpressionFactory.variable(assemblerFieldName).invokeMethod("toResource", new Object[]{
                                ValueExpressionFactory.variable(entityVariableName),
                        }),
                        SpringMVCValueExpressionFactory.httpStatusCreated()
                });
        IInstruction returnCreateInstruction = InstructionFactory.returnInstruction(createReturnValue);

        // #2 根据结果返回不同值的分支语句
        IInstruction ifInstruction =
                InstructionFactory.choiceBuilder(ValueExpressionFactory.variable("updated"), returnUpdateSuccessInstruction)
                        .setElse(returnCreateInstruction).build();

        // 创建 Swagger 相关的注解信息
        AnnotationInfo apiOperation =
                SwaggerAnnotationFactory.apiOperation(DocumentGeneratorUtils.updateOrCreate(entityInfo.getDescription()));
        AnnotationInfo created = SwaggerAnnotationFactory.createHttpCreatedResponse(DocumentGeneratorUtils.created(entityInfo.getDescription()));
        AnnotationInfo updated = SwaggerAnnotationFactory.createHttpOkResponse(DocumentGeneratorUtils.updated(entityInfo.getDescription()));
        AnnotationInfo apiResponses = SwaggerAnnotationFactory.apiResponses(created, updated);

        return new MethodInfo()
                .setName("update")
                .addAnnotationInfo(SpringMVCAnnotationFactory.putMapping("/{id}"))
                .setReturnValueType(returnType)
                .addParameter(objectParameter)
                .addParameter(idParameter)
                .setRootInstruction(InstructionFactory.sequence(
                        executeInstruction,
                        InstructionFactory.emptyInstruction(),
                        ifInstruction
                ))
                .addAnnotationInfo(apiOperation)
                .addAnnotationInfo(apiResponses);
    }

    /**
     * 处理对一的关联关系
     *
     * @param entityName          实体名称
     * @param relationshipHandler 关系元数据
     * @return 对应的方法对象
     */
    private MethodInfo handleToOneRelationshipConvert(String entityName,
                                                      ConvertDataContext.RelationshipHandler relationshipHandler) {
        // 方法名称
        String methodName = "get" + StringUtils.upperFirstLatter(relationshipHandler.getHandlerFieldName());

        // 寻找关联对方的实体类和资源类
        String handledEntityName = relationshipHandler.getToBeHandled();
        ClassInfo handledEntityClass = this.context.getClassByEntityAndType(handledEntityName, TYPE_ENTITY_CLASS);
        ClassInfo handledResourceClass = this.context.getClassByEntityAndType(handledEntityName, TYPE_RESOURCE_CLASS);
        String handledAssemblerFieldName = this.context.getAssemblerDefaultFieldName(handledEntityName);

        // 寻找本类的实体类和资源类
        String handlerEntityName = relationshipHandler.getHandler();
        ClassInfo handlerEntityClass = this.context.getClassByEntityAndType(handlerEntityName, TYPE_ENTITY_CLASS);
        MethodInfo relationshipGetterMethod = handlerEntityClass.getterMethod(relationshipHandler.getHandlerFieldName());

        String serviceFieldName = this.context.getServiceDefaultFieldName(entityName);
        String entityVariableName = StringUtils.lowerFirstLatter(handlerEntityClass.getClassName());

        // 方法的返回值
        TypeInfo returnType = SpringMVCTypeFactory.httpEntity(handledResourceClass.getType());

        // 实体类的唯一标识符
        FieldInfo handlerIdField = ClassInfoUtils.findSingleId(handlerEntityClass);

        // 方法参数
        Parameter idParameter = new Parameter()
                .setName("id")
                .setTypeInfo(handlerIdField.getType())
                .addAnnotationInfo(SpringMVCAnnotationFactory.pathVariable("id"));

        // 创建方法当中的指令
        // #1 寻找到关联本体的实体对象
        IInstruction findThisInstruction = InstructionFactory.variableDeclaration(
                handlerEntityClass.getType(), entityVariableName,
                ValueExpressionFactory.variable(serviceFieldName).invokeMethod("find", new Object[]{
                        ValueExpressionFactory.variable("id")
                }));

        // #3 如果没有找到，则返回空白对象和 404
        IValueExpression notFoundReturnValueExpression = ValueExpressionFactory.invokeConstructor(
                SpringMVCTypeFactory.responseEntity(handledResourceClass.getType()), new Object[]{
                        ValueExpressionFactory.variable(handledAssemblerFieldName).invokeMethod("toResource", new Object[]{
                                ValueExpressionFactory.invokeConstructor(handledEntityClass.getType())
                        }),
                        SpringMVCValueExpressionFactory.httpStatusNotFound()
                });
        IInstruction notFoundReturnInstruction = InstructionFactory.returnInstruction(notFoundReturnValueExpression);

        // #4 如果找到，则转换成相应的资源对象和 200
        IValueExpression foundReturnValue = ValueExpressionFactory.invokeConstructor(
                SpringMVCTypeFactory.responseEntity(handledResourceClass.getType()), new Object[]{
                        ValueExpressionFactory.variable(handledAssemblerFieldName).invokeMethod("toResource", new Object[]{
                                ValueExpressionFactory.variable(entityVariableName).invokeMethod(relationshipGetterMethod.getName())
                        }),
                        SpringMVCValueExpressionFactory.httpStatusOK()
                });
        IInstruction foundReturnInstruction = InstructionFactory.returnInstruction(foundReturnValue);

        // #2 根据结果判断的分支结构
        IInstruction ifInstruction = InstructionFactory.choiceBuilder(
                ValueExpressionFactory.logicalEqual(ValueExpressionFactory.variable(entityVariableName), ValueExpressionFactory.nullExpression()),
                notFoundReturnInstruction).setElse(foundReturnInstruction).build();

        return new MethodInfo()
                .addAnnotationInfo(SpringMVCAnnotationFactory.getMapping("/{id}/" + relationshipHandler.getHandlerFieldName()))
                .setReturnValueType(returnType)
                .setName(methodName)
                .addParameter(idParameter)
                .setRootInstruction(InstructionFactory.sequence(findThisInstruction, ifInstruction));
    }

    /**
     * 处理对多关系的方法
     *
     * @param entityName          本实体的名称
     * @param relationshipHandler 关联关系信息
     * @return 方法信息
     */
    private MethodInfo handleToManyRelationshipConvert(String entityName,
                                                       ConvertDataContext.RelationshipHandler relationshipHandler) {

        // 获取关联关系的元数据
        String handlerFieldName = relationshipHandler.getHandlerFieldName();

        // 关联对方的实体类和资源类
        String handledEntityName = relationshipHandler.getToBeHandled();
        ClassInfo handledEntityClass = this.context.getClassByEntityAndType(handledEntityName, TYPE_ENTITY_CLASS);
        ClassInfo handledResourceClass = this.context.getClassByEntityAndType(handledEntityName, TYPE_RESOURCE_CLASS);
        String handledAssemblerField = this.context.getAssemblerDefaultFieldName(handledEntityName);
        String handledServiceFieldName = this.context.getServiceDefaultFieldName(handledEntityName);
        String handledEntityVariableName = StringUtils.lowerFirstLatter(StringUtils.upperFirstLatter(handlerFieldName));

        TypeInfo listHandledType = CollectionsTypeFactory.listWithGeneric(handledEntityClass.getType());
        TypeInfo listHandledResourceType = CollectionsTypeFactory.listWithGeneric(handledResourceClass.getType());

        // 获得本实体的实体类信息
        String handlerEntityName = relationshipHandler.getHandler();
        EntityInfo thisEntity = this.context.getEntityByName(handlerEntityName);
        ClassInfo handlerEntityClass = this.context.getClassByEntityAndType(handlerEntityName, TYPE_ENTITY_CLASS);

        FieldInfo handlerIdField = ClassInfoUtils.findSingleId(handlerEntityClass);

        MethodInfo retMethod = new MethodInfo();

        // 创建返回值
        TypeInfo returnResourceType;
        ClassInfo pagedResourceClass = context.getCommonClass(ConverterContext.KEY_PAGED_RESOURCE);
        TypeInfo pagedResourceType = pagedResourceClass.getType();

        if (converterConfig.isPagingEnabled()) {
            // 获得对方实体相应的 HATEOAS 资源类
            ClassInfo resourceClass = context.getClassByEntityAndType(handledEntityName, TYPE_RESOURCE_CLASS);
            returnResourceType = TypeInfoFactory.wrapGenerics(pagedResourceType, resourceClass.getType());
        } else {
            ClassInfo resourceClass = context.getClassByEntityAndType(handledEntityName, TYPE_RESOURCE_CLASS);
            returnResourceType = CollectionsTypeFactory.listWithGeneric(resourceClass.getType());
        }

        // 添加参数
        Parameter idParameter = new Parameter()
                .setTypeInfo(handlerIdField.getType())
                .setName("id")
                .addAnnotationInfo(SpringMVCAnnotationFactory.pathVariable("id"));
        retMethod.addParameter(idParameter);

        if (converterConfig.isPagingEnabled()) {
            // 这里创建相关的 Swagger 注解
            AnnotationInfo sizeSwaggerAnnotationInfo =
                    SwaggerAnnotationFactory.springDataPageableSizeAnnotation(converterConfig.getDefaultPageSize());
            AnnotationInfo pageSwaggerAnnotationInfo = SwaggerAnnotationFactory.springDataPageablePageAnnotation();
            AnnotationInfo idSwaggerAnnotationInfo =
                    SwaggerAnnotationFactory.implicitPathParam(
                            "id", idParameter.getTypeInfo().getName(),
                            String.format("%s的唯一标识符", thisEntity.getDescription()), "0");
            AnnotationInfo implicitParams =
                    SwaggerAnnotationFactory.implicitParams(sizeSwaggerAnnotationInfo, pageSwaggerAnnotationInfo, idSwaggerAnnotationInfo);
            retMethod.addAnnotationInfo(implicitParams);

            // 之类创建方法的参数
            retMethod.addParameter(SpringDataParameterFactory.pageable(converterConfig.getDefaultPageSize()));
        } else {
            // 这里创建相关的 Swagger 注解
            AnnotationInfo idAnnotation =
                    SwaggerAnnotationFactory.apiParam("id", String.format("%s的唯一标识符", thisEntity.getDescription()));
            retMethod.addAnnotationInfo(idAnnotation);
        }

        // 获得方法本身的信息
        String methodName = "get" + StringUtils.upperFirstLatter(handlerFieldName);
        retMethod.setName(methodName);

        // 生成相关的指令
        // #1 调用相关的 Service 类获得数据
        // 获得对方实体对应的 Service 字段名称
        TypeInfo rawEntityType = SpringDataTypeFactory.pageTypeWithGeneric(handledEntityClass.getType());
        String serviceMethodName = String.format("findBy%s%s",
                StringUtils.upperFirstLatter(handlerEntityClass.getClassName()),
                StringUtils.upperFirstLatter(handlerIdField.getName()));

        // 产生相应的变量
        IInstruction serviceInvocationInstruction = null;
        if (converterConfig.isPagingEnabled()) {
            serviceInvocationInstruction =
                    InstructionFactory.variableDeclaration(rawEntityType, handledEntityVariableName,
                            ValueExpressionFactory.variable(handledServiceFieldName)
                                    .invokeMethod(serviceMethodName, new Object[]{
                                            ValueExpressionFactory.variable("id"),
                                            ValueExpressionFactory.variable("pageable")
                                    }));
        } else {
            serviceInvocationInstruction =
                    InstructionFactory.variableDeclaration(rawEntityType, handledEntityVariableName,
                            ValueExpressionFactory.variable(handledServiceFieldName)
                                    .invokeMethod(serviceMethodName, new Object[]{
                                            ValueExpressionFactory.variable("id")
                                    }));
        }

        // #2 进行转换
        // 获得当前对方实体的 HATEOAS 资源类
        TypeInfo pagedHandledResourceType =
                SpringDataTypeFactory.pageTypeWithGeneric(handledResourceClass.getType());
        String pagedHandledResourceVariableName = String.format("paged%s",
                StringUtils.upperFirstLatter(handledEntityVariableName));
        IInstruction mapInstruction = InstructionFactory.variableDeclaration(pagedHandledResourceType, pagedHandledResourceVariableName,
                ValueExpressionFactory.variable(handledEntityVariableName)
                        .invokeMethod("map", ValueExpressionFactory.variable(handledAssemblerField)));

        // #3 创建相应的 PagedResource 对象
        TypeInfo pagedHandledEntityResource =
                TypeInfoFactory.wrapGenerics(pagedResourceType, handledResourceClass.getType());
        IInstruction pagedResourceAssemblerInstruction =
                InstructionFactory.variableDeclaration(pagedHandledEntityResource, "pagedResource",
                        ValueExpressionFactory.invokeConstructor(pagedHandledEntityResource, new Object[]{
                                ValueExpressionFactory.variable(pagedHandledResourceVariableName)
                        }));

        // #4 返回相应的 ResponseEntity 对象
        IValueExpression returnValue =
                ValueExpressionFactory.invokeConstructor(SpringMVCTypeFactory.responseEntity(pagedHandledEntityResource),
                        new Object[]{
                                ValueExpressionFactory.variable("pagedResource"),
                                SpringMVCValueExpressionFactory.httpStatusOK()
                        });
        IInstruction returnInstruction = InstructionFactory.returnInstruction(returnValue);

        return retMethod
                .setReturnValueType(SpringMVCTypeFactory.httpEntity(returnResourceType))
                .addAnnotationInfo(SpringMVCAnnotationFactory.getMapping("/{id}/" + handlerFieldName))
                .setRootInstruction(InstructionFactory.sequence(
                        serviceInvocationInstruction,
                        mapInstruction,
                        InstructionFactory.emptyInstruction(),
                        pagedResourceAssemblerInstruction,
                        returnInstruction
                ));
    }

    @Override
    public MethodInfo getRelatedResourceById(String entityName,
                                             ConvertDataContext.RelationshipHandler relationshipHandler) {
        MethodInfo info;
        String handledEntityName = relationshipHandler.getToBeHandled();

        EntityInfo entityInfo = this.context.getEntityByName(entityName);
        EntityInfo handledEntityInfo = this.context.getEntityByName(handledEntityName);

        switch (relationshipHandler.getType()) {
            case MANY_TO_MANY:
            case ONE_TO_MANY:
                info = handleToManyRelationshipConvert(entityName, relationshipHandler);
                break;
            case MANY_TO_ONE:
            case ONE_TO_ONE:
                info = handleToOneRelationshipConvert(entityName, relationshipHandler);
                break;
            default:
                throw new IllegalArgumentException("program should not go here..");
        }

        // 创建 Swagger 相关的注解
        AnnotationInfo apiOperation =
                SwaggerAnnotationFactory.apiOperation(DocumentGeneratorUtils.getRelatedEntityInfo(entityInfo.getDescription(), handledEntityInfo.getDescription()));
        AnnotationInfo ok =
                SwaggerAnnotationFactory.createHttpOkResponse(DocumentGeneratorUtils.gotten(handledEntityInfo.getDescription()));
        AnnotationInfo notFound =
                SwaggerAnnotationFactory.createNotFoundResponse(DocumentGeneratorUtils.notFound(handledEntityInfo.getDescription()));
        AnnotationInfo responses = SwaggerAnnotationFactory.apiResponses(ok, notFound);

        return info.addAnnotationInfo(apiOperation).addAnnotationInfo(responses);
    }

    private IInstruction createInstructionForGetRelatedResourcesCreateForHandler(
            String entityName, ConvertDataContext.RelationshipHandler handler) {
        String handlerEntityName = handler.getHandler();
        ClassInfo handlerEntityClass = context.getClassByEntityAndType(handlerEntityName, TYPE_ENTITY_CLASS);

        String handledEntityName = handler.getToBeHandled();
        ClassInfo handledEntityClass = context.getClassByEntityAndType(handledEntityName, TYPE_ENTITY_CLASS);
        ClassInfo handledResourceClass = context.getClassByEntityAndType(handledEntityName, TYPE_RESOURCE_CLASS);

        String serviceFieldName = context.getServiceDefaultFieldName(entityName);

        String newEntityObjectName = String.format("new%s", StringUtils.upperFirstLatter(handledEntityClass.getClassName()));
        IValueExpression newEntityObjectValueExpression = ValueExpressionFactory.variable(newEntityObjectName);

        String handledEntityObjectForParameter = StringUtils.lowerFirstLatter(handledEntityClass.getClassName());
        String handledResourceAssemblerFieldName = context.getAssemblerDefaultFieldName(handledEntityName);

        List<IInstruction> instructionList = new ArrayList<>();

        // #1 调用业务类的方法指令
        {
            // 业务类的方法名
            String serviceMethodName = String.format("create%sIn%s",
                    StringUtils.upperFirstLatter(handler.getHandlerFieldName()),
                    StringUtils.upperFirstLatter(handlerEntityClass.getClassName()));
            IInstruction serviceInvocationInstruction =
                    InstructionFactory.variableDeclaration(handledEntityClass.getType(), newEntityObjectName,
                            ValueExpressionFactory.variable(serviceFieldName).invokeMethod(serviceMethodName, new Object[]{
                                    ValueExpressionFactory.variable("id"),
                                    ValueExpressionFactory.variable(handledEntityObjectForParameter)
                            }));
            instructionList.add(serviceInvocationInstruction);
        }

        // #2 根据方法的返回值判断是否添加成功
        {
            // 判断是否操作成功的条件
            IValueExpression condition = ValueExpressionFactory.logicalEqual(
                    newEntityObjectValueExpression, ValueExpressionFactory.nullExpression());

            // #3 如果没有执行成功，则创建一个没有任何内容的实体对象
            IInstruction createEmptyInstruction = InstructionFactory.assignment(
                    newEntityObjectName, ValueExpressionFactory.invokeConstructor(handledEntityClass.getType()));

            // #4 返回这个没有任何内容的实体对象并返回 404 响应码
            IValueExpression toResourceMethodValue = ValueExpressionFactory.variable(handledResourceAssemblerFieldName)
                    .invokeMethod("toResource", newEntityObjectValueExpression);

            IValueExpression notFoundReturnValue = ValueExpressionFactory.invokeConstructor(
                    SpringMVCTypeFactory.responseEntity(handledResourceClass.getType()), new Object[]{
                            toResourceMethodValue,
                            SpringMVCValueExpressionFactory.httpStatusNotFound()
                    });
            IInstruction notFoundReturnInstruction = InstructionFactory.returnInstruction(notFoundReturnValue);

            // #5 返回已经添加到数据库当中的实体信息对象，并返回 201 的状态码
            IValueExpression successReturnValue = ValueExpressionFactory.invokeConstructor(
                    SpringMVCTypeFactory.responseEntity(handledResourceClass.getType()), new Object[]{
                            toResourceMethodValue,
                            SpringMVCValueExpressionFactory.httpStatusCreated()
                    });
            IInstruction successReturnInstruction = InstructionFactory.returnInstruction(successReturnValue);

            ChoiceFlowBuilder builder = new ChoiceFlowBuilder()
                    .setIfBlock(condition, InstructionFactory.sequence(createEmptyInstruction, notFoundReturnInstruction))
                    .setElse(successReturnInstruction);
            IInstruction ifInstruction = builder.build();
            instructionList.add(ifInstruction);
        }

        return InstructionFactory.sequence(instructionList);
    }

    @Override
    public MethodInfo getRelatedResourcesCreateForHandler(String entityName, ConvertDataContext.RelationshipHandler relationshipHandler) {
        if (relationshipHandler.getType() == RelationshipType.ONE_TO_ONE ||
                relationshipHandler.getType() == RelationshipType.MANY_TO_ONE) {
            return null;
        }

        // 获得关联关系对方的实体信息
        String handledEntityName = relationshipHandler.getToBeHandled();
        ClassInfo handledEntityClass = context.getClassByEntityAndType(handledEntityName, TYPE_ENTITY_CLASS);
        ClassInfo handledResourceClass = context.getClassByEntityAndType(handledEntityName, TYPE_RESOURCE_CLASS);
        String handlerFieldName = relationshipHandler.getHandlerFieldName();

        // 获得本实体的信息
        ClassInfo thisEntityClass = context.getClassByEntityAndType(entityName, TYPE_ENTITY_CLASS);
        EntityInfo thisEntity = context.getEntityByName(entityName);
        FieldInfo thisEntityIdField = ClassInfoUtils.findSingleId(thisEntityClass);

        // 创建相应的注解信息
        // SpringMVC 路由注解
        String routeURI = String.format("/{id}/%s", handlerFieldName);
        AnnotationInfo postMappingAnnotation = SpringMVCAnnotationFactory.postMapping(routeURI);

        // Swagger ApiOperation 注解
        AnnotationInfo apiOperationAnnotation =
                SwaggerAnnotationFactory.apiOperation(DocumentGeneratorUtils.createSthInSpecifiedSth(
                        relationshipHandler.getHandlerFieldDescription(), thisEntity.getDescription()));

        // Swagger Api Responses 注解
        AnnotationInfo responsesAnnotation = SwaggerAnnotationFactory.apiResponses(
                SwaggerAnnotationFactory.createHttpCreatedResponse(
                        DocumentGeneratorUtils.createdSuccess(relationshipHandler.getHandlerFieldDescription())),
                SwaggerAnnotationFactory.createNotFoundResponse(
                        DocumentGeneratorUtils.notFound(thisEntity.getDescription()))
        );

        // 创建方法的返回值
        TypeInfo returnType = SpringMVCTypeFactory.httpEntity(handledResourceClass.getType());

        // 方法名
        String methodName = String.format("create%s", StringUtils.upperFirstLatter(relationshipHandler.getHandlerFieldName()));

        // 方法的参数
        Parameter idParameter = ParameterFactory.create(thisEntityIdField.getType(), "id");
        idParameter.addAnnotationInfo(SpringMVCAnnotationFactory.pathVariable("id"));

        Parameter bodyParameter = ParameterFactory.create(handledEntityClass.getType(), StringUtils.lowerFirstLatter(handledEntityClass.getClassName()));
        bodyParameter.addAnnotationInfo(SpringMVCAnnotationFactory.requestBody());

        // # 临时创建返回 null 的指令
        IInstruction instruction = createInstructionForGetRelatedResourcesCreateForHandler(entityName, relationshipHandler);

        return new MethodInfo()
                .setName(methodName)
                .setReturnValueType(returnType)
                .addParameter(idParameter)
                .addParameter(bodyParameter)
                .addAnnotationInfo(apiOperationAnnotation)
                .addAnnotationInfo(postMappingAnnotation)
                .addAnnotationInfo(responsesAnnotation)
                .setRootInstruction(instruction);
    }

    @Override
    public MethodInfo getRelatedResourcesDeleteForHandler(String entityName, ConvertDataContext.RelationshipHandler relationshipHandler) {


        return null;
    }
}
