package cn.dyr.rest.generator.java.meta.flow.expression;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;

/**
 * 三目运算符表达式
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class TernaryValueExpression extends AbstractExpression {

    private IValueExpression condition;
    private IValueExpression yesValueExpression;
    private IValueExpression noValueExpression;

    public IValueExpression getCondition() {
        return condition;
    }

    public TernaryValueExpression setCondition(IValueExpression condition) {
        this.condition = condition;
        return this;
    }

    public IValueExpression getYesValueExpression() {
        return yesValueExpression;
    }

    public TernaryValueExpression setYesValueExpression(IValueExpression yesValueExpression) {
        this.yesValueExpression = yesValueExpression;
        return this;
    }

    public IValueExpression getNoValueExpression() {
        return noValueExpression;
    }

    public TernaryValueExpression setNoValueExpression(IValueExpression noValueExpression) {
        this.noValueExpression = noValueExpression;
        return this;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        super.fillImportOperations(context);

        if (condition != null) {
            condition.fillImportOperations(context);
        }

        if (yesValueExpression != null) {
            yesValueExpression.fillImportOperations(context);
        }

        if (noValueExpression != null) {
            noValueExpression.fillImportOperations(context);
        }
    }
}
