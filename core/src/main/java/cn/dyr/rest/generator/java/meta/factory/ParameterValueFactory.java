package cn.dyr.rest.generator.java.meta.factory;

import cn.dyr.rest.generator.java.generator.elements.ValueExpressionToken;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.flow.expression.ChainValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.value.BooleanParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.ByteParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.CharacterParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.ClassParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.DoubleParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.FloatParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.IntegerParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.LongParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.NullParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.ShortParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.StringParameterValue;
import cn.dyr.rest.generator.java.meta.parameters.value.ValueExpressionParameterValue;

/**
 * 用于创建实参对象的工厂类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ParameterValueFactory {

    /**
     * 根据参数值创建一个参数对象
     *
     * @param value 参数值
     * @return 如果参数值能够被转换成一个对象，则返回相应的参数对象；否则抛出 IllegalArgumentException 异常
     */
    public static ParameterValue fromObject(Object value) {
        if (value == null) {
            return new NullParameterValue();
        }

        Class<?> valueClass = value.getClass();

        if (valueClass == String.class) {
            String strValue = (String) value;
            return new StringParameterValue(strValue);
        } else if (valueClass == Integer.class) {
            int intValue = (int) value;
            return new IntegerParameterValue(intValue);
        } else if (valueClass == Long.class) {
            long longValue = (long) value;
            return new LongParameterValue(longValue);
        } else if (valueClass == Double.class) {
            double doubleValue = (double) value;
            return new DoubleParameterValue(doubleValue);
        } else if (valueClass == Boolean.class) {
            boolean booleanValue = (boolean) value;
            return new BooleanParameterValue(booleanValue);
        } else if (valueClass == Float.class) {
            float floatValue = (float) value;
            return new FloatParameterValue(floatValue);
        } else if (valueClass == Character.class) {
            char charValue = (char) value;
            return new CharacterParameterValue(charValue);
        } else if (valueClass == Byte.class) {
            byte byteValue = (byte) value;
            return new ByteParameterValue(byteValue);
        } else if (valueClass == Short.class) {
            short shortValue = (short) value;
            return new ShortParameterValue(shortValue);
        } else if (valueClass == Class.class) {
            TypeInfo typeInfo = TypeInfoFactory.fromClass(((Class) value).getName());
            return new ClassParameterValue(typeInfo);
        } else if (value instanceof TypeInfo) {
            TypeInfo typeValue = (TypeInfo) value;
            return new ClassParameterValue(typeValue);
        } else if (value instanceof ClassInfo) {
            return new ClassParameterValue(((ClassInfo) value).getType());
        } else if (value instanceof IValueExpression) {
            return new ValueExpressionParameterValue((IValueExpression) value);
        }

        throw new IllegalArgumentException(
                String.format("unsupported parameter value type: %s", valueClass.getName()));
    }

    /**
     * 将一个 Object 数组根据具体的类型转换为 ParameterValue 对象
     *
     * @param values 要进行转换的对象
     * @return 根据这个对象转换而来的 ParameterValue 对象
     */
    public static ParameterValue[] fromObjects(Object[] values) {
        if (values == null) {
            throw new NullPointerException("values is null");
        }

        ParameterValue[] retValue = new ParameterValue[values.length];
        for (int i = 0; i < values.length; i++) {
            retValue[i] = fromObject(values[i]);
        }

        return retValue;
    }

    /**
     * 创建一个 long 类型的实参对象
     *
     * @param value 实参值
     * @return 这个实参值对应的实参对象
     */
    public static ParameterValue createLongParameterValue(long value) {
        return new LongParameterValue(value);
    }

    /**
     * 创建一个字符串类型的实参对象
     *
     * @param value 字符串型的实参值
     * @return 这个实参值对应的实参对象
     */
    public static ParameterValue createStringParameterValue(String value) {
        return new StringParameterValue(value);
    }
}