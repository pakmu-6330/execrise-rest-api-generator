package cn.dyr.rest.generator.java.meta.flow.expression;

/**
 * 表示一个运算符在后的一元运算的值
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SuffixSingleOperandOperationExpression extends AbstractExpression {

    /**
     * 表示自加运算
     */
    public static final String SELF_PLUS = "++";

    /**
     * 表示自减运算
     */
    public static final String SELF_MINUS = "--";

    private String operator;
    private IValueExpression operand;

    public String getOperator() {
        return operator;
    }

    public SuffixSingleOperandOperationExpression setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public IValueExpression getOperand() {
        return operand;
    }

    public SuffixSingleOperandOperationExpression setOperand(IValueExpression operand) {
        this.operand = operand;
        return this;
    }
}
