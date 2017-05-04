package cn.dyr.rest.generator.java.generator.elements;

import cn.dyr.rest.generator.java.meta.parameters.value.BooleanParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.ByteParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.CharacterParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.ClassParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.DoubleParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.FloatParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.IntegerParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.LongParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.ShortParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.StringParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.ValueExpressionParameterValue;

/**
 * 表示表达式当中其中一个参数或者常量的元素
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ParameterValueToken implements IToken {

    private ParameterValue value;

    public ParameterValueToken(ParameterValue value) {
        this.value = value;
    }

    public ParameterValue getValue() {
        return value;
    }

    public ParameterValueToken setValue(ParameterValue value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        int type = this.value.getType();

        switch (type) {
            case ParameterValue.TYPE_PRIMITIVE_SHORT:
                return String.format("%d", ((ShortParameterValue) this.value).getValue());
            case ParameterValue.TYPE_PRIMITIVE_BYTE:
                return String.format("%d", ((ByteParameterValue) this.value).getValue());
            case ParameterValue.TYPE_PRIMITIVE_INT:
                return String.format("%d", ((IntegerParameterValue) this.value).getValue());
            case ParameterValue.TYPE_PRIMITIVE_LONG:
                return String.format("%dL", ((LongParameterValue) this.value).getValue());
            case ParameterValue.TYPE_PRIMITIVE_FLOAT:
                return String.format("%sf", String.valueOf(((FloatParameterValue) this.value).getValue()));
            case ParameterValue.TYPE_PRIMITIVE_DOUBLE:
                return String.format("%f", ((DoubleParameterValue) this.value).getValue());
            case ParameterValue.TYPE_PRIMITIVE_BOOLEAN:
                return ((BooleanParameterValue) this.value).isValue() ? "true" : "false";
            case ParameterValue.TYPE_PRIMITIVE_CHAR:
                return String.format("'%c'", ((CharacterParameterValue) this.value).getValue());
            case ParameterValue.TYPE_PRIMITIVE_CLASS:
                TypeInfoToken token = new TypeInfoToken(((ClassParameterValue) this.value).getValue());
                return String.format("%s.class", token.toString());
            case ParameterValue.TYPE_OBJECT_STRING:
                return String.format("\"%s\"", ((StringParameterValue) this.value).getValue());
            case ParameterValue.TYPE_OBJECT_NULL:
                return "null";
            case ParameterValue.TYPE_VALUE_EXPRESSION:
                ValueExpressionToken valueExpressionToken = new ValueExpressionToken(((ValueExpressionParameterValue) this.value).getValueExpression());
                return valueExpressionToken.toString();
            default:
                throw new IllegalArgumentException(String.format("unknown parameter type: %d", type));
        }
    }
}
