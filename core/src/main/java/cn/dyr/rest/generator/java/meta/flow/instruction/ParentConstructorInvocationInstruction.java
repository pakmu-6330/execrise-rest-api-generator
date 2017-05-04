package cn.dyr.rest.generator.java.meta.flow.instruction;

import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;

/**
 * 表示父类构造方法的调用
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ParentConstructorInvocationInstruction extends SpecialMethodInvocationInstruction {
    @Override
    public int getSpecialMethodType() {
        return SPECIAL_METHOD_TYPE_PARENT_CONSTRUCTOR;
    }

    /**
     * 获得方法名，因为本类表示父类构造方法的调用，所以这个方法的返回值固定为 super
     *
     * @return super
     */
    @Override
    public String getMethodName() {
        return "super";
    }

    /**
     * 设置方法名，因为本类表示父类构造方法的调用，所以调用这个方法会抛出 IllegalStateException 异常
     *
     * @param methodName 方法名
     * @return 指令本身
     * @throws IllegalStateException 调用必抛
     */
    @Override
    public MethodInvocationInstruction setMethodName(String methodName) {
        throw new IllegalStateException("setMethodName method invocation is not allowed on parent constructor invocation instruction");
    }

    @Override
    public IInstruction invoke(String methodName, ParameterValue[] parameters) {
        throw new IllegalStateException("method invocation is not allowed in parent constructor invocation instruction");
    }

    @Override
    public IInstruction invoke(String methodName, Object[] parameters) {
        throw new IllegalStateException("method invocation is not allowed in parent constructor invocation instruction");
    }

    @Override
    public IValueExpression toValueExpression() {
        throw new IllegalStateException("parent constructor invocation instruction cannot be converted to value expression");
    }
}
