package cn.dyr.rest.generator.java.meta.flow.expression.constant;

/**
 * 表示一个 float 类型的常量表达式
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class FloatValueExpression extends AbstractConstantExpression {

    public FloatValueExpression(float value) {
        super(String.format("%ff", value));
    }

}
