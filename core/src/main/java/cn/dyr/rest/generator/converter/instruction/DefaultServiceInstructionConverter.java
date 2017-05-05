package cn.dyr.rest.generator.converter.instruction;

import cn.dyr.rest.generator.converter.ConvertDataContext;
import cn.dyr.rest.generator.converter.ConverterContext;
import cn.dyr.rest.generator.converter.ConverterInject;
import cn.dyr.rest.generator.converter.ConverterInjectType;
import cn.dyr.rest.generator.converter.DataInject;
import cn.dyr.rest.generator.converter.DataInjectType;
import cn.dyr.rest.generator.converter.name.INameConverter;
import cn.dyr.rest.generator.framework.jdk.CollectionsTypeFactory;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.FieldInfo;
import cn.dyr.rest.generator.java.meta.MethodInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.InstructionFactory;
import cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.util.ClassInfoUtils;
import cn.dyr.rest.generator.util.StringUtils;
import net.oschina.util.Inflector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_DAO_INTERFACE;
import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_ENTITY_CLASS;
import static cn.dyr.rest.generator.java.meta.factory.InstructionFactory.throwExceptionWithMessageParameter;
import static cn.dyr.rest.generator.java.meta.factory.InstructionFactory.variableDeclaration;
import static cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory.greaterThan;
import static cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory.intExpression;
import static cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory.logicalInequal;
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
        IInstruction cascadeSaveInstruction = InstructionFactory.invoke(
                entityVariable, setterMethod.getName(), new Object[]{
                        variable(convertDataContext.getDAODefaultFieldName(handledEntityName))
                                .invokeMethod("save", new Object[]{
                                relatedEntityInstanceVariable
                        })
                });

        // #6 获得数据库中已有的关联对象
        IInstruction getAndSetInstruction = InstructionFactory.invoke(entityVariable, setterMethod.getName(), new Object[]{
                variable(convertDataContext.getDAODefaultFieldName(handledEntityName))
                        .invokeMethod("findOne", new Object[]{
                        relatedEntityIdVariable
                })
        });

        // #4 判断关联对象的唯一标识符是否为默认值
        IValueExpression relatedEntityIdIsDefault = ValueExpressionFactory.logicalEqual(
                relatedEntityIdVariable,
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
        IInstruction cascadeSaveInstruction = InstructionFactory.invoke(
                variable(newCollectionName), "add",
                new Object[]{
                        variable(handledDAOName)
                                .invokeMethod("save", new Object[]{
                                variable(singleClass)
                        })
                });

        // #8 获得数据库当中的记录，添加到新的集合当中
        IInstruction getExistsEntityInstruction = InstructionFactory.invoke(
                variable(newCollectionName), "add",
                new Object[]{
                        variable(handledDAOName)
                                .invokeMethod("findOne", new Object[]{
                                variable(idName)
                        })
                });

        // #6 级联保存还是从数据库里面重新取对象的判断指令
        IValueExpression saveOrGetIfValueExpression =
                ValueExpressionFactory.logicalEqual(variable(idName),
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
                InstructionFactory.invoke(variable(entityVariable),
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
            // #1.1 寻找到指定编号的本对象
            IInstruction findThisEntityInstruction = InstructionFactory.variableDeclaration(handledEntityClass.getType(), "entity",
                    ValueExpressionFactory.variable(handledDAODefaultFieldName).invokeMethod("findOne", new Object[]{
                            ValueExpressionFactory.variable(idVariableName)
                    }));
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
}
