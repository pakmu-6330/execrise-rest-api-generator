package cn.dyr.rest.generator.java.meta.flow.expression;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;

import static cn.dyr.rest.generator.util.ExpressionUtil.isComplexExpression;

/**
 * 表示一个运算符号在前的一元运算的值
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class PrefixSingleOperandOperationExpression extends AbstractExpression {

    /**
     * 表示布尔值取反的操作
     */
    public static final String LOGIC_NOT = "!";

    /**
     * 表示按位取非的操作
     */
    public static final String BIT_NOT = "~";

    private String operator;
    private IValueExpression operand;

    public String getOperator() {
        return operator;
    }

    public PrefixSingleOperandOperationExpression setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public IValueExpression getOperand() {
        return operand;
    }

    public PrefixSingleOperandOperationExpression setOperand(IValueExpression operand) {
        this.operand = operand;
        return this;
    }

    public boolean isComplex() {
        return isComplexExpression(this);
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        if (this.operand != null) {
            this.operand.fillImportOperations(context);
        }
    }
}
