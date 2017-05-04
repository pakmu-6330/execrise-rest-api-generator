package cn.dyr.rest.generator.java.meta.flow.instruction;

import cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;

/**
 * 表示一些特殊函数的调用，例如父类构造方法，本类构造方法等
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public abstract class SpecialMethodInvocationInstruction extends MethodInvocationInstruction {

    /**
     * 表示这个函数调用调用的是父类的构造方法
     */
    public static final int SPECIAL_METHOD_TYPE_PARENT_CONSTRUCTOR = 1;

    /**
     * 表示这个构造函数调用的是本类的构造方法
     */
    public static final int SPECIAL_METHOD_TYPE_THIS_CONSTRUCTOR = 2;

    /**
     * 表示函数调用指令调用的是静态函数
     */
    public static final int SPECIAL_METHOD_TYPE_STATIC_METHOD = 3;

    /**
     * 因为特殊的函数调用不需要从某个特定对象或者表达式引出，这个方法恒返回空表达式
     *
     * @return 空表达式
     */
    @Override
    public IValueExpression getValue() {
        return ValueExpressionFactory.empty();
    }

    /**
     * 特殊函数调用的引出值固定为空表达式，调用这个方法会抛出 IllegalStateException 异常
     *
     * @param value 要设置的值
     * @return 这个值表达式对象本身
     * @throws IllegalStateException 调用必抛
     */
    @Override
    public MethodInvocationInstruction setValue(IValueExpression value) {
        throw new IllegalStateException("setValue method invocation is not allowed on special method invocation");
    }

    /**
     * 获得一个表示这个特殊函数是什么函数的值
     *
     * @return 表示这个特殊函数类型的值
     */
    public abstract int getSpecialMethodType();
}
