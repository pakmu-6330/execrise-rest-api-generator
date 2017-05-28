package cn.dyr.rest.generator.java.meta.factory;

import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.flow.ForEachLoop;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.flow.SequenceFlow;
import cn.dyr.rest.generator.java.meta.flow.SingleLineCommentInstruction;
import cn.dyr.rest.generator.java.meta.flow.expression.ClassStaticValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.flow.instruction.AssignmentInstruction;
import cn.dyr.rest.generator.java.meta.flow.instruction.EmptyInstruction;
import cn.dyr.rest.generator.java.meta.flow.instruction.ExceptionThrowInstruction;
import cn.dyr.rest.generator.java.meta.flow.instruction.MethodInvocationInstruction;
import cn.dyr.rest.generator.java.meta.flow.instruction.ParentConstructorInvocationInstruction;
import cn.dyr.rest.generator.java.meta.flow.instruction.ReturnInstruction;
import cn.dyr.rest.generator.java.meta.flow.instruction.StaticMethodInstruction;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;

import java.util.Collection;

/**
 * 用于创建抽象指令对象的工厂对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class InstructionFactory {

    /**
     * 创建一个返回特定值的值语句
     *
     * @param returnValue 被返回的值语句
     * @return 用于执行返回值操作的指令
     */
    public static IInstruction returnInstruction(IValueExpression returnValue) {
        if (returnValue == null) {
            throw new NullPointerException("return value object cannot be null");
        }

        return new ReturnInstruction(returnValue);
    }

    /**
     * 创建一条用于方法内部声明变量
     *
     * @param typeInfo        所声明变量的类型
     * @param name            变量的名称
     * @param valueExpression 要赋值的值表达式
     * @return 对应的变量声明指令
     */
    public static IInstruction variableDeclaration(
            TypeInfo typeInfo, String name, IValueExpression valueExpression) {
        return new AssignmentInstruction()
                .setDeclaration(true)
                .setValue(valueExpression)
                .setTargetValueType(typeInfo)
                .setTargetVariable(ValueExpressionFactory.variable(name));
    }

    /**
     * 创建一个变量赋值指令
     *
     * @param name            目标变量名
     * @param valueExpression 值的对象
     * @return 这个赋值操作所对应的指令对象
     */
    public static IInstruction assignment(
            String name, IValueExpression valueExpression
    ) {
        return new AssignmentInstruction()
                .setDeclaration(false)
                .setValue(valueExpression)
                .setTargetVariable(ValueExpressionFactory.variable(name));
    }

    /**
     * 创建一个变量赋值的指令
     *
     * @param targetVariable  被赋值的值的表达式
     * @param valueExpression 值的表达式
     * @return 这个赋值操作指令所对应的指令对象
     */
    public static IInstruction assignment(
            IValueExpression targetVariable,
            IValueExpression valueExpression
    ) {
        return new AssignmentInstruction()
                .setDeclaration(false)
                .setValue(valueExpression)
                .setTargetVariable(targetVariable);
    }

    /**
     * 创建一个方法调用的指令
     *
     * @param originValue 从哪个变量或者值调用方法
     * @param methodName  要调用的方法的名称
     * @param parameters  调用方法时往方法传入的实参
     * @return 表示方法调用的指令对象
     */
    public static IInstruction invoke(
            IValueExpression originValue,
            String methodName, ParameterValue[] parameters) {
        return new MethodInvocationInstruction()
                .setValue(originValue)
                .setMethodName(methodName)
                .setParameterValues(parameters);
    }

    /**
     * 创建一个方法调用的指令
     *
     * @param originValue 从哪个变量或者值调用方法
     * @param methodName  要调用的方法的名称
     * @param parameters  调用方法时往方法传入的实参
     * @return 表示方法调用的指令
     */
    public static IInstruction invoke(
            IValueExpression originValue,
            String methodName, Object[] parameters) {
        return new MethodInvocationInstruction()
                .setValue(originValue)
                .setMethodName(methodName)
                .setParameterValues(ParameterValueFactory.fromObjects(parameters));
    }

    /**
     * 创建一个方法调用的指令
     *
     * @param originValue 从哪个变量或者值调用方法
     * @param methodName  要调用的方法的名称
     * @param parameter   调用方法时往方法传入的实参
     * @return 表示方法调用的指令
     */
    public static IInstruction invoke(
            IValueExpression originValue,
            String methodName, Object parameter
    ) {
        return new MethodInvocationInstruction()
                .setValue(originValue)
                .setMethodName(methodName)
                .setParameterValues(ParameterValueFactory.fromObjects(new Object[]{parameter}));
    }

    /**
     * 创建一个静态调用的指令
     *
     * @param classType    类
     * @param staticImport 表示这个静态调用是否通过静态 import 的方式进行引入
     * @param methodName   函数名称
     * @param parameters   参数
     * @return 静态函数调用的指令
     */
    public static IInstruction staticInvoke(
            TypeInfo classType, boolean staticImport, String methodName, ParameterValue[] parameters
    ) {
        return new StaticMethodInstruction()
                .setStaticImport(staticImport)
                .setValue(new ClassStaticValueExpression(classType))
                .setParameterValues(parameters)
                .setMethodName(methodName);
    }


    /**
     * 创建一个调用类静态函数的指令
     *
     * @param classType    类
     * @param staticImport 表示这个静态调用是否通过静态 import 的方式进行引入
     * @param methodName   函数名称
     * @param parameters   参数
     * @return 静态函数调用的指令
     */
    public static IInstruction invokeStaticMethod(
            TypeInfo classType, boolean staticImport,
            String methodName, Object[] parameters
    ) {
        return new StaticMethodInstruction()
                .setStaticImport(staticImport)
                .setValue(new ClassStaticValueExpression(classType))
                .setParameterValues(ParameterValueFactory.fromObjects(parameters))
                .setMethodName(methodName);
    }

    /**
     * 创建一个调用父类构造方法的指令
     *
     * @param parameters 调用父类构造方法时传入的参数
     * @return 调用父类构造方法的指令
     */
    public static IInstruction superConstructor(ParameterValue[] parameters) {
        return new ParentConstructorInvocationInstruction()
                .setParameterValues(parameters);
    }

    /**
     * 创建一个顺序结构的指令块
     *
     * @param instructions 要添加到顺序结构当中的指令
     * @return 创建好的顺序结构的指令块
     */
    public static IInstruction sequence(IInstruction... instructions) {
        SequenceFlow flow = new SequenceFlow();

        for (IInstruction instruction : instructions) {
            flow.addInstruction(instruction);
        }

        return flow;
    }

    /**
     * 创建一个顺序结构的指令块
     *
     * @param instructionCollection 要添加到顺序结构当中的指令
     * @return 创建好的顺序结构的指令块
     */
    public static IInstruction sequence(Collection<IInstruction> instructionCollection) {
        SequenceFlow flow = new SequenceFlow();

        for (IInstruction instruction : instructionCollection) {
            flow.addInstruction(instruction);
        }

        return flow;
    }

    /**
     * 获得一个用于创建 if 语句的 builder 语句
     *
     * @param ifCondition if 条件语句
     * @param ifBlock     if 语句块内容
     * @return builder 对象
     */
    public static ChoiceFlowBuilder choiceBuilder(IValueExpression ifCondition, IInstruction ifBlock) {
        ChoiceFlowBuilder builder = new ChoiceFlowBuilder();
        builder.setIfBlock(ifCondition, ifBlock);

        return builder;
    }

    /**
     * 创建一条空白指令
     *
     * @return 空白指令
     */
    public static IInstruction emptyInstruction() {
        return new EmptyInstruction();
    }

    /**
     * 创建一个表示 foreach 循环的空循环指令
     *
     * @param type             循环变量的类型
     * @param loopVariableName 循环变量的名称
     * @param collections      循环所迭代的集合
     * @return 对应的 foreach 循环指令
     */
    public static IInstruction forEachLoop(
            TypeInfo type, String loopVariableName,
            IValueExpression collections) {
        return new ForEachLoop()
                .setLoopVariableType(type)
                .setLoopVariableName(loopVariableName)
                .setLoopExpression(collections);
    }

    /**
     * 创建一个表示 foreach 循环的循环指令
     *
     * @param type             循环变量的类型
     * @param loopVariableName 循环变量的名称
     * @param collections      循环所迭代的结合
     * @param loopBody         循环体指令
     * @return 对应的 foreach 指令
     */
    public static IInstruction forEachLoop(
            TypeInfo type, String loopVariableName,
            IValueExpression collections, IInstruction loopBody) {
        return new ForEachLoop()
                .setLoopBody(loopBody)
                .setLoopVariableType(type)
                .setLoopVariableName(loopVariableName)
                .setLoopExpression(collections);
    }

    /**
     * 创建一个单行注释指令
     *
     * @param comment 注释内容
     * @return 对应的单行注释内容
     */
    public static IInstruction singleLineComment(String comment) {
        return new SingleLineCommentInstruction(comment);
    }

    /**
     * 创建一个抛出异常的指令
     *
     * @param typeInfo 异常的类型
     * @param message  异常的提示信息
     * @return 对应的指令
     */
    public static IInstruction throwExceptionWithMessageParameter(TypeInfo typeInfo, String message) {
        IValueExpression valueExpression = ValueExpressionFactory.invokeConstructor(typeInfo, new Object[]{message});
        return new ExceptionThrowInstruction(valueExpression);
    }
}
