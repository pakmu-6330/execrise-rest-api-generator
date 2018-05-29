package cn.dyr.rest.generator.converter.instruction;

import cn.dyr.rest.generator.converter.ConvertDataContext;
import cn.dyr.rest.generator.converter.ConverterConfig;
import cn.dyr.rest.generator.converter.ConverterContext;
import cn.dyr.rest.generator.converter.ConverterInject;
import cn.dyr.rest.generator.converter.ConverterInjectType;
import cn.dyr.rest.generator.converter.DataInject;
import cn.dyr.rest.generator.converter.DataInjectType;
import cn.dyr.rest.generator.converter.name.INameConverter;
import cn.dyr.rest.generator.entity.RelationshipType;
import cn.dyr.rest.generator.framework.jdk.CollectionsTypeFactory;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.FieldInfo;
import cn.dyr.rest.generator.java.meta.MethodInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.ChoiceFlowBuilder;
import cn.dyr.rest.generator.java.meta.factory.InstructionFactory;
import cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.project.Project;
import cn.dyr.rest.generator.util.ClassInfoUtils;
import cn.dyr.rest.generator.util.StringUtils;
import com.sun.org.apache.bcel.internal.generic.IINC;
import net.oschina.util.Inflector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_DAO_INTERFACE;
import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_ENTITY_CLASS;
import static cn.dyr.rest.generator.java.meta.factory.InstructionFactory.assignment;
import static cn.dyr.rest.generator.java.meta.factory.InstructionFactory.emptyInstruction;
import static cn.dyr.rest.generator.java.meta.factory.InstructionFactory.invoke;
import static cn.dyr.rest.generator.java.meta.factory.InstructionFactory.returnInstruction;
import static cn.dyr.rest.generator.java.meta.factory.InstructionFactory.sequence;
import static cn.dyr.rest.generator.java.meta.factory.InstructionFactory.throwExceptionWithMessageParameter;
import static cn.dyr.rest.generator.java.meta.factory.InstructionFactory.variableDeclaration;
import static cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory.greaterThan;
import static cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory.intExpression;
import static cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory.invokeConstructor;
import static cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory.logicalEqual;
import static cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory.logicalInequal;
import static cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory.nullExpression;
import static cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory.typeDefaultValueExpression;
import static cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory.variable;

