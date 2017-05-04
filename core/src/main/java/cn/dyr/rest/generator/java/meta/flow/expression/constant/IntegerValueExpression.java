package cn.dyr.rest.generator.java.meta.flow.expression.constant;

/**
 * 表示一个 int 类型的常量表达式
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class IntegerValueExpression extends AbstractConstantExpression {

    public IntegerValueExpression(int value) {
        super(String.format("%d", value));
    }

}
