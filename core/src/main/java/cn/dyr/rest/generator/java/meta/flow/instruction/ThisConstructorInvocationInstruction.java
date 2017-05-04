package cn.dyr.rest.generator.java.meta.flow.instruction;

import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;

/**
 * 表示当前这个类的构造方法的调用
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ThisConstructorInvocationInstruction extends SpecialMethodInvocationInstruction {
    @Override
    public int getSpecialMethodType() {
        return SPECIAL_METHOD_TYPE_THIS_CONSTRUCTOR;
    }

    /**
     * 获得方法名，因为调用本类的构造方法，所以这个方法的返回值固定为 this1
     *
     * @return this
     */
    @Override
    public String getMethodName() {
        return "this";
    }

    @Override
    public MethodInvocationInstruction setMethodName(String methodName) {
        throw new IllegalStateException("setMethodName method invocation is not allowed on this constructor invocation instruction");
    }

    @Override
    public IInstruction invoke(String methodName, ParameterValue[] parameters) {
        throw new IllegalStateException("method invocation is not allowed in this class constructor invocation instruction");
    }

    @Override
    public IInstruction invoke(String methodName, Object[] parameters) {
        throw new IllegalStateException("method invocation is not allowed in this class constructor invocation instruction");
    }

    @Override
    public IValueExpression toValueExpression() {
        throw new IllegalStateException("this constructor invocation instruction cannot be converted to value expression");
    }
}
