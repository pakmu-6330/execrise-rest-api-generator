package cn.dyr.rest.generator.java.meta.flow.expression.constant;

import cn.dyr.rest.generator.java.meta.flow.expression.AbstractExpression;

/**
 * 表示字符串类型的常量
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class StringValueExpression extends AbstractExpression {

    private String value;

    public StringValueExpression(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public StringValueExpression setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return String.format("\"%s\"", this.value);
    }
}
