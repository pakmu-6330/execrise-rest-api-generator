package cn.dyr.rest.generator.java.meta.flow.expression.constant;

/**
 * 表示一个 double 类型的常量表达式
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DoubleValueExpression extends AbstractConstantExpression {

    public DoubleValueExpression(double value) {
        super(String.format("%fd", value));
    }

}
