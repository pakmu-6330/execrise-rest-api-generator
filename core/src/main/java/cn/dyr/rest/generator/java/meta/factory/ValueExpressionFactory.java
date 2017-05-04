package cn.dyr.rest.generator.java.meta.factory;

import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.flow.expression.BinaryOperatorExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.ClassStaticValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.ConstructorInvocationValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.EmptyValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.EnumerationValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.PrefixSingleOperandOperationExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.VariableExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.constant.BooleanValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.constant.ByteValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.constant.CharacterValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.constant.DoubleValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.constant.FloatValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.constant.IntegerValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.constant.LongValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.constant.NullValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.constant.ShortValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.constant.StringValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.constant.VoidValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;

import java.util.Objects;

/**
 * 用于创建一个常用的值表达式对象的工厂
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ValueExpressionFactory {

    private static final IValueExpression NULL_VALUE_EXPRESSION = new NullValueExpression();
    private static final IValueExpression VOID_VALUE_EXPRESSION = new VoidValueExpression();
    private static final IValueExpression EMPTY_VALUE_EXPRESSION = new EmptyValueExpression();

    /**
     * 创建一个表示 null 的值表达式对象
     *
     * @return 一个表示 null 的值表达式
     */
    public static IValueExpression nullExpression() {
        return NULL_VALUE_EXPRESSION;
    }

    /**
     * 创建一个不表示任何值的表达式对象
     *
     * @return 一个不表示任何值的值表达式
     */
    public static IValueExpression voidExpression() {
        return VOID_VALUE_EXPRESSION;
    }

    /**
     * 创建一个表示 long 类型的常量表达式
     *
     * @param value 常量的值
     * @return 这个常量值对应的表达式对象
     */
    public static IValueExpression longExpression(long value) {
        return new LongValueExpression(value);
    }

    /**
     * 创建一个表示 short 类型的常量表达式
     *
     * @param value 常量的值
     * @return 这个常量值对应的表达式对象
     */
    public static IValueExpression shortExpression(short value) {
        return new ShortValueExpression(value);
    }

    /**
     * 创建一个表示 int 类型的常量表达式
     *
     * @param value 常量的值
     * @return 这个常量值对应的表达式对象
     */
    public static IValueExpression intExpression(int value) {
        return new IntegerValueExpression(value);
    }

    /**
     * 创建一个表示 byte 类型的常量表达式
     *
     * @param value 常量的值
     * @return 这个常量值对应的表达式对象
     */
    public static IValueExpression byteExpression(byte value) {
        return new ByteValueExpression(value);
    }

    /**
     * 创建一个表示 char 类型的常量表达式
     *
     * @param value 常量的值
     * @return 这个常量值对应的表达式对象
     */
    public static IValueExpression charExpression(char value) {
        return new CharacterValueExpression(value);
    }

    /**
     * 创建一个表示 boolean 类型的常量表达式
     *
     * @param value 常量的值
     * @return 这个常量值对应的表达式对象
     */
    public static IValueExpression booleanExpression(boolean value) {
        return new BooleanValueExpression(value);
    }

    /**
     * 创建一个表示 float 类型的常量表达式
     *
     * @param value 常量的值
     * @return 这个常量值对应的表达式对象
     */
    public static IValueExpression floatExpression(float value) {
        return new FloatValueExpression(value);
    }

    /**
     * 创建一个表示 double 类型的常量表达式
     *
     * @param value 常量的值
     * @return 这个常量值对应的表达式对象
     */
    public static IValueExpression doubleExpression(double value) {
        return new DoubleValueExpression(value);
    }

    /**
     * 创建一个表示 byte 类型默认值的常量表达式
     *
     * @return byte 类型和默认值的常量表达式
     */
    public static IValueExpression byteDefault() {
        return new ByteValueExpression((byte) 0);
    }

    /**
     * 创建一个 long 类型的默认值（0L）的常量表达式
     *
     * @return long 类型默认值的常量表达式
     */
    public static IValueExpression longDefault() {
        return longExpression(0);
    }

    /**
     * 创建一个 short 类型的默认值（0）表达式
     *
     * @return short 类型默认值的常量表达式
     */
    public static IValueExpression shortDefault() {
        return new ShortValueExpression((short) 0);
    }

    /**
     * 创建一个 int 类型的默认值（0）的常量表达式
     *
     * @return int 类型默认值的常量表达式
     */
    public static IValueExpression intDefault() {
        return intExpression(0);
    }

    /**
     * 创建一个 char 类型的默认值（\u0000）的常量表达式
     *
     * @return char 类型默认值的常量表达式
     */
    public static IValueExpression charDefault() {
        return new CharacterValueExpression(0);
    }

    /**
     * 创建一个 boolean 类型的默认值（false）的常量表达式
     *
     * @return boolean 类型默认值的常量表达式
     */
    public static IValueExpression booleanDefault() {
        return booleanExpression(false);
    }

    /**
     * 创建一个 float 类型的默认值（0.0f）的常量表达式
     *
     * @return float 类型默认值的常量表达式
     */
    public static IValueExpression floatDefault() {
        return new FloatValueExpression(0.0f);
    }

    /**
     * 创建一个 double 类型的默认值（0.0d）的常量表达式
     *
     * @return double 类型默认值的常量表达式
     */
    public static IValueExpression doubleDefault() {
        return new DoubleValueExpression(0.0d);
    }

    /**
     * 创建一个字符串常量的表达式
     *
     * @param value 字符串常量
     * @return 这个常量对应的表达式对象
     */
    public static IValueExpression stringExpression(String value) {
        return new StringValueExpression(value);
    }

    /**
     * 创建某个类型的默认值表达式
     *
     * @param typeInfo 类型
     * @return 这个类型的默认值表达式
     */
    public static IValueExpression typeDefaultValueExpression(TypeInfo typeInfo) {
        if (typeInfo.isArray() || !typeInfo.isPrimitiveType()) {
            return nullExpression();
        }

        String name = typeInfo.getName();
        switch (name) {
            case "byte":
                return byteDefault();
            case "short":
                return shortDefault();
            case "int":
                return intDefault();
            case "long":
                return longDefault();
            case "char":
                return charDefault();
            case "boolean":
                return booleanDefault();
            case "float":
                return floatDefault();
            case "double":
                return doubleDefault();
        }

        throw new IllegalArgumentException(String.format("default value for %s is not supported", typeInfo.getFullName()));
    }

    /**
     * 表示调用某个类构造方法返回的值
     *
     * @param typeInfo   要调用构造方法的类
     * @param parameters 构造方法的入参
     * @return 调用构造方法以后的值
     */
    public static IValueExpression invokeConstructor(TypeInfo typeInfo, ParameterValue[] parameters) {
        return new ConstructorInvocationValueExpression()
                .setTypeInfo(typeInfo)
                .setParameterValues(parameters);
    }

    /**
     * 表示调用某个类构造方法的返回值
     *
     * @param typeInfo   要调用构造方法的类
     * @param parameters 构造方法的入参
     * @return 调用构造方法以后的值
     */
    public static IValueExpression invokeConstructor(TypeInfo typeInfo, Object[] parameters) {
        ParameterValue[] parameterValues = new ParameterValue[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            parameterValues[i] = ParameterValueFactory.fromObject(parameters[i]);
        }

        return new ConstructorInvocationValueExpression()
                .setTypeInfo(typeInfo)
                .setParameterValues(parameterValues);
    }

    /**
     * 表示调用某个类构造方法的返回值
     *
     * @param typeInfo 要调用构造方法的类
     * @return 调用构造方法以后的值
     */
    public static IValueExpression invokeConstructor(TypeInfo typeInfo) {
        return new ConstructorInvocationValueExpression()
                .setTypeInfo(typeInfo);
    }

    /**
     * 创建一个表示变量的表达式对象
     *
     * @param name 变量的名称
     * @return 这个变量对应的值的对象
     */
    public static IValueExpression variable(String name) {
        VariableExpression variableExpression = new VariableExpression();
        variableExpression.setName(name)
                .setPrimitiveTypeVariable(false);

        return variableExpression;
    }

    /**
     * 创建一个用于静态调用的类表达式
     *
     * @param classTypeInfo 类
     * @return 这个类的表达式
     */
    public static IValueExpression classForStatic(TypeInfo classTypeInfo) {
        return new ClassStaticValueExpression(classTypeInfo);
    }

    /**
     * 返回当前类的引用
     *
     * @return 表示 this 引用的值表达式/
     */
    public static IValueExpression thisReference() {
        return variable("this");
    }

    /**
     * 返回一个空白的值表达式
     *
     * @return 空白值表达式
     */
    public static IValueExpression empty() {
        return EMPTY_VALUE_EXPRESSION;
    }

    /**
     * 用于检查二元运算符的左右表达式是否同时不为空
     *
     * @param left  左表达式
     * @param right 右表达式
     */
    private static void requireBothExpressionNotNull(IValueExpression left, IValueExpression right) {
        Objects.requireNonNull(left, "left expression is null");
        Objects.requireNonNull(right, "right expression is null");
    }

    /**
     * 生成一个加法表达式
     *
     * @param left  加法左边的表达式
     * @param right 加法右边的表达式
     * @return 值表达式
     */
    public static IValueExpression plus(IValueExpression left, IValueExpression right) {
        requireBothExpressionNotNull(left, right);

        return new BinaryOperatorExpression()
                .setOperator(BinaryOperatorExpression.OPERATOR_PLUS)
                .setLeftExpression(left)
                .setRightExpression(right);
    }

    /**
     * 生成一个减法表达式
     *
     * @param left  减法左边的表达式
     * @param right 减法右边的表达式
     * @return 值表达式
     */
    public static IValueExpression subtract(IValueExpression left, IValueExpression right) {
        requireBothExpressionNotNull(left, right);

        return new BinaryOperatorExpression()
                .setOperator(BinaryOperatorExpression.OPERATOR_MINUS)
                .setLeftExpression(left)
                .setRightExpression(right);
    }

    /**
     * 生成一个判断是否相等的表达式
     *
     * @param left  左表达式
     * @param right 右表达式
     * @return 值表达式
     */
    public static IValueExpression logicalEqual(IValueExpression left, IValueExpression right) {
        requireBothExpressionNotNull(left, right);

        return new BinaryOperatorExpression()
                .setOperator(BinaryOperatorExpression.OPERATOR_LOGIC_EQUALITY)
                .setLeftExpression(left)
                .setRightExpression(right);
    }

    /**
     * 生成一个判断是否不相等的表达式
     *
     * @param left  左表达式
     * @param right 右表达式
     * @return 值表达式
     */
    public static IValueExpression logicalInequal(IValueExpression left, IValueExpression right) {
        requireBothExpressionNotNull(left, right);

        return new BinaryOperatorExpression()
                .setOperator(BinaryOperatorExpression.OPERATOR_LOGIC_INEQUITY)
                .setLeftExpression(left)
                .setRightExpression(right);
    }

    /**
     * 创建两个表达式执行逻辑与以后产生的表达式
     *
     * @param left  左表达式
     * @param right 右表达式
     * @return 两个表达式逻辑与以后产生的表达式
     */
    public static IValueExpression logicalAnd(IValueExpression left, IValueExpression right) {
        requireBothExpressionNotNull(left, right);

        if (left instanceof BinaryOperatorExpression) {
            ((BinaryOperatorExpression) left).setSimpleExpression(false);
        }

        if (right instanceof BinaryOperatorExpression) {
            ((BinaryOperatorExpression) right).setSimpleExpression(false);
        }

        return new BinaryOperatorExpression()
                .setOperator(BinaryOperatorExpression.OPERATOR_LOGIC_AND)
                .setLeftExpression(left)
                .setRightExpression(right);
    }

    /**
     * 创建一个大于的表达式
     *
     * @param left  左表达式
     * @param right 右表达式
     * @return 对应的大于表达式
     */
    public static IValueExpression greaterThan(IValueExpression left, IValueExpression right) {
        if (left instanceof BinaryOperatorExpression) {
            ((BinaryOperatorExpression) left).setSimpleExpression(false);
        }

        return new BinaryOperatorExpression()
                .setOperator(BinaryOperatorExpression.OPERATOR_GREATER_THAN)
                .setLeftExpression(left)
                .setRightExpression(right);
    }

    /**
     * 创建一个逻辑非的值表达式
     *
     * @param expression 需要进行取非的表达式
     * @return 取非以后的值表达式
     */
    public static IValueExpression logicalNot(IValueExpression expression) {
        Objects.requireNonNull(expression, "expression is null");

        return new PrefixSingleOperandOperationExpression().setOperand(expression).setOperator(PrefixSingleOperandOperationExpression.LOGIC_NOT);
    }

    /**
     * 创建一个枚举的值表达式
     *
     * @param enumClass 枚举类
     * @param member    枚举的成员值
     * @return 枚举表达式
     */
    public static IValueExpression enumerationValue(TypeInfo enumClass, String member) {
        Objects.requireNonNull(enumClass, "enum type is null");
        Objects.requireNonNull(member, "member is null");

        return new EnumerationValueExpression(enumClass, member);
    }
}
