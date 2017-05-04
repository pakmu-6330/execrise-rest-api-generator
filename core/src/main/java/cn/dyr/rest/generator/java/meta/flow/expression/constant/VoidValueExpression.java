package cn.dyr.rest.generator.java.meta.flow.expression.constant;

import cn.dyr.rest.generator.exception.InvocationNotSupportException;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;

/**
 * 用于不返回任何值的值表达式
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class VoidValueExpression extends AbstractConstantExpression {

    public VoidValueExpression() {
        super("void");
    }
}
