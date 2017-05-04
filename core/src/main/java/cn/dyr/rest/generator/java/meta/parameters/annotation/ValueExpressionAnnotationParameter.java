package cn.dyr.rest.generator.java.meta.parameters.annotation;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;

/**
 * 利用值表达式作为注解参数对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ValueExpressionAnnotationParameter extends AbstractAnnotationParameter {

    private IValueExpression valueExpression;

    public ValueExpressionAnnotationParameter(IValueExpression valueExpression) {
        this.valueExpression = valueExpression;
    }

    public IValueExpression getValueExpression() {
        return valueExpression;
    }

    public ValueExpressionAnnotationParameter setValueExpression(IValueExpression valueExpression) {
        this.valueExpression = valueExpression;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_VALUE_EXPRESSION;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        if (this.valueExpression != null) {
            this.valueExpression.fillImportOperations(context);
        }
    }
}
