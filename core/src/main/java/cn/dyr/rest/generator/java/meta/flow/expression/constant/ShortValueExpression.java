package cn.dyr.rest.generator.java.meta.flow.expression.constant;

/**
 * 表示一个 short 类型的常量表达式
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ShortValueExpression extends AbstractConstantExpression {

    public ShortValueExpression(short value) {
        super(String.format("%s", value));
    }

}
