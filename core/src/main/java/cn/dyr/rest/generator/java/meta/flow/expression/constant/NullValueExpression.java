package cn.dyr.rest.generator.java.meta.flow.expression.constant;

import cn.dyr.rest.generator.exception.InvocationNotSupportException;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;

/**
 * 用于返回 null 时的值表达式
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class NullValueExpression extends AbstractConstantExpression {

    public NullValueExpression() {
        super("null");
    }
}
