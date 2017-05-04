package cn.dyr.rest.generator.java.meta.parameters.annotation;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;

/**
 * 利用值表达式数组用作注解参数的对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ValueExpressionArrayAnnotationParameter extends AbstractAnnotationParameter {

    private IValueExpression[] valueExpressions;

    public ValueExpressionArrayAnnotationParameter(IValueExpression[] valueExpressions) {
        this.valueExpressions = valueExpressions;
    }

    public IValueExpression[] getValueExpressions() {
        return valueExpressions;
    }

    public ValueExpressionArrayAnnotationParameter setValueExpressions(IValueExpression[] valueExpressions) {
        this.valueExpressions = valueExpressions;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_ARRAY_VALUE_EXPRESSION;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        if (this.valueExpressions != null) {
            for (IValueExpression expression : this.valueExpressions) {
                expression.fillImportOperations(context);
            }
        }
    }
}
