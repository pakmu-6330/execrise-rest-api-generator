package cn.dyr.rest.generator.java.meta.flow;

import cn.dyr.rest.generator.java.generator.analysis.IImportProcessor;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;

/**
 * 抽象表示一条指令，这些指令可以是具体的一行代码，或者是
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IInstruction extends IImportProcessor {

    /**
     * 表示一条表示函数返回的指令
     */
    int INSTRUCTION_TYPE_RETURN = 1;

    /**
     * 表示一条变量或者常量复制的指令
     */
    int INSTRUCTION_TYPE_ASSIGNMENT = 2;

    /**
     * 表示方法的调用指令
     */
    int INSTRUCTION_TYPE_METHOD_INVOCATION = 3;

    /**
     * 表示这条抽象的指令表示一个程序流单位
     */
    int INSTRUCTION_TYPE_FLOW = 4;

    /**
     * 表示这是一条空白的指令，对应产生一空行
     */
    int INSTRUCTION_TYPE_EMPTY = 5;

    /**
     * 表示这是一条表示注释信息的指令
     */
    int INSTRUCTION_TYPE_COMMENT = 6;

    /**
     * 表示这是一条抛异常
     */
    int INSTRUCTION_THROW_INSTRUCTION = 7;

    /**
     * 获得这条指令的指令类型
     *
     * @return 这条抽象指令的指令类型
     */
    int getInstructionType();

    /**
     * 通过在这个指令的基础上调用方法产生一条新的指令
     *
     * @param methodName 方法名
     * @param parameters 参数值
     * @return 调用指定方法后产生的新的指令
     */
    IInstruction invoke(String methodName, ParameterValue[] parameters);

    /**
     * 通过在这个指令的基础上调用方法产生一条新的指令
     *
     * @param methodName 方法名
     * @param parameters 参数值
     * @return 调用指定方法后产生的新的指令
     */
    IInstruction invoke(String methodName, Object[] parameters);

    /**
     * 通过在这个指令的基础上调用方法产生一条新的指令
     *
     * @param methodName 方法名
     * @param parameter  参数
     * @return 调用指定方法后产生的新的指令
     */
    IInstruction invoke(String methodName, Object parameter);

    /**
     * 对部分可以转换成表达式的指令提供转换功能
     *
     * @return 这个指令转换成值表达式以后对应的对象
     */
    IValueExpression toValueExpression();
}
