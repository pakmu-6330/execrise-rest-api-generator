package cn.dyr.rest.generator.java.meta.flow.expression;

import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;

/**
 * 这个类表示一个 null 的表达式
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class NullValueExpression extends VariableExpression {

    /**
     * 用于抛出不支持操作的异常
     *
     * @param desc 异常的描述字符串
     */
    protected void throwUnsupportedOperation(String desc) {
        throw new UnsupportedOperationException(desc);
    }

    public NullValueExpression() {
        super.setName("null");
    }

    @Override
    public boolean isPrimitiveTypeVariable() {
        return true;
    }

    @Override
    public VariableExpression setPrimitiveTypeVariable(boolean primitiveTypeVariable) {
        return this;
    }

    @Override
    public String getName() {
        return "null";
    }

    @Override
    public VariableExpression setName(String name) {
        throwUnsupportedOperation("set name");
        return this;
    }

    @Override
    public IValueExpression invokeMethod(String name) {
        throwUnsupportedOperation("method invocation");
        return this;
    }

    @Override
    public IValueExpression invokeMethod(String name, ParameterValue[] parameters) {
        throwUnsupportedOperation("method invocation");
        return this;
    }

    @Override
    public IValueExpression invokeMethod(String name, Object[] parameters) {
        throwUnsupportedOperation("method invocation");
        return this;
    }

    @Override
    public IValueExpression accessField(String name) {
        throwUnsupportedOperation("field access");
        return this;
    }
}
