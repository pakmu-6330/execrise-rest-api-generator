package cn.dyr.rest.generator.java.meta.flow.expression.constant;

/**
 * 表示一个 byte 类型的常量表达式
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ByteValueExpression extends AbstractConstantExpression {

    public ByteValueExpression(byte value) {
        super(String.format("%d", value));
    }

}