/**
 * 指令转换的实现类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DefaultServiceInstructionConverter implements IServiceInstructionConverter {

    @DataInject(DataInjectType.DATA_CONTEXT)
    private ConvertDataContext convertDataContext;

    @DataInject(DataInjectType.CONFIG)
    private ConverterConfig converterConfig;

    @DataInject(DataInjectType.PROJECT)
    private Project project;

    @ConverterInject(ConverterInjectType.NAME)
    private INameConverter nameConverter;

    private IInstruction toOneSave(ConvertDataContext.RelationshipHandler handler, String entityVariableName) {
        // 获得主控方和被控方的名称
        String handlerEntityName = handler.getHandler();
        String handledEntityName = handler.getToBeHandled();

        // 获得主控方和被控方对应的实体类元信息
        ClassInfo handlerEntityClass =
                convertDataContext.getClassByEntityAndType(handlerEntityName, TYPE_ENTITY_CLASS);

        // 获得被控方对应的 DAO 接口信息
        ClassInfo handledDAOInterface =
                convertDataContext.getClassByEntityAndType(handledEntityName, TYPE_DAO_INTERFACE);

        // 获得主控方中关联关系字段的 set 和 get 方法
        String handlerFieldName = handler.getHandlerFieldName();
        MethodInfo getterMethod = handlerEntityClass.getterMethod(handlerFieldName);
        MethodInfo setterMethod = handlerEntityClass.setterMethod(handlerFieldName);

        Objects.requireNonNull(getterMethod, handlerFieldName + " getter method not found!");
        Objects.requireNonNull(setterMethod, handlerFieldName + " setter method not found!");

        // 本实体类的参数变量值
        IValueExpression entityVariable = variable(entityVariableName);

        // 关联对象的变量值
        IValueExpression relatedEntityInstanceVariable = variable(handlerFieldName);

        // #1 获得关联关系类的命令
        IInstruction getterInstruction = variableDeclaration(
                getterMethod.getReturnValueType(), handlerFieldName,
                variable(entityVariableName).invokeMethod(getterMethod.getName()));

        // 获得关联实体的 id 名称，变量，获得指令等信息
        String relatedEntityIdName = handlerFieldName + "Id";
        IValueExpression relatedEntityIdVariable = variable(relatedEntityIdName);

        // #3 获得关联对象的唯一标识符
        ClassInfo relatedClassInfo = this.convertDataContext.getClassByEntityAndType(handledEntityName, TYPE_ENTITY_CLASS);
        Objects.requireNonNull(relatedClassInfo, "entity class of " + handledEntityName + " not found!");

        FieldInfo relatedClassIdField = ClassInfoUtils.findSingleId(relatedClassInfo);
        Objects.requireNonNull(relatedClassIdField, "id field of entity class " + relatedClassInfo.getFullName() + " not found!");

        MethodInfo relatedIdGetter = relatedClassInfo.getterMethod(relatedClassIdField.getName());
        Objects.requireNonNull(relatedIdGetter, "id get method of entity class " + relatedClassInfo.getFullName() + " not found!");

        IInstruction relatedIdGetInstruction = variableDeclaration(
                relatedIdGetter.getReturnValueType(), relatedEntityIdName,
                relatedEntityInstanceVariable.invokeMethod(relatedIdGetter.getName()));

        // #5 级联保存关联对象
        IInstruction cascadeSaveInstruction = invoke(entityVariable, setterMethod.getName(), new Object[]{
                variable(convertDataContext.getDAODefaultFieldName(handledEntityName))
                        .invokeMethod("save", new Object[]{
                        relatedEntityInstanceVariable
                })
        });

        // #6 获得数据库中已有的关联对象
        IValueExpression getExpression = null;
        if (project.getSpringBootVersion().getMajorVersion() == 1) {
            getExpression = variable(convertDataContext.getDAODefaultFieldName(handledEntityName))
                    .invokeMethod("findOne", new Object[]{relatedEntityIdVariable});
        } else {
            getExpression = variable(convertDataContext.getDAODefaultFieldName(handledEntityName))
                    .invokeMethod("findById", new Object[]{relatedEntityIdVariable})
                    .invokeMethod("orElse", ValueExpressionFactory.nullExpression());
        }

        IInstruction getAndSetInstruction = invoke(entityVariable, setterMethod.getName(), new Object[]{getExpression});

        // #4 判断关联对象的唯一标识符是否为默认值
        IValueExpression relatedEntityIdIsDefault = logicalEqual(relatedEntityIdVariable,
                ValueExpressionFactory.typeDefaultValueExpression(relatedIdGetter.getReturnValueType()));
        IInstruction saveOrGetInstruction = InstructionFactory.choiceBuilder(relatedEntityIdIsDefault, cascadeSaveInstruction)
                .setElse(getAndSetInstruction).build();

        // #2 判断关联对象是否为空
        IValueExpression relatedEntityNullCondition =
                logicalInequal(relatedEntityInstanceVariable, ValueExpressionFactory.nullExpression());
        IInstruction handleInstruction = InstructionFactory.choiceBuilder(relatedEntityNullCondition,
                InstructionFactory.sequence(relatedIdGetInstruction, saveOrGetInstruction)).build();

        return InstructionFactory.sequence(getterInstruction, handleInstruction);
    }

    private IInstruction toManySave(ConvertDataContext.RelationshipHandler handler, String entityVariable) {
        // 获得关联类信息
        String handlerEntityName = handler.getHandler();
        String handledEntityName = handler.getToBeHandled();

        ClassInfo handlerEntityClass = this.convertDataContext.getClassByEntityAndType(handlerEntityName, TYPE_ENTITY_CLASS);
        ClassInfo handledEntityClass = this.convertDataContext.getClassByEntityAndType(handledEntityName, TYPE_ENTITY_CLASS);

        // 关键类对应的 DAO 接口
        String handledDAOName = this.convertDataContext.getDAODefaultFieldName(handledEntityName);

        // 关联字段的字段名
        String handlerFieldName = handler.getHandlerFieldName();

        // 获得这个关联字段单数名称
        // 规则，直接尝试转换成单数，转换后如果仍然与原来字符串相等，则添加 single 前缀
        String singleClass = Inflector.getInstance().singularize(handlerFieldName);
        if (singleClass.equals(handlerFieldName)) {
            singleClass = "single" + StringUtils.upperFirstLatter(handlerFieldName);
        }

        // 关联实体的唯一标识符变量名、类型、get 方法等信息
        String idName = singleClass + "Id";

        FieldInfo idField = ClassInfoUtils.findSingleId(handledEntityClass);
        Objects.requireNonNull(idField, "id field not found in class " + handledEntityClass.getFullName());

        MethodInfo idGetterMethod = handledEntityClass.getterMethod(idField.getName());
        MethodInfo idSetterMethod = handledEntityClass.setterMethod(idField.getName());

        // 关联字段的类型
        TypeInfo handledCollectionType = CollectionsTypeFactory.arrayListWithGeneric(handledEntityClass.getType());

        // 关联字段的 get set 方法
        MethodInfo getterMethod = handlerEntityClass.getterMethod(handlerFieldName);
        MethodInfo setterMethod = handlerEntityClass.setterMethod(handlerFieldName);

        // #1 拿到关联关系的集合类
        IInstruction collectionsGetInstruction = variableDeclaration(getterMethod.getReturnValueType(), handlerFieldName,
                variable(entityVariable).invokeMethod(getterMethod.getName(), new Object[]{}));

        // #2 创建一个新的关联关系的集合类
        String newCollectionName = "new" + StringUtils.upperFirstLatter(handler.getHandlerFieldName());
        IInstruction newCollectionInstruction =
                variableDeclaration(handledCollectionType, newCollectionName,
                        ValueExpressionFactory.invokeConstructor(handledCollectionType));

        // #5 创建获得唯一标识符的指令
        IInstruction entityClassIdGetInstruction = variableDeclaration(
                idGetterMethod.getReturnValueType(), idName,
                variable(singleClass).invokeMethod(idGetterMethod.getName()));

        // #7 级联保存关联对象
        IInstruction cascadeSaveInstruction = invoke(
                variable(newCollectionName), "add",
                new Object[]{
                        variable(handledDAOName)
                                .invokeMethod("save", new Object[]{
                                variable(singleClass)
                        })
                });

        // #8 获得数据库当中的记录，添加到新的集合当中
        IValueExpression existsExpression = null;
        if (project.getSpringBootVersion().getMajorVersion() == 1) {
            existsExpression = variable(handledDAOName).invokeMethod("findOne", variable(idName));
        } else {
            existsExpression = variable(handledDAOName).invokeMethod("findById", variable(idName))
                    .invokeMethod("orElse", ValueExpressionFactory.nullExpression());
        }

        IInstruction getExistsEntityInstruction = invoke(
                variable(newCollectionName), "add",
                new Object[]{existsExpression});

        // #6 级联保存还是从数据库里面重新取对象的判断指令
        IValueExpression saveOrGetIfValueExpression =
                logicalEqual(variable(idName),
                        ValueExpressionFactory.typeDefaultValueExpression(idGetterMethod.getReturnValueType()));
        IInstruction saveOrGetInstruction =
                InstructionFactory.choiceBuilder(saveOrGetIfValueExpression, cascadeSaveInstruction)
                        .setElse(getExistsEntityInstruction).build();

        // #4 对关联集合当中的每一个元素进行迭代
        IInstruction loopInstruction =
                InstructionFactory.forEachLoop(handledEntityClass.getType(),
                        singleClass, variable(newCollectionName),
                        InstructionFactory.sequence(entityClassIdGetInstruction, saveOrGetInstruction));

        // #9 将新的集合设置到本实体当中
        IInstruction setValueInstruction =
                invoke(variable(entityVariable),
                        setterMethod.getName(), new Object[]{
                                variable(newCollectionName)
                        });

        // #3 判断集合是否为空的 if 语句
        IValueExpression ifCondition = ValueExpressionFactory.logicalAnd(
                logicalInequal(
                        variable(handlerFieldName),
                        ValueExpressionFactory.nullExpression()),
                greaterThan(
                        variable(handlerFieldName)
                                .invokeMethod("size"),
                        intExpression(0)
                )
        );
        IInstruction ifInstruction =
                InstructionFactory.choiceBuilder(ifCondition, InstructionFactory.sequence(
                        loopInstruction,
                        InstructionFactory.emptyInstruction(),
                        setValueInstruction
                )).build();

        return InstructionFactory.sequence(
                collectionsGetInstruction,
                newCollectionInstruction,
                ifInstruction);
    }

    @Override
    public IInstruction oneToOneHandlerServiceSave(ConvertDataContext.RelationshipHandler handler, String entityVariable) {
        return toOneSave(handler, entityVariable);
    }

    @Override
    public IInstruction oneToManyHandlerServiceSave(ConvertDataContext.RelationshipHandler handler, String entityVariable) {
        return toManySave(handler, entityVariable);
    }

    @Override
    public IInstruction manyToOneHandlerServiceSave(ConvertDataContext.RelationshipHandler handler, String entityVariable) {
        return toOneSave(handler, entityVariable);
    }

    @Override
    public IInstruction manyToManyHandlerServiceSave(ConvertDataContext.RelationshipHandler handler, String entityVariable) {
        return toManySave(handler, entityVariable);
    }

    private IInstruction handleServiceDelete(ConvertDataContext.RelationshipHandler handler, String idVariableName) {
        // 获得关系维护方的信息
        String handlerEntityName = handler.getHandler();
        String handledEntityName = handler.getToBeHandled();

        ClassInfo handlerEntityClass = this.convertDataContext.getClassByEntityAndType(handlerEntityName, TYPE_ENTITY_CLASS);
        ClassInfo handledEntityClass = this.convertDataContext.getClassByEntityAndType(handledEntityName, TYPE_ENTITY_CLASS);
        String handlerDAODefaultFieldName = this.convertDataContext.getDAODefaultFieldName(handler.getHandler());
        String handledDAODefaultFieldName = this.convertDataContext.getDAODefaultFieldName(handler.getToBeHandled());

        // 根据类似的规则
        String methodName = this.nameConverter.reversedDAOQueryMethodName(handler);
        String checkResultName = methodName + "Result";

        List<IInstruction> instructions = new ArrayList<>();

        // #1 反向查询相应的列表（如果关联关系为单向）
        // #1 如果关联关系为双向，则获得当前对象，根据相应的 get 方法获得
        TypeInfo handlerResultListType = CollectionsTypeFactory.listWithGeneric(handlerEntityClass.getType());

        if (!handler.isBidirectional()) {
            IInstruction reversedQueryInstruction = variableDeclaration(handlerResultListType, checkResultName,
                    variable(handlerDAODefaultFieldName).invokeMethod(methodName, new Object[]{
                            variable(idVariableName)
                    }));
            instructions.add(reversedQueryInstruction);
        } else {
            IValueExpression existsExpression = null;
            if (project.getSpringBootVersion().getMajorVersion() == 1) {
                existsExpression = ValueExpressionFactory.variable(handledDAODefaultFieldName)
                        .invokeMethod("findOne", ValueExpressionFactory.variable(idVariableName));
            } else {
                existsExpression = ValueExpressionFactory.variable(handledDAODefaultFieldName)
                        .invokeMethod("findById", ValueExpressionFactory.variable(idVariableName))
                        .invokeMethod("orElse", ValueExpressionFactory.nullExpression());
            }

            // #1.1 寻找到指定编号的本对象
            IInstruction findThisEntityInstruction = InstructionFactory.variableDeclaration(handledEntityClass.getType(), "entity", existsExpression);
            instructions.add(findThisEntityInstruction);

            // #1.2 将这个对象关联的列表赋值到指定的变量上
            MethodInfo listGetterMethod = handledEntityClass.getterMethod(handler.getHandledFieldName());
            IInstruction assignmentInstruction = InstructionFactory.variableDeclaration(handlerResultListType, checkResultName,
                    ValueExpressionFactory.variable("entity").invokeMethod(listGetterMethod.getName()));
            instructions.add(assignmentInstruction);

            // #1.3 为了好看，增加一行空语句
            instructions.add(InstructionFactory.emptyInstruction());
        }

        // #3 抛出异常
        ClassInfo dbConstraintClass = this.convertDataContext.getCommonClass(ConverterContext.KEY_DB_CONSTRAINT_EXCEPTION);
        IInstruction exceptionThrowInstruction = throwExceptionWithMessageParameter(
                dbConstraintClass.getType(),
                String.format("failed to delete %s because of the reference from %s by field called %s",
                        handler.getToBeHandled(), handler.getHandler(), handler.getHandlerFieldName()));

        // #2 引用判断的指令
        IValueExpression ifCondition = ValueExpressionFactory.logicalAnd(
                logicalInequal(variable(checkResultName), ValueExpressionFactory.nullExpression()),
                greaterThan(variable(checkResultName).invokeMethod("size"), intExpression(0))
        );
        IInstruction ifInstruction = InstructionFactory.choiceBuilder(ifCondition, exceptionThrowInstruction).build();
        instructions.add(ifInstruction);

        return InstructionFactory.sequence(instructions);
    }

    @Override
    public IInstruction oneToOneHandledServiceDelete(ConvertDataContext.RelationshipHandler handler, String idVariableName) {
        return handleServiceDelete(handler, idVariableName);
    }

    @Override
    public IInstruction oneToManyHandledServiceDelete(ConvertDataContext.RelationshipHandler handler, String idVariableName) {
        return handleServiceDelete(handler, idVariableName);
    }

    @Override
    public IInstruction manyToOneHandledServiceDelete(ConvertDataContext.RelationshipHandler handler, String idVariableName) {
        return handleServiceDelete(handler, idVariableName);
    }

    @Override
    public IInstruction manyToManyHandledServiceDelete(ConvertDataContext.RelationshipHandler handler, String idVariableName) {
        return handleServiceDelete(handler, idVariableName);
    }

    @Override
    public IInstruction entityToManyCreatedInstructionForHandler(ConvertDataContext.RelationshipHandler handler,
                                                                 String idVariable, String handledEntityObjectVariable) {
        // 判断是否为对多的关联关系
        if (handler.getType() == RelationshipType.MANY_TO_ONE ||
                handler.getType() == RelationshipType.ONE_TO_ONE) {
            return null;
        }

        // 准备数据
        // 关联关系维护方的各种类
        String handlerEntityName = handler.getHandler();
        ClassInfo handlerEntityClass = convertDataContext.getClassByEntityAndType(handlerEntityName, TYPE_ENTITY_CLASS);
        String handlerDAOFieldName = convertDataContext.getDAODefaultFieldName(handlerEntityName);
        IValueExpression handlerDAOFieldValueExpression = ValueExpressionFactory.variable(handlerDAOFieldName);

        String handlerFieldName = handler.getHandlerFieldName();
        String handlerCollectionsVariableName = StringUtils.lowerFirstLatter(handlerFieldName);
        IValueExpression handlerCollectionsValueExpression = variable(handlerCollectionsVariableName);
        MethodInfo handlerFieldGetterMethod = handlerEntityClass.getterMethod(handlerFieldName);
        MethodInfo handlerFieldSetterMethod = handlerEntityClass.setterMethod(handlerFieldName);

        // 维护方实体对象变量名
        String handlerEntityVariableName = StringUtils.lowerFirstLatter(handlerEntityClass.getClassName());
        IValueExpression handlerEntityValueExpression = variable(handlerEntityVariableName);

        // 关联关系被维护方的各种类
        String handledEntityName = handler.getToBeHandled();
        ClassInfo handledEntityClass = convertDataContext.getClassByEntityAndType(handledEntityName, TYPE_ENTITY_CLASS);
        FieldInfo handledEntityIdField = ClassInfoUtils.findSingleId(handledEntityClass);
        MethodInfo handledEntityIdSetterMethod = handledEntityClass.setterMethod(handledEntityIdField.getName());
        String handledEntityDAOFieldName = convertDataContext.getDAODefaultFieldName(handledEntityName);
        IValueExpression handledEntityObjectVariableValueExpression = variable(handledEntityObjectVariable);

        // 一个用于保存已经生成的的指令的列表
        List<IInstruction> instructionList = new ArrayList<>();
        List<IInstruction> choiceYesList = new ArrayList<>();
        List<IInstruction> choiceNoList = new ArrayList<>();

        // #1 寻找关联维护方对象
        {
            IValueExpression handlerValueExpression = null;
            if (project.getSpringBootVersion().getMajorVersion() == 1) {
                handlerValueExpression = handlerDAOFieldValueExpression
                        .invokeMethod("findOne", ValueExpressionFactory.variable(idVariable));
            } else {
                handlerValueExpression = handlerDAOFieldValueExpression
                        .invokeMethod("findById", ValueExpressionFactory.variable(idVariable))
                        .invokeMethod("orElse", ValueExpressionFactory.nullExpression());
            }

            IInstruction findHandlerEntityInstruction = InstructionFactory.variableDeclaration(
                    handlerEntityClass.getType(), handlerEntityVariableName, handlerValueExpression);
            instructionList.add(findHandlerEntityInstruction);
        }

        // #3 找不到关联维护方的实体对象则直接返回 null
        {
            IInstruction nullInstruction = returnInstruction(nullExpression());
            choiceYesList.add(nullInstruction);
        }

        // #4 将被维护实体对象的唯一标识符设置为默认值
        {
            IInstruction setDefaultIdInstruction = invoke(handledEntityObjectVariableValueExpression,
                    handledEntityIdSetterMethod.getName(),
                    typeDefaultValueExpression(handledEntityIdField.getType()));
            choiceNoList.add(setDefaultIdInstruction);
        }

        // #5 将被维护实体对象保存到数据库
        {
            IInstruction saveHandledObjectInstruction = invoke(variable(handledEntityDAOFieldName),
                    "save", handledEntityObjectVariableValueExpression);
            choiceNoList.add(saveHandledObjectInstruction);
        }

        // #6 获得关联方与被维护实体对象之间的集合类
        {
            IValueExpression value = handlerEntityValueExpression.invokeMethod(handlerFieldGetterMethod.getName());
            IInstruction getCollectionsInstruction = variableDeclaration(
                    CollectionsTypeFactory.listWithGeneric(handledEntityClass.getType()),
                    handlerCollectionsVariableName, value);
            choiceNoList.add(emptyInstruction());
            choiceNoList.add(getCollectionsInstruction);
        }

        // #7 确保集合不为空的操作指令
        {
            // 判断条件
            IValueExpression condition = logicalEqual(handlerCollectionsValueExpression, nullExpression());

            // #8 创建一个新的集合类
            IInstruction assignmentInstruction = assignment(handlerCollectionsVariableName,
                    invokeConstructor(CollectionsTypeFactory.arrayListWithGeneric(handledEntityClass.getType())));

            // #9 将这个新的集集合类设置到关联类实体对象中
            IInstruction setCollectionInstruction = invoke(handlerEntityValueExpression,
                    handlerFieldSetterMethod.getName(), new Object[]{handlerCollectionsValueExpression});

            ChoiceFlowBuilder builder = new ChoiceFlowBuilder()
                    .setIfBlock(condition, sequence(assignmentInstruction, setCollectionInstruction));
            IInstruction targetInstruction = builder.build();
            choiceNoList.add(targetInstruction);
        }

        // #10 将被维护方实体添加到集合类中
        {
            IInstruction addInstruction = invoke(handlerCollectionsValueExpression, "add",
                    new Object[]{handledEntityObjectVariableValueExpression});
            choiceNoList.add(emptyInstruction());
            choiceNoList.add(addInstruction);
        }

        // #11 更新维护方的实体对象
        {
            IInstruction handlerSaveInstruction = invoke(handlerDAOFieldValueExpression, "save", handlerEntityValueExpression);
            choiceNoList.add(handlerSaveInstruction);
        }

        // #2 判断维护方实体是否存在
        {
            IValueExpression condition = logicalEqual(handlerEntityValueExpression, nullExpression());
            ChoiceFlowBuilder builder = new ChoiceFlowBuilder()
                    .setIfBlock(condition, sequence(choiceYesList))
                    .setElse(sequence(choiceNoList));
            IInstruction ifInstruction = builder.build();
            instructionList.add(ifInstruction);
        }

        // #12 返回被关联实体对象
        {
            IInstruction returnInstruction = returnInstruction(handledEntityObjectVariableValueExpression);
            instructionList.add(emptyInstruction());
            instructionList.add(returnInstruction);
        }

        return sequence(instructionList);
    }

    @Override
    public IInstruction entityManyToOneCreatedInstructionForHandled(
            ConvertDataContext.RelationshipHandler handler,
            String handledIdVariable, String handlerEntityVariable) {
        // 判判断这个适用条件
        if (handler.getType() != RelationshipType.MANY_TO_ONE) {
            return null;
        }

        // 如果被维护方没有早维护方的关联字段，则说明不需要生成相应的逻辑
        if (StringUtils.isStringEmpty(handler.getHandledFieldName())) {
            return null;
        }

        // 准备相关数据
        // 1. 关联关系主控方的一些相关数据
        String handlerEntityName = handler.getHandler();
        ClassInfo handlerEntityClass = convertDataContext.getClassByEntityAndType(handlerEntityName, TYPE_ENTITY_CLASS);
        FieldInfo handlerEntityIdField = ClassInfoUtils.findSingleId(handlerEntityClass);
        MethodInfo handlerEntityIdSetter = handlerEntityClass.setterMethod(handlerEntityIdField.getName());

        String handlerDAOFieldName = convertDataContext.getDAODefaultFieldName(handlerEntityName);
        IValueExpression handlerDAOFieldValueExpression = ValueExpressionFactory.variable(handlerDAOFieldName);

        IValueExpression handlerEntityVariableValueExpression = ValueExpressionFactory.variable(handlerEntityVariable);

        String handlerFieldName = handler.getHandlerFieldName();
        MethodInfo handlerFieldSetterMethod = handlerEntityClass.setterMethod(handlerFieldName);

        // 2. 关联关系被控方的一些相关数据
        String handledEntityName = handler.getToBeHandled();
        ClassInfo handledEntityClass = convertDataContext.getClassByEntityAndType(handledEntityName, TYPE_ENTITY_CLASS);

        String handledDAOFieldName = convertDataContext.getDAODefaultFieldName(handledEntityName);
        String handledEntityVariable = StringUtils.lowerFirstLatter(handledEntityClass.getClassName());

        IValueExpression handledIdVariableValueExpression = ValueExpressionFactory.variable(handledIdVariable);
        IValueExpression handledEntityVariableValueExpression = ValueExpressionFactory.variable(handledEntityVariable);

        // 3. 用于存放生成指令的集合类
        List<IInstruction> instructionList = new ArrayList<>();

        // #1 找到关联关系被控方的对象
        {
            IValueExpression daoInvokeValueExpression = null;
            if (project.getSpringBootVersion().getMajorVersion() == 1) {
                daoInvokeValueExpression = ValueExpressionFactory.variable(handledDAOFieldName)
                        .invokeMethod("findOne", handledIdVariableValueExpression);
            } else {
                daoInvokeValueExpression = ValueExpressionFactory.variable(handledDAOFieldName)
                        .invokeMethod("findById", handledIdVariableValueExpression)
                        .invokeMethod("orElse", ValueExpressionFactory.nullExpression());
            }
            IInstruction daoInvokeInstruction = InstructionFactory.variableDeclaration(
                    handledEntityClass.getType(), handledEntityVariable, daoInvokeValueExpression);
            instructionList.add(daoInvokeInstruction);
        }

        // #2 判断被维护方对象是否存在
        {
            // 判断条件
            IValueExpression condition = ValueExpressionFactory.logicalEqual(handledEntityVariableValueExpression, ValueExpressionFactory.nullExpression());

            // #3 返回 null 的指令
            IInstruction returnNullInstruction = InstructionFactory.returnInstruction(ValueExpressionFactory.nullExpression());

            ChoiceFlowBuilder builder = new ChoiceFlowBuilder()
                    .setIfBlock(condition, returnNullInstruction);
            IInstruction ifInstruction = builder.build();

            instructionList.add(ifInstruction);
        }

        // #4 清空维护关系实体的唯一标识符
        {
            IInstruction clearHandlerIdInstruction =
                    InstructionFactory.invoke(handlerEntityVariableValueExpression,
                            handlerEntityIdSetter.getName(),
                            ValueExpressionFactory.typeDefaultValueExpression(handlerEntityIdField.getType()));
            instructionList.add(InstructionFactory.emptyInstruction());
            instructionList.add(clearHandlerIdInstruction);
        }

        // #5 将维护方实体保存到数据库当中
        {
            IInstruction saveInstruction = InstructionFactory
                    .invoke(handlerDAOFieldValueExpression, "save",
                            handlerEntityVariableValueExpression);
            instructionList.add(saveInstruction);
        }

        // #6 建立维护方实体和被维护方实体之间的关系
        {
            IInstruction relationshipBuildInstruction =
                    InstructionFactory.invoke(handlerEntityVariableValueExpression,
                            handlerFieldSetterMethod.getName(), handledEntityVariableValueExpression);
            instructionList.add(InstructionFactory.emptyInstruction());
            instructionList.add(relationshipBuildInstruction);
        }

        // #7 将关系已经建立的维护方实体保存到数据库中
        {
            IInstruction saveInstruction = InstructionFactory
                    .invoke(handlerDAOFieldValueExpression, "save",
                            handlerEntityVariableValueExpression);
            instructionList.add(saveInstruction);
        }

        // #8 返回已经保存到数据库中的主控方实体对象
        {
            IInstruction returnInstruction = InstructionFactory.returnInstruction(handlerEntityVariableValueExpression);
            instructionList.add(InstructionFactory.emptyInstruction());
            instructionList.add(returnInstruction);
        }

        return InstructionFactory.sequence(instructionList);
    }

    @Override
    public IInstruction entityToOneUpdateInstructionForHandler(
            ConvertDataContext.RelationshipHandler handler,
            String handlerIdVariable, String handledEntityVariable) {
        // 先检查是否满足这个逻辑的使用条件
        if (handler.getType() == RelationshipType.ONE_TO_MANY ||
                handler.getType() == RelationshipType.MANY_TO_MANY) {
            return null;
        }

        IValueExpression handledEntityVariableValueExpression = ValueExpressionFactory.variable(handledEntityVariable);

        // 这里准备维护方实体的一些数据
        String handlerEntityName = handler.getHandler();
        ClassInfo handlerEntityClass = convertDataContext.getClassByEntityAndType(handlerEntityName, TYPE_ENTITY_CLASS);

        String handlerEntityVariableName = StringUtils.lowerFirstLatter(handlerEntityClass.getClassName());
        IValueExpression handlerEntityVariableValueExpression = ValueExpressionFactory.variable(handlerEntityVariableName);

        String handlerDAOFieldName = convertDataContext.getDAODefaultFieldName(handlerEntityName);
        IValueExpression handlerDAOFieldValueExpression = ValueExpressionFactory.variable(handlerDAOFieldName);

        String handlerFieldName = handler.getHandlerFieldName();
        MethodInfo handlerFieldSetterMethod = handlerEntityClass.setterMethod(handlerFieldName);

        // 这里准备被维护方实体的一些数据
        String handledEntityName = handler.getToBeHandled();
        ClassInfo handledEntityClass = convertDataContext.getClassByEntityAndType(handledEntityName, TYPE_ENTITY_CLASS);

        String handledDAOFieldName = convertDataContext.getDAODefaultFieldName(handledEntityName);
        IValueExpression handledDAOFieldValueExpression = ValueExpressionFactory.variable(handledDAOFieldName);

        FieldInfo handledIdField = ClassInfoUtils.findSingleId(handledEntityClass);
        MethodInfo handledIdGetterMethod = handledEntityClass.getterMethod(handledIdField.getName());

        List<IInstruction> instructionList = new ArrayList<>();

        // #1 根据维护方实体编号查找数据库中已经存在的实体
        {
            IValueExpression daoInvokeExpression = null;
            if (project.getSpringBootVersion().getMajorVersion() == 1) {
                daoInvokeExpression = handlerDAOFieldValueExpression
                        .invokeMethod("findOne", ValueExpressionFactory.variable(handlerIdVariable));
            } else {
                daoInvokeExpression = handlerDAOFieldValueExpression
                        .invokeMethod("findById", ValueExpressionFactory.variable(handlerIdVariable))
                        .invokeMethod("orElse", ValueExpressionFactory.nullExpression());
            }

            IInstruction findHandlerInstruction = InstructionFactory.variableDeclaration(
                    handlerEntityClass.getType(), handlerEntityVariableName, daoInvokeExpression);
            instructionList.add(findHandlerInstruction);
        }

        // #2, #3 根据寻找结果进行判断
        {
            IValueExpression condition = ValueExpressionFactory.logicalEqual(
                    handlerEntityVariableValueExpression, ValueExpressionFactory.nullExpression());

            IInstruction returnInstruction = InstructionFactory.returnNull();
            IInstruction ifInstruction = new ChoiceFlowBuilder().setIfBlock(condition, returnInstruction).build();
            instructionList.add(ifInstruction);
        }

        // #4 检查传入的被维护方是否存在
        {
            IValueExpression existsValueExpression = null;

            IValueExpression getHandledEntityId = handledEntityVariableValueExpression.invokeMethod(handledIdGetterMethod.getName());
            if (project.getSpringBootVersion().getMajorVersion() == 1) {
                existsValueExpression = handledDAOFieldValueExpression
                        .invokeMethod("findOne", getHandledEntityId);
            } else {
                existsValueExpression = handledDAOFieldValueExpression
                        .invokeMethod("findById", getHandledEntityId)
                        .invokeMethod("orElse", ValueExpressionFactory.nullExpression());
            }

            IInstruction checkExistenceInstruction =
                    InstructionFactory.variableDeclaration(handledEntityClass.getType(), "exists", existsValueExpression);
            instructionList.add(InstructionFactory.emptyInstruction());
            instructionList.add(checkExistenceInstruction);
        }

        // #5, #6, #7 三条指令
        {
            IValueExpression existsValueExpression = ValueExpressionFactory.variable("exists");
            IValueExpression condition = ValueExpressionFactory.logicalEqual(existsValueExpression, ValueExpressionFactory.nullExpression());

            IInstruction saveInstruction = InstructionFactory.invoke(
                    handledDAOFieldValueExpression, "save", handledEntityVariableValueExpression);
            IInstruction assignmentInstruction = InstructionFactory.assignment(
                    handledEntityVariable, existsValueExpression);

            IInstruction ifInstruction = new ChoiceFlowBuilder().setIfBlock(condition, saveInstruction)
                    .setElse(assignmentInstruction).build();
            instructionList.add(ifInstruction);
        }

        // #8 变更维护方实体和被维护方实体之间的关系
        {
            IInstruction setFieldInstruction = InstructionFactory.invoke(
                    handlerEntityVariableValueExpression, handlerFieldSetterMethod.getName(),
                    handledEntityVariableValueExpression);
            instructionList.add(InstructionFactory.emptyInstruction());
            instructionList.add(setFieldInstruction);
        }

        // #9 通过维护方实体 DAO 对修改关系以后的维护方实体进行保存
        {
            IInstruction saveInstruction = InstructionFactory.invoke(
                    handlerDAOFieldValueExpression, "save", handlerEntityVariableValueExpression);
            instructionList.add(saveInstruction);
        }

        // #10 对修改以后的被维护方对象进行返回
        {
            IInstruction returnInstruction = InstructionFactory.returnInstruction(handledEntityVariableValueExpression);
            instructionList.add(InstructionFactory.emptyInstruction());
            instructionList.add(returnInstruction);
        }

        return InstructionFactory.sequence(instructionList);
    }
}
