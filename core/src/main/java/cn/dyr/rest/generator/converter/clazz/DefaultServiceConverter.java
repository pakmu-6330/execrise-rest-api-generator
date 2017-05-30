package cn.dyr.rest.generator.converter.clazz;

import cn.dyr.rest.generator.converter.ConvertDataContext;
import cn.dyr.rest.generator.converter.ConverterConfig;
import cn.dyr.rest.generator.converter.ConverterInject;
import cn.dyr.rest.generator.converter.ConverterInjectType;
import cn.dyr.rest.generator.converter.DataInject;
import cn.dyr.rest.generator.converter.DataInjectType;
import cn.dyr.rest.generator.converter.instruction.IServiceInstructionConverter;
import cn.dyr.rest.generator.converter.name.INameConverter;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.entity.RelationshipType;
import cn.dyr.rest.generator.framework.jdk.CollectionsTypeFactory;
import cn.dyr.rest.generator.framework.jdk.JDKAnnotationFactory;
import cn.dyr.rest.generator.framework.spring.SpringFrameworkAnnotationFactory;
import cn.dyr.rest.generator.framework.spring.data.SpringDataParameterFactory;
import cn.dyr.rest.generator.framework.spring.data.SpringDataTypeFactory;
import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.FieldInfo;
import cn.dyr.rest.generator.java.meta.MethodInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.InstructionFactory;
import cn.dyr.rest.generator.java.meta.factory.ParameterFactory;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.Parameter;
import cn.dyr.rest.generator.util.ClassInfoUtils;
import cn.dyr.rest.generator.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_DAO_INTERFACE;
import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_ENTITY_CLASS;

