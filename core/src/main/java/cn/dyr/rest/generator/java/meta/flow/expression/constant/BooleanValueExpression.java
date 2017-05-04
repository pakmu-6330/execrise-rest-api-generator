package cn.dyr.rest.generator.java.meta.flow.expression.constant;

/**
 * 表示一个 boolean 类型的常量表达式
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class BooleanValueExpression extends AbstractConstantExpression {
    public BooleanValueExpression(boolean value) {
        super(value ? "true" : "false");
    }
}
