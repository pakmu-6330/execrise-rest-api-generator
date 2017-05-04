package cn.dyr.rest.generator.java.meta.flow.expression;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;

import static cn.dyr.rest.generator.util.ExpressionUtil.isComplexExpression;

/**
 * 由基本二元运算符产生的表达式对象<br>
 * 目前支持的运算符有：
 * <ul>
 * <li>+</li>
 * <li>-</li>
 * <li>*</li>
 * <li>/</li>
 * <li>==</li>
 * <li>!=</li>
 * <li>&lt;</li>
 * <li>&gt;</li>
 * </ul>
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class BinaryOperatorExpression extends AbstractExpression {

    /**
     * 加号
     */
    public static final String OPERATOR_PLUS = "+";

    /**
     * 减号
     */
    public static final String OPERATOR_MINUS = "-";

    /**
     * 乘号
     */
    public static final String OPERATOR_MULTPLY = "*";

    /**
     * 除号
     */
    public static final String OPERATOR_DIVIDE = "/";

    /**
     * 判定两值是否相等
     */
    public static final String OPERATOR_LOGIC_EQUALITY = "==";

    /**
     * 判断两值是否不等
     */
    public static final String OPERATOR_LOGIC_INEQUITY = "!=";

    /**
     * 短路逻辑或运算
     */
    public static final String OPERATOR_LOGIC_OR = "||";

    /**
     * 短路逻辑与运算
     */
    public static final String OPERATOR_LOGIC_AND = "&&";

    /**
     * 判断左值是否大于右值
     */
    public static final String OPERATOR_GREATER_THAN = ">";

    /**
     * 判断左值是否大于等于右值
     */
    public static final String OPERATOR_GREATER_THAN_OR_EQUAL = ">=";

    /**
     * 判断左值是否小于右值
     */
    public static final String OPERATOR_LESS_THAN = "<";

    /**
     * 判断左值是否小于等于右值
     */
    public static final String OPERATOR_LESS_THAN_OR_EQUAL = "<=";

    /**
     * 逻辑按位与运算
     */
    public static final String OPERATOR_BIT_AND = "&";

    /**
     * 逻辑按位或运算
     */
    public static final String OPERATOR_BIT_OR = "|";

    /**
     * 逻辑按位异或运算
     */
    public static final String OPERATOR_BIT_XOR = "^";

    /**
     * 逻辑按位左移运算
     */
    public static final String OPERATOR_BIT_LEFT_MOVE = "<<";

    /**
     * 逻辑按位右移运算
     */
    public static final String OPERATOR_BIT_RIGHT_MOVE = ">>";

    /**
     * 逻辑按位无符号右移
     */
    public static final String OPERATOR_BIT_UNSIGNED_RIGHT_MOVE = ">>>";

    private String operator;
    private IValueExpression leftExpression;
    private IValueExpression rightExpression;
    private boolean simpleExpression;

    public BinaryOperatorExpression() {
        this.simpleExpression = true;
    }

    public String getOperator() {
        return operator;
    }

    public BinaryOperatorExpression setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public IValueExpression getLeftExpression() {
        return leftExpression;
    }

    /**
     * 判断运算符左侧的表示式是否为复杂类型的表达式
     *
     * @return 表示运算符左侧表达式是否为复杂类型表达式的布尔值
     */
    public boolean isLeftExpressionComplexExpression() {
        return isComplexExpression(this.leftExpression);
    }

    public BinaryOperatorExpression setLeftExpression(IValueExpression leftExpression) {
        this.leftExpression = leftExpression;
        return this;
    }

    public IValueExpression getRightExpression() {
        return rightExpression;
    }

    /**
     * 判断运算符右侧的表达式是否为复杂类型的表达式
     *
     * @return 表示运算符右侧表达式是否为复杂类型表达式的布尔值
     */
    public boolean isRightExpressionComplexExpression() {
        return isComplexExpression(rightExpression);
    }

    public BinaryOperatorExpression setRightExpression(IValueExpression rightExpression) {
        this.rightExpression = rightExpression;
        return this;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        if (this.leftExpression != null) {
            this.leftExpression.fillImportOperations(context);
        }

        if (this.rightExpression != null) {
            this.rightExpression.fillImportOperations(context);
        }
    }

    @Override
    public boolean isSimpleExpression() {
        return this.simpleExpression;
    }

    public BinaryOperatorExpression setSimpleExpression(boolean simpleExpression) {
        this.simpleExpression = simpleExpression;
        return this;
    }
}