/**
 * 默认的 Service 类转换器的实现类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DefaultServiceConverter implements IServiceConverter {

    private static TypeInfo PAGEABLE_TYPE = SpringDataTypeFactory.pageable();

    @ConverterInject(ConverterInjectType.NAME)
    private INameConverter nameConverter;

    @DataInject(DataInjectType.DATA_CONTEXT)
    private ConvertDataContext convertDataContext;

    @ConverterInject(ConverterInjectType.SERVICE_INSTRUCTION)
    private IServiceInstructionConverter instructionConverter;

    @DataInject(DataInjectType.SERVICE_IMPL_PACKAGE_NAME)
    private String servicePackageName;

    @DataInject(DataInjectType.CONFIG)
    private ConverterConfig converterConfig;

    /**
     * 创建分页查询的方法
     *
     * @param entityInfo 实体信息
     * @return 这个实体对应的分页查询的方法
     */
    private MethodInfo pagedGetMethod(EntityInfo entityInfo) {
        ClassInfo entityClass = this.convertDataContext.getClassByEntityAndType(entityInfo.getName(), TYPE_ENTITY_CLASS);
        String daoFieldName = this.convertDataContext.getDAODefaultFieldName(entityInfo.getName());

        MethodInfo findMethod = new MethodInfo()
                .setName("find")
                .addParameter(ParameterFactory.create(PAGEABLE_TYPE, "pageable"))
                .setReturnValueType(SpringDataTypeFactory.pageTypeWithGeneric(entityClass.getType()));

        IValueExpression returnValueExpression =
                ValueExpressionFactory.variable(daoFieldName).invokeMethod("findAll", new Object[]{
                        ValueExpressionFactory.variable("pageable")
                });
        IInstruction instruction = InstructionFactory.returnInstruction(returnValueExpression);
        findMethod.setRootInstruction(instruction);

        return findMethod;
    }

    /**
     * 创建一个通过实体 ID 查询对象的查询方法
     *
     * @param entityInfo 要进行查询的实体名称
     * @return 这个实体 ID 对应的实体对象
     */
    private MethodInfo idGetMethod(EntityInfo entityInfo) {
        // 1. 寻找这个实体的 ID 类型
        ClassInfo entityClass = this.convertDataContext.getClassByEntityAndType(entityInfo.getName(), TYPE_ENTITY_CLASS);
        TypeInfo entityType = entityClass.getType();

        FieldInfo idField = ClassInfoUtils.findSingleId(entityClass);
        TypeInfo idType = idField.getType();

        // 2. 创建相应的指令
        String daoFieldName = this.convertDataContext.getDAODefaultFieldName(entityInfo.getName());
        IValueExpression returnValue = ValueExpressionFactory.variable(daoFieldName)
                .invokeMethod("findOne", new Object[]{
                        ValueExpressionFactory.variable("id")
                });
        IInstruction returnInstruction = InstructionFactory.returnInstruction(returnValue);

        // 3. 创建方法的基本信息并返回
        return new MethodInfo()
                .setName("find")
                .setPublic()
                .setReturnValueType(entityType)
                .addParameter(ParameterFactory.create(idType, "id"))
                .setRootInstruction(returnInstruction);
    }

    /**
     * 获得级联保存的方法名称
     *
     * @param entityInfo 实体对象
     * @return 处理这个实体级联保存的方法
     */
    private static String getCascadeSaveHandleMethodName(EntityInfo entityInfo) {
        return String.format("handle%sCascadeSave", StringUtils.upperFirstLatter(entityInfo.getName()));
    }

    /**
     * 获得级联删除的方法名称
     *
     * @param entityInfo 实体对象
     * @return 处理这个实体级联删除的方法
     */
    private static String getCascadeDeleteHandleMethodName(EntityInfo entityInfo) {
        return String.format("handle%sCascadeDelete", StringUtils.upperFirstLatter(entityInfo.getName()));
    }

    /**
     * 产生这个实体用作变量的变量名称
     *
     * @param entityInfo 实体信息
     * @return 对应的变量名称
     */
    private static String getEntityVariableName(EntityInfo entityInfo) {
        return StringUtils.lowerFirstLatter(entityInfo.getName());
    }

    /**
     * 获得某个特定实体的级联删除方法
     *
     * @param entityInfo 要生成级联删除的实体对象
     * @return 级联删除方法
     */
    private MethodInfo getCascadeDeleteMethod(EntityInfo entityInfo) {
        ClassInfo entityClass = this.convertDataContext.getClassByEntityAndType(entityInfo.getName(), TYPE_ENTITY_CLASS);
        FieldInfo idField = ClassInfoUtils.findSingleId(entityClass);

        String methodName = getCascadeDeleteHandleMethodName(entityInfo);

        List<IInstruction> instructionList = new ArrayList<>();

        List<ConvertDataContext.RelationshipHandler> handlers = this.convertDataContext.findByHandled(entityInfo.getName());
        for (ConvertDataContext.RelationshipHandler handler : handlers) {
            IInstruction instruction = null;

            switch (handler.getType()) {
                case ONE_TO_ONE:
                    instruction = this.instructionConverter.oneToOneHandledServiceDelete(handler, "id");
                    break;
                case ONE_TO_MANY:
                    instruction = this.instructionConverter.oneToManyHandledServiceDelete(handler, "id");
                    break;
                case MANY_TO_MANY:
                    instruction = this.instructionConverter.manyToManyHandledServiceDelete(handler, "id");
                    break;
                case MANY_TO_ONE:
                    instruction = this.instructionConverter.manyToOneHandledServiceDelete(handler, "id");
                    break;
            }

            if (instruction != null) {
                instructionList.add(InstructionFactory.singleLineComment("处理 " + handler.getHandlerFieldName() + " 的关联关系"));
                instructionList.add(instruction);
                instructionList.add(InstructionFactory.emptyInstruction());
            }
        }

        return new MethodInfo()
                .setPrivate()
                .setName(methodName)
                .addParameter(ParameterFactory.create(idField.getType(), "id"))
                .setRootInstruction(InstructionFactory.sequence(instructionList));
    }

    /**
     * 获得某个特定实体的级联保存方法
     *
     * @param entityInfo 要生成级联保存的实体对象
     * @return 级联保存方法
     */
    private MethodInfo getCascadeSaveMethod(EntityInfo entityInfo) {
        String methodName = getCascadeSaveHandleMethodName(entityInfo);
        String entityVariableName = getEntityVariableName(entityInfo);
        ClassInfo entityClass = this.convertDataContext.getClassByEntityAndType(entityInfo.getName(), TYPE_ENTITY_CLASS);

        MethodInfo methodInfo = new MethodInfo()
                .setName(methodName)
                .setPrivate()
                .addParameter(ParameterFactory.create(entityClass.getType(), entityVariableName));

        List<ConvertDataContext.RelationshipHandler> thisRelationshipHandler =
                this.convertDataContext.findByHandler(entityInfo.getName());
        List<IInstruction> relationshipOperationInstruction = new ArrayList<>();

        boolean firstRelationship = true;
        IInstruction instruction = null;

        for (ConvertDataContext.RelationshipHandler handler : thisRelationshipHandler) {
            RelationshipType type = handler.getType();

            switch (type) {
                case MANY_TO_ONE:
                    instruction = this.instructionConverter.manyToOneHandlerServiceSave(handler, entityVariableName);
                    break;
                case ONE_TO_ONE:
                    instruction = this.instructionConverter.oneToOneHandlerServiceSave(handler, entityVariableName);
                    break;
                case MANY_TO_MANY:
                    instruction = this.instructionConverter.manyToManyHandlerServiceSave(handler, entityVariableName);
                    break;
                case ONE_TO_MANY:
                    instruction = this.instructionConverter.oneToManyHandlerServiceSave(handler, entityVariableName);
                    break;
            }

            if (instruction != null) {
                if (firstRelationship) {
                    firstRelationship = false;
                } else {
                    relationshipOperationInstruction.add(InstructionFactory.emptyInstruction());
                }

                IInstruction comment = InstructionFactory.singleLineComment("处理 " + handler.getHandlerFieldName() + " 的关联关系");
                relationshipOperationInstruction.add(InstructionFactory.sequence(comment, instruction));
            }
        }

        return methodInfo.setRootInstruction(InstructionFactory.sequence(relationshipOperationInstruction));
    }

    /**
     * 创建一个级联保存实体的方法
     *
     * @param entityInfo 要进行级联保存的实体信息
     * @return 带有级联保存实体的功能
     */
    private MethodInfo getCreateMethod(EntityInfo entityInfo) {
        ClassInfo entityClass = this.convertDataContext.getClassByEntityAndType(entityInfo.getName(), TYPE_ENTITY_CLASS);

        String daoFieldName = this.convertDataContext.getDAODefaultFieldName(entityInfo.getName());
        String entityVariableName = getEntityVariableName(entityInfo);

        IValueExpression daoFieldValueExpression = ValueExpressionFactory.variable(daoFieldName);

        FieldInfo idField = ClassInfoUtils.findSingleId(entityClass);
        MethodInfo idGetterMethod = entityClass.getterMethod(idField.getName());

        MethodInfo saveMethod = new MethodInfo()
                .setName("save")
                .setReturnValueType(entityClass.getType())
                .addParameter(ParameterFactory.create(entityClass.getType(), entityVariableName))
                .addAnnotationInfo(SpringFrameworkAnnotationFactory.transactional());

        // 关联关系保存的处理
        String saveCascadeHandleMethod = getCascadeSaveHandleMethodName(entityInfo);
        IInstruction cascadeSaveInstruction =
                InstructionFactory.invoke(ValueExpressionFactory.thisReference(), saveCascadeHandleMethod, new Object[]{
                        ValueExpressionFactory.variable(entityVariableName)
                });

        // 本实体类的 DAO 调用
        IInstruction saveInstruction = InstructionFactory.invoke(daoFieldValueExpression, "save",
                new Object[]{ValueExpressionFactory.variable(entityVariableName)});
        IInstruction returnInstruction = InstructionFactory.returnInstruction(
                ValueExpressionFactory.thisReference()
                        .accessField(daoFieldName)
                        .invokeMethod("findOne", new Object[]{
                                ValueExpressionFactory.variable(entityVariableName)
                                        .invokeMethod(idGetterMethod.getName())
                        }));

        // 将所有操作关联关系的指令拼合在一起
        List<IInstruction> targetRelationshipOperationInstruction = new ArrayList<>();

        targetRelationshipOperationInstruction.add(cascadeSaveInstruction);
        targetRelationshipOperationInstruction.add(saveInstruction);
        targetRelationshipOperationInstruction.add(returnInstruction);

        saveMethod.setRootInstruction(InstructionFactory.sequence(targetRelationshipOperationInstruction));

        return saveMethod;
    }

    /**
     * 创建一个带级联删除实体的方法
     *
     * @param entityInfo 实体类型
     * @return 对应的删除操作方法
     */
    private MethodInfo getDeleteMethod(EntityInfo entityInfo) {
        ClassInfo entityClass = this.convertDataContext.getClassByEntityAndType(entityInfo.getName(), TYPE_ENTITY_CLASS);
        String daoDefaultFieldName = this.convertDataContext.getDAODefaultFieldName(entityInfo.getName());

        IValueExpression daoValueExpression = ValueExpressionFactory.variable(daoDefaultFieldName);

        TypeInfo idTypeInfo = ClassInfoUtils.getEntityIdType(entityClass);

        List<IInstruction> instructionList = new ArrayList<>();

        MethodInfo deleteMethod = new MethodInfo()
                .setName("delete")
                .setReturnValueType(entityClass.getType())
                .addParameter(ParameterFactory.create(idTypeInfo, "id"))
                .addAnnotationInfo(SpringFrameworkAnnotationFactory.transactional());

        // 调用本实体的 DAO 方法进行删除
        IValueExpression existenceCondition =
                ValueExpressionFactory.logicalInequal(
                        ValueExpressionFactory.variable("found"),
                        ValueExpressionFactory.nullExpression());

        // 寻找数据中存在的实体
        IInstruction findExistInstruction = InstructionFactory.variableDeclaration(entityClass.getType(), "found",
                daoValueExpression.invokeMethod("findOne", new Object[]{
                        ValueExpressionFactory.variable("id")
                }));

        // 调用 DAO 的删除方法前对级联的信息进行处理
        IInstruction cascadeDeleteInstruction = InstructionFactory.invoke(
                ValueExpressionFactory.thisReference(), getCascadeDeleteHandleMethodName(entityInfo), new Object[]{
                        ValueExpressionFactory.variable("id")
                });

        // 实际调用 DAO 删除方法的指令
        IInstruction deleteInstruction = InstructionFactory.invoke(daoValueExpression, "delete", new Object[]{
                ValueExpressionFactory.variable("found")
        });

        // 如果成功更改，则返回数据库原来存在的对象
        IInstruction returnFoundEntityInstruction = InstructionFactory.returnInstruction(ValueExpressionFactory.variable("found"));

        // 如果指定 id 不存在，则返回 null
        IInstruction returnNullInstruction = InstructionFactory.returnInstruction(ValueExpressionFactory.nullExpression());

        IInstruction executeInstruction = InstructionFactory.choiceBuilder(existenceCondition,
                InstructionFactory.sequence(cascadeDeleteInstruction, deleteInstruction, returnFoundEntityInstruction))
                .setElse(returnNullInstruction).build();

        instructionList.add(findExistInstruction);
        instructionList.add(executeInstruction);

        // 将所有的指令进行拼装
        IInstruction rootInstruction = InstructionFactory.sequence(instructionList);
        deleteMethod.setRootInstruction(rootInstruction);

        return deleteMethod;
    }

    /**
     * 创建一个修改实体的方法
     *
     * @param entityInfo 实体对象
     * @return 这个实体对应的修改方法
     */
    private MethodInfo getUpdateMethod(EntityInfo entityInfo) {
        ClassInfo entityClass = this.convertDataContext.getClassByEntityAndType(entityInfo.getName(), TYPE_ENTITY_CLASS);
        String daoDefaultFieldName = this.convertDataContext.getDAODefaultFieldName(entityInfo.getName());

        String entityVariableName = getEntityVariableName(entityInfo);

        IValueExpression daoFieldValueExpression = ValueExpressionFactory.variable(daoDefaultFieldName);
        IValueExpression entityVariable = ValueExpressionFactory.variable(entityVariableName);

        FieldInfo entityIdField = ClassInfoUtils.findSingleId(entityClass);
        MethodInfo idGetterMethod = entityClass.getterMethod(entityIdField.getName());

        MethodInfo updateMethod = new MethodInfo()
                .setName("update")
                .setReturnValueType(TypeInfoFactory.booleanType())
                .addParameter(ParameterFactory.create(entityClass.getType(), entityVariableName))
                .addAnnotationInfo(SpringFrameworkAnnotationFactory.transactional());

        // Service #1 查找传入 id 的 book 是否存在
        // Service #1 #1 获得本实体类的 id 字段
        Objects.requireNonNull(entityIdField, "id field not found!");
        Objects.requireNonNull(idGetterMethod, "id get method not found!");

        // Service #1 #2 检查数据库是否已经存在这个对象
        IInstruction existCheckInstruction = InstructionFactory.variableDeclaration(
                entityClass.getType(), "exists",
                daoFieldValueExpression
                        .invokeMethod("findOne", new Object[]{
                                entityVariable
                                        .invokeMethod(idGetterMethod.getName())
                        }));

        // Service #2 调用 DAO 的保存函数
        IInstruction updateInstruction = InstructionFactory.invoke(
                daoFieldValueExpression,
                "save",
                new Object[]{entityVariable}
        );

        // Service #3 返回
        IValueExpression returnExpression = ValueExpressionFactory.logicalInequal(
                ValueExpressionFactory.variable("exists"), ValueExpressionFactory.nullExpression());
        IInstruction returnInstruction = InstructionFactory.returnInstruction(returnExpression);

        IInstruction rootInstruction = InstructionFactory.sequence(
                existCheckInstruction,
                updateInstruction,
                InstructionFactory.emptyInstruction(),
                returnInstruction
        );
        updateMethod.setRootInstruction(rootInstruction);

        return updateMethod;
    }

    /**
     * 用于更新关联对象的 service 方法的生成方法
     *
     * @param entityInfo 实体对象
     * @return 这个主控实体对象维护相关关联关系方法的列表
     */
    private List<MethodInfo> relatedEntityUpdateMethods(EntityInfo entityInfo) {
        List<ConvertDataContext.RelationshipHandler> list = this.convertDataContext.findByHandler(entityInfo.getName());
        if (list == null || list.size() == 0) {
            return Collections.emptyList();
        }

        List<MethodInfo> retValues = new ArrayList<>();
        for (ConvertDataContext.RelationshipHandler handler : list) {
            MethodInfo methodInfo = relatedEntityUpdateMethod(handler);
            retValues.add(methodInfo);
        }

        return retValues;
    }

    /**
     * 用于更新关联对象的 service 方法的生成方法
     *
     * @param handler 关联关系对象
     * @return 维护这个关联关系的更新方法
     */
    private MethodInfo relatedEntityUpdateMethod(ConvertDataContext.RelationshipHandler handler) {
        String handledEntityName = handler.getToBeHandled();
        String handlerEntityName = handler.getHandler();

        ClassInfo handledEntityClass = this.convertDataContext.getClassByEntityAndType(handledEntityName, TYPE_ENTITY_CLASS);
        ClassInfo handlerEntityClass = this.convertDataContext.getClassByEntityAndType(handlerEntityName, TYPE_ENTITY_CLASS);

        String handlerFieldName = handler.getHandlerFieldName();

        // 1. 产生方法的基本信息
        FieldInfo handlerClassIdField = ClassInfoUtils.findSingleId(handlerEntityClass);

        String methodName = String.format("update%s", StringUtils.upperFirstLatter(handlerFieldName));
        String handledEntityVariableName = StringUtils.lowerFirstLatter(handlerEntityName);

        MethodInfo methodInfo = new MethodInfo()
                .setName(methodName)
                .setReturnValueType(handledEntityClass.getType())
                .addAnnotationInfo(SpringFrameworkAnnotationFactory.transactional())
                .addParameter(ParameterFactory.create(handlerClassIdField.getType(), "id"))
                .addParameter(ParameterFactory.create(handledEntityClass.getType(), handledEntityVariableName));

        // 2. 各种指令的产生
        //

        return methodInfo;
    }

    /**
     * 用于创建 Service 类中关联关系的查询方法
     *
     * @param classInfo  类信息
     * @param entityInfo 实体信息
     */
    private void handleRelatedEntityQueryMethod(ClassInfo classInfo, EntityInfo entityInfo) {
        // 寻找本实体类的 DAO 名称
        String daoFieldName = convertDataContext.getDAODefaultFieldName(entityInfo.getName());
        IValueExpression daoValueExpression = ValueExpressionFactory.variable(daoFieldName);

        // 寻找本实体类
        ClassInfo entityClass = convertDataContext.getClassByEntityAndType(entityInfo.getName(), TYPE_ENTITY_CLASS);

        // 寻找自身维护的关联关系
        List<ConvertDataContext.RelationshipHandler> handlers = convertDataContext.findByHandler(entityInfo.getName());
        if (handlers != null) {
            for (ConvertDataContext.RelationshipHandler handler : handlers) {
                // 如果只是单向关系，则忽略
                if (handler.getHandledFieldName() == null ||
                        handler.getHandledFieldName().trim().length() == 0) {
                    continue;
                }

                // 寻找对方的实体名称、ID以及关系字段名
                String handledEntityName = handler.getToBeHandled();
                String handlerFieldName = handler.getHandlerFieldName();

                ClassInfo handledEntityClass = convertDataContext.getClassByEntityAndType(handledEntityName, TYPE_ENTITY_CLASS);
                FieldInfo handledEntityId = ClassInfoUtils.findSingleId(handledEntityClass);

                // 方法的返回值
                TypeInfo returnType = null;
                if (handler.getType() == RelationshipType.MANY_TO_MANY ||
                        handler.getType() == RelationshipType.MANY_TO_ONE) {
                    if (converterConfig.isPagingEnabled()) {
                        returnType = SpringDataTypeFactory.pageTypeWithGeneric(entityClass.getType());
                    } else {
                        returnType = CollectionsTypeFactory.listWithGeneric(entityClass.getType());
                    }
                } else {
                    returnType = entityClass.getType();
                }

                // 方法的名称
                String methodName = String.format("findBy%s%s",
                        StringUtils.upperFirstLatter(handlerFieldName),
                        StringUtils.upperFirstLatter(handledEntityId.getName()));
                String handledEntityIdVariable = String.format("%s%s",
                        handlerFieldName, StringUtils.upperFirstLatter(handledEntityId.getName()));

                // 构建相关方法
                MethodInfo methodInfo = new MethodInfo();
                methodInfo.setReturnValueType(returnType);
                methodInfo.setName(methodName);

                // 创建对方 id 参数
                Parameter idParameter = ParameterFactory.create(handledEntityId.getType(), handledEntityIdVariable);
                methodInfo.addParameter(idParameter);

                // 如果启用了分页，那么添加分页数据参数
                if (converterConfig.isPagingEnabled()) {
                    Parameter pageable = ParameterFactory.create(PAGEABLE_TYPE, "pageable");
                    methodInfo.addParameter(pageable);
                }

                // 创建 Override 注解
                methodInfo.addAnnotationInfo(JDKAnnotationFactory.override());

                // 生成相应的指令
                IValueExpression returnValueExpression = null;
                if (converterConfig.isPagingEnabled()) {
                    returnValueExpression = daoValueExpression.invokeMethod(methodName, new Object[]{
                            ValueExpressionFactory.variable(idParameter.getName()),
                            ValueExpressionFactory.variable("pageable")
                    });
                } else {
                    returnValueExpression = daoValueExpression.invokeMethod(methodName, new Object[]{
                            ValueExpressionFactory.variable(idParameter.getName())
                    });
                }

                methodInfo.setRootInstruction(InstructionFactory.returnInstruction(returnValueExpression));

                classInfo.addMethod(methodInfo);
            }
        }

        // 寻找维护关联关系的对方
    }

    /**
     * 处理由关系维护方查询关系被维护方的查询方法
     *
     * @param classInfo  Service 类实例
     * @param entityInfo 实体信息类
     */
    private void handleHandledQueryMethod(ClassInfo classInfo, EntityInfo entityInfo) {
        ClassInfo thisEntityClass = convertDataContext.getClassByEntityAndType(entityInfo.getName(), TYPE_ENTITY_CLASS);
        String daoFieldName = convertDataContext.getDAODefaultFieldName(entityInfo.getName());
        IValueExpression daoFieldValueExpression = ValueExpressionFactory.variable(daoFieldName);

        List<ConvertDataContext.RelationshipHandler> handledList = convertDataContext.findByHandled(entityInfo.getName());
        if (handledList != null) {
            for (ConvertDataContext.RelationshipHandler handler : handledList) {
                String handlerEntityName = handler.getHandler();
                ClassInfo handlerEntityClass = convertDataContext.getClassByEntityAndType(handlerEntityName, TYPE_ENTITY_CLASS);
                FieldInfo handlerIdField = ClassInfoUtils.findSingleId(handlerEntityClass);

                MethodInfo queryMethod = new MethodInfo();

                // 方法名称
                String methodName = String.format("findBy%s%s",
                        StringUtils.upperFirstLatter(handlerEntityClass.getClassName()),
                        StringUtils.upperFirstLatter(handlerIdField.getName()));

                // 根据是否启用分页设置方法的返回值和参数
                queryMethod.addAnnotationInfo(JDKAnnotationFactory.override());
                queryMethod.setName(methodName);
                queryMethod.addParameter(ParameterFactory.create(handlerIdField.getType(), "id"));
                IValueExpression returnValue;

                if (converterConfig.isPagingEnabled()) {
                    queryMethod.setReturnValueType(
                            SpringDataTypeFactory.pageTypeWithGeneric(thisEntityClass.getType()));
                    queryMethod.addParameter(SpringDataParameterFactory.pageable());

                    returnValue = daoFieldValueExpression.invokeMethod(methodName, new Object[]{
                            ValueExpressionFactory.variable("id"),
                            ValueExpressionFactory.variable("pageable")
                    });
                } else {
                    queryMethod.setReturnValueType(
                            CollectionsTypeFactory.listWithGeneric(thisEntityClass.getType()));

                    returnValue = daoFieldValueExpression.invokeMethod(methodName, ValueExpressionFactory.variable("id"));
                }

                // 这个方法的指令
                queryMethod.setRootInstruction(InstructionFactory.returnInstruction(returnValue));

                // 将这个方法添加到类中
                classInfo.addMethod(queryMethod);
            }
        }
    }

    private void handleHandlerToManyMethod(ClassInfo serviceClass, EntityInfo entityInfo) {
        List<ConvertDataContext.RelationshipHandler> handlers = convertDataContext.findByHandler(entityInfo.getName());
        if (handlers != null) {
            for (ConvertDataContext.RelationshipHandler handler : handlers) {
                String handlerEntityName = handler.getHandler();
                ClassInfo handlerEntityClass = convertDataContext.getClassByEntityAndType(handlerEntityName, TYPE_ENTITY_CLASS);
                String handlerEntityVariable = StringUtils.lowerFirstLatter(handlerEntityClass.getClassName());
                FieldInfo handlerIdField = ClassInfoUtils.findSingleId(handlerEntityClass);

                String handledEntityName = handler.getToBeHandled();
                ClassInfo handledEntityClass = convertDataContext.getClassByEntityAndType(handledEntityName, TYPE_ENTITY_CLASS);
                String handledEntityVariable = StringUtils.lowerFirstLatter(handledEntityClass.getClassName());

                IInstruction instruction = this.instructionConverter.handledEntityToManyCreatedInstruction(
                        handler, "id", handledEntityVariable);
                if (instruction == null) {
                    continue;
                }

                // 创建方法名
                String methodName = String.format("create%sIn%s",
                        StringUtils.upperFirstLatter(handler.getHandlerFieldName()),
                        StringUtils.upperFirstLatter(handlerEntityClass.getClassName()));

                // 返回类型
                TypeInfo returnType = handledEntityClass.getType();

                // 方法参数
                Parameter idParameter = ParameterFactory.create(handlerIdField.getType(), "id");
                Parameter handledParameter = ParameterFactory.create(handledEntityClass.getType(), handledEntityVariable);

                // 注解信息
                AnnotationInfo transactional = SpringFrameworkAnnotationFactory.transactional();
                AnnotationInfo override = JDKAnnotationFactory.override();

                // 方法信息拼装
                MethodInfo methodInfo = new MethodInfo()
                        .setName(methodName)
                        .setReturnValueType(returnType)
                        .addParameter(idParameter)
                        .addParameter(handledParameter)
                        .addAnnotationInfo(transactional)
                        .addAnnotationInfo(override)
                        .setRootInstruction(instruction);

                // 添加到 Service 类当中
                serviceClass.addMethod(methodInfo);
            }
        }
    }

    @Override
    public ClassInfo fromEntity(EntityInfo entityInfo) {
        ClassInfo entityClass = this.convertDataContext.getClassByEntityAndType(entityInfo.getName(), TYPE_ENTITY_CLASS);
        FieldInfo entityIdField = ClassInfoUtils.findSingleId(entityClass);

        MethodInfo idGetterMethod = entityClass.getterMethod(entityIdField.getName());
        MethodInfo idSetterMethod = entityClass.setterMethod(entityIdField.getName());

        // Service 类的基本信息
        String serviceImplName = this.nameConverter.serviceImplNameFromEntityName(entityInfo.getName());
        ClassInfo serviceClass = new ClassInfo()
                .setClassName(serviceImplName)
                .setPackageName(servicePackageName)
                .addAnnotation(SpringFrameworkAnnotationFactory.service());

        // 本实体类 DAO 的应用
        ClassInfo thisDao = this.convertDataContext.getClassByEntityAndType(entityInfo.getName(), TYPE_DAO_INTERFACE);
        FieldInfo thisDaoField = new FieldInfo()
                .setName(this.nameConverter.defaultNameOfVariableName(thisDao.getClassName()))
                .setType(thisDao.getType())
                .setPrivate()
                .addAnnotation(SpringFrameworkAnnotationFactory.autowired());
        serviceClass.addField(thisDaoField);

        // 本实体类对应的的 DAO 接口名称
        IValueExpression thisEntityDAO = ValueExpressionFactory
                .thisReference()
                .accessField(this.convertDataContext.getDAODefaultFieldName(entityInfo.getName()));

        // 利用关联关系数据导入其他的 DAO 引用
        // 关联其他对象对应的 DAO
        List<ConvertDataContext.RelationshipHandler> relationshipHandledByMe = convertDataContext.findByHandler(entityInfo.getName());
        Set<String> relatedEntitiesName = new HashSet<>();

        for (ConvertDataContext.RelationshipHandler handler : relationshipHandledByMe) {
            relatedEntitiesName.add(handler.getToBeHandled());
        }

        // 被其他地方关联的 DAO
        List<ConvertDataContext.RelationshipHandler> relationshipHandledByOther = convertDataContext.findByHandled(entityInfo.getName());
        for (ConvertDataContext.RelationshipHandler handler : relationshipHandledByOther) {
            relatedEntitiesName.add(handler.getHandler());
        }

        for (String str : relatedEntitiesName) {
            ClassInfo daoInterface = this.convertDataContext.getClassByEntityAndType(str, TYPE_DAO_INTERFACE);
            FieldInfo daoField = new FieldInfo()
                    .setName(this.nameConverter.defaultNameOfVariableName(daoInterface.getClassName()))
                    .setType(daoInterface.getType())
                    .setPrivate()
                    .addAnnotation(SpringFrameworkAnnotationFactory.autowired());
            serviceClass.addField(daoField);
        }

        String nameWhereEntityAsParameter = this.nameConverter.fieldNameFromAttributeName(entityInfo.getName());
        IValueExpression entityParameter = ValueExpressionFactory.variable(nameWhereEntityAsParameter);

        // Service 类的分页查询方法
        MethodInfo pagedGetMethod = this.pagedGetMethod(entityInfo);
        pagedGetMethod.addAnnotationInfo(JDKAnnotationFactory.override());
        serviceClass.addMethod(pagedGetMethod);

        // Service 类的查询一项方法
        MethodInfo idGetMethod = this.idGetMethod(entityInfo);
        idGetMethod.addAnnotationInfo(JDKAnnotationFactory.override());
        serviceClass.addMethod(idGetMethod);

        // Service 类级联保存方法
        MethodInfo cascadeSaveMethod = this.getCascadeSaveMethod(entityInfo);
        serviceClass.addMethod(cascadeSaveMethod);

        // Service 类的增加方法
        MethodInfo createMethod = this.getCreateMethod(entityInfo);
        createMethod.addAnnotationInfo(JDKAnnotationFactory.override());
        serviceClass.addMethod(createMethod);

        // Service 类的级联删除方法
        MethodInfo cascadeDeleteMethod = this.getCascadeDeleteMethod(entityInfo);
        serviceClass.addMethod(cascadeDeleteMethod);

        // Service 类的删除方法
        MethodInfo deleteMethod = this.getDeleteMethod(entityInfo);
        deleteMethod.addAnnotationInfo(JDKAnnotationFactory.override());
        serviceClass.addMethod(deleteMethod);

        // Service 类的修改方法，使用一个花括号以控制变量的作用域
        MethodInfo updateMethod = this.getUpdateMethod(entityInfo);
        updateMethod.addAnnotationInfo(JDKAnnotationFactory.override());
        serviceClass.addMethod(updateMethod);

        // Service 关联的查询
        handleRelatedEntityQueryMethod(serviceClass, entityInfo);
        handleHandledQueryMethod(serviceClass, entityInfo);

        // 产生这个实体维护的关联关系对应的 Service 添加方法
        handleHandlerToManyMethod(serviceClass, entityInfo);

        return serviceClass;
    }
}
