package cn.dyr.rest.generator.java.meta.parameters.annotation;

import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;

/**
 * 注解参数工厂
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class AnnotationParameterFactory {

    /**
     * 创建一个 byte 类型的注解参数对象
     *
     * @param value 参数值
     * @return 对应的注解参数对象
     */
    public static AnnotationParameter byteParameter(byte value) {
        return new PrimitiveByteAnnotationParameter(value);
    }

    /**
     * 创建一个 short 类型的注解参数对象
     *
     * @param value 参数值
     * @return 对应的注解参数对象
     */
    public static AnnotationParameter shortParameter(short value) {
        return new PrimitiveShortAnnotationParameter(value);
    }

    /**
     * 创建一个 int 类型的注解参数对象
     *
     * @param value 参数值
     * @return 对应的注解参数对象
     */
    public static AnnotationParameter intParameter(int value) {
        return new PrimitiveIntegerAnnotationParameter(value);
    }

    /**
     * 创建一个 long 类型的注解参数对象
     *
     * @param value 参数值
     * @return 对应的注解参数对象
     */
    public static AnnotationParameter longParameter(long value) {
        return new PrimitiveLongAnnotationParameter(value);
    }

    /**
     * 创建一个 float 类型的注解参数对象
     *
     * @param value 参数值
     * @return 对应的注解参数对象
     */
    public static AnnotationParameter floatParameter(float value) {
        return new PrimitiveFloatAnnotationParameter(value);
    }

    /**
     * 创建一个 double 类型的注解参数对象
     *
     * @param value 参数值
     * @return 对应的注解参数对象
     */
    public static AnnotationParameter doubleParameter(double value) {
        return new PrimitiveDoubleAnnotationParameter(value);
    }

    /**
     * 创建一个 boolean 类型的注解参数对象
     *
     * @param value 参数值
     * @return 对应的注解参数对象
     */
    public static AnnotationParameter booleanParameter(boolean value) {
        return new PrimitiveBooleanAnnotationParameter(value);
    }

    /**
     * 创建一个 char 类型的注解参数对象
     *
     * @param value 参数值
     * @return 对应的注解参数对象
     */
    public static AnnotationParameter charParameter(char value) {
        return new PrimitiveCharacterAnnotationParameter(value);
    }

    /**
     * 创建一个枚举成员类型的注解参数对象
     *
     * @param enumInfo 枚举类
     * @param constant 成员名
     * @return 对应的注解参数对象
     */
    public static AnnotationParameter enumerationParameter(TypeInfo enumInfo, String constant) {
        return new EnumAnnotationParameter().setEnumType(enumInfo).setValue(constant);
    }

    /**
     * 创建一个枚举数组的注解参数对象
     *
     * @param enumInfo 枚举类
     * @param args     成员名称数组
     * @return 对应的注解参数对象
     */
    public static AnnotationParameter enumerationArrayParameter(TypeInfo enumInfo, String[] args) {
        return new EnumArrayAnnotationParameter().setEnumType(enumInfo).setValue(args);
    }

    /**
     * 创建一个字符串注解参数
     *
     * @param value 参数值
     * @return 对应的注解参数对象
     */
    public static AnnotationParameter stringParameter(String value) {
        return new StringAnnotationParameter(value);
    }

    /**
     * 创建一个 class 对象参数，如果传入的 TypeInfo 对象表示基本数据类型，则会抛出 IllegalArgumentException 异常
     *
     * @param typeInfo class 对象所属类
     * @return 这个 class 类对应的注解参数对象
     */
    public static AnnotationParameter classParameter(TypeInfo typeInfo) {
        return new ClassAnnotationParameter(typeInfo);
    }

    /**
     * 创建一个 class 对象参数
     *
     * @param clazz class 对象
     * @return 这个 class 类对应的注解参数对象
     */
    public static AnnotationParameter classParameter(Class<?> clazz) {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(clazz.getName());
        return new ClassAnnotationParameter(typeInfo);
    }

    /**
     * 创建一个注解类型的注解参数
     *
     * @param value 注解信息
     * @return 使用这个注解作为参数值的注解参数
     */
    public static AnnotationParameter annotationParameter(AnnotationInfo value) {
        return new AnnotationAnnotationParameter(value);
    }

    /**
     * 创建一个注解数组类型的注解参数
     *
     * @param values 注解信息集合
     * @return 使用这个注解数组作为参数值的注解参数
     */
    public static AnnotationParameter annotationArray(AnnotationInfo... values) {
        return new AnnotationArrayAnnotationParameter(values);
    }

    /**
     * 创建一个表达式值的注解参数
     *
     * @param expression 表达式
     * @return 使用这个表达式作为参数值的注解参数
     */
    public static AnnotationParameter valueExpression(IValueExpression expression) {
        return new ValueExpressionAnnotationParameter(expression);
    }

    /**
     * 创建一个表达式值数组的注解参数
     *
     * @param expressions 表达式数组
     * @return 使用这个表达式数组作为参数值的注解参数l
     */
    public static AnnotationParameter valueExpressions(IValueExpression... expressions) {
        return new ValueExpressionArrayAnnotationParameter(expressions);
    }

    /**
     * 根据实际传入的对象创建相应的注解参数
     *
     * @param parameter 参数对象
     * @return 对应的注解参数对象
     */
    public static AnnotationParameter from(Object parameter) {
        if (parameter.getClass() == String.class) {
            return stringParameter((String) parameter);
        } else if (parameter.getClass() == Integer.class) {
            return intParameter((Integer) parameter);
        } else if (parameter.getClass() == Long.class) {
            return longParameter((Long) parameter);
        } else if (parameter.getClass() == Boolean.class) {
            return booleanParameter((Boolean) parameter);
        } else if (parameter.getClass() == Float.class) {
            return floatParameter((Float) parameter);
        } else if (parameter.getClass() == Double.class) {
            return doubleParameter((Double) parameter);
        } else if (parameter.getClass() == Byte.class) {
            return byteParameter((Byte) parameter);
        } else if (parameter.getClass() == Short.class) {
            return shortParameter((Short) parameter);
        } else if (parameter.getClass() == Double.class) {
            return doubleParameter((Double) parameter);
        } else if (parameter.getClass() == Character.class) {
            return charParameter((Character) parameter);
        } else if (parameter.getClass() == Class.class) {
            return classParameter((Class<?>) parameter);
        } else if (parameter instanceof TypeInfo) {
            return classParameter((TypeInfo) parameter);
        } else if (parameter instanceof IValueExpression) {
            return valueExpression((IValueExpression) parameter);
        }

        throw new IllegalArgumentException(
                String.format("%s is not supported by annotation parameter factory",
                        parameter.getClass().getName()));
    }
}
