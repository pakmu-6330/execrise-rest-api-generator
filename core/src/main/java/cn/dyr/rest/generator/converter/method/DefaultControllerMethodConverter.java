package cn.dyr.rest.generator.converter.method;

import cn.dyr.rest.generator.converter.ConvertDataContext;
import cn.dyr.rest.generator.converter.ConverterConfig;
import cn.dyr.rest.generator.converter.ConverterContext;
import cn.dyr.rest.generator.converter.ConverterInject;
import cn.dyr.rest.generator.converter.DataInject;
import cn.dyr.rest.generator.converter.name.INameConverter;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.framework.jdk.CollectionsTypeFactory;
import cn.dyr.rest.generator.framework.jdk.CollectionsValueExpressionFactory;
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
import cn.dyr.rest.generator.java.meta.factory.InstructionFactory;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.Parameter;
import cn.dyr.rest.generator.util.ClassInfoUtils;
import cn.dyr.rest.generator.util.StringUtils;
import net.oschina.util.Inflector;

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
        AnnotationInfo pageParameterAnnotation = SwaggerAnnotationFactory.springDataPageableParameter(5);

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

        TypeInfo listHandledType = CollectionsTypeFactory.listWithGeneric(handledEntityClass.getType());
        TypeInfo listHandledResourceType = CollectionsTypeFactory.listWithGeneric(handledResourceClass.getType());

        // 获得本实体的实体类信息
        String handlerEntityName = relationshipHandler.getHandler();
        ClassInfo handlerEntityClass = this.context.getClassByEntityAndType(handlerEntityName, TYPE_ENTITY_CLASS);
        String serviceFieldName = this.context.getServiceDefaultFieldName(entityName);

        FieldInfo handlerIdField = ClassInfoUtils.findSingleId(handlerEntityClass);
        MethodInfo relationGetterMethod = handlerEntityClass.getterMethod(handlerFieldName);

        MethodInfo retMethod = new MethodInfo();

        // 创建返回值
        TypeInfo returnResourceType = null;
        if (converterConfig.isPagingEnabled()) {
            ClassInfo pagedResourceClass = context.getCommonClass(ConverterContext.KEY_PAGED_RESOURCE);
            TypeInfo pagedResourceType = pagedResourceClass.getType();
            returnResourceType = TypeInfoFactory.wrapGenerics(pagedResourceType, handledEntityClass.getType());
        } else {
            returnResourceType = CollectionsTypeFactory.listWithGeneric(handledEntityClass.getType());
        }

        TypeInfo returnType = SpringMVCTypeFactory.httpEntity(returnResourceType);

        // 添加参数
        Parameter idParameter = new Parameter()
                .setTypeInfo(handlerIdField.getType())
                .setName("id")
                .addAnnotationInfo(SpringMVCAnnotationFactory.pathVariable("id"));

        // 获得方法本身的信息
        String methodName = "get" + StringUtils.upperFirstLatter(handlerFieldName);

        String entityVariableName = StringUtils.lowerFirstLatter(handlerEntityClass.getClassName());

        // 产生方法的指令
        // #1 寻找本实体的指令
        IInstruction findThisInstruction = InstructionFactory.variableDeclaration(handlerEntityClass.getType(), entityVariableName,
                ValueExpressionFactory.variable(serviceFieldName).invokeMethod("find", new Object[]{
                        ValueExpressionFactory.variable("id")
                }));

        // #3 如果本实体不存在，则返回一个空列表
        IInstruction createEmptyList = InstructionFactory.variableDeclaration(
                listHandledType,
                handlerFieldName, CollectionsValueExpressionFactory.emptyList());

        // #4 如果不存在，则返回一个空白对应的资源列表
        IValueExpression notFoundValueExpression = ValueExpressionFactory.invokeConstructor(
                SpringMVCTypeFactory.responseEntity(listHandledResourceType), new Object[]{
                        ValueExpressionFactory.variable(handledAssemblerField).invokeMethod("toResources", new Object[]{
                                ValueExpressionFactory.variable(handlerFieldName)
                        }),
                        SpringMVCValueExpressionFactory.httpStatusNotFound()
                });
        IInstruction notFoundReturnInstruction = InstructionFactory.returnInstruction(notFoundValueExpression);

        // #5 如果存在，则返回相应的资源列表
        IValueExpression returnExistValue = ValueExpressionFactory.invokeConstructor(
                SpringMVCTypeFactory.responseEntity(listHandledResourceType), new Object[]{
                        ValueExpressionFactory.variable(handledAssemblerField).invokeMethod("toResources", new Object[]{
                                ValueExpressionFactory.variable(entityVariableName).invokeMethod(relationGetterMethod.getName())
                        }),
                        SpringMVCValueExpressionFactory.httpStatusOK()
                });
        IInstruction returnExistInstruction = InstructionFactory.returnInstruction(returnExistValue);

        // #2 分支结构
        IValueExpression ifCondition =
                ValueExpressionFactory.logicalEqual(
                        ValueExpressionFactory.variable(entityVariableName),
                        ValueExpressionFactory.nullExpression());
        IInstruction ifInstruction = InstructionFactory.choiceBuilder(ifCondition,
                InstructionFactory.sequence(
                        createEmptyList,
                        notFoundReturnInstruction))
                .setElse(returnExistInstruction).build();

        return retMethod
                .setName(methodName)
                .setReturnValueType(returnType)
                .addParameter(idParameter)
                .addAnnotationInfo(SpringMVCAnnotationFactory.getMapping("/{id}/" + handlerFieldName))
                .setRootInstruction(InstructionFactory.sequence(
                        findThisInstruction,
                        ifInstruction
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
}
