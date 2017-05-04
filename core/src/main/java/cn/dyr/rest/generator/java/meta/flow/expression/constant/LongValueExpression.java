package cn.dyr.rest.generator.java.meta.flow.expression.constant;

/**
 * 一个表示 long 类型的常量
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class LongValueExpression extends AbstractConstantExpression {

    public LongValueExpression(long value) {
        super(String.format("%sL", value));
    }
}
