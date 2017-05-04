package cn.dyr.rest.generator.java.meta.flow.expression.constant;

import cn.dyr.rest.generator.exception.InvocationNotSupportException;
import cn.dyr.rest.generator.java.meta.flow.expression.AbstractExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;

/**
 * 表示常量的值类型
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public abstract class AbstractConstantExpression extends AbstractExpression {

    private String value;

    public AbstractConstantExpression(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public IValueExpression invokeMethod(String name, ParameterValue[] parameters) {
        throw new InvocationNotSupportException("method invocation is not supported");
    }

    @Override
    public IValueExpression invokeMethod(String name, Object[] parameters) {
        return super.invokeMethod(name, parameters);
    }

    @Override
    public IValueExpression accessField(String name) {
        return null;
    }
}
