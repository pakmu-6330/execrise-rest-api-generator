package cn.dyr.rest.generator.java.meta.parameters.value;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;

/**
 * 表示一个函数的调用链的返回值作为参数值
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ValueExpressionParameterValue implements ParameterValue {

    private IValueExpression valueExpression;

    public IValueExpression getValueExpression() {
        return valueExpression;
    }

    public ValueExpressionParameterValue setValueExpression(IValueExpression valueExpression) {
        this.valueExpression = valueExpression;
        return this;
    }

    public ValueExpressionParameterValue(IValueExpression valueExpression) {
        this.valueExpression = valueExpression;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        if (valueExpression != null) {
            valueExpression.fillImportOperations(context);
        }
    }

    @Override
    public int getType() {
        return TYPE_VALUE_EXPRESSION;
    }
}
