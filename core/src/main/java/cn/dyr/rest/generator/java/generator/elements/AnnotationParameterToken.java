package cn.dyr.rest.generator.java.generator.elements;

import cn.dyr.rest.generator.java.generator.IndentManager;
import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.annotation.AnnotationAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.AnnotationArrayAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.AnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.ByteArrayAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.CharacterArrayAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.ClassAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.ClassArrayAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.DoubleArrayAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.EnumAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.EnumArrayAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.FloatArrayAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.IntegerArrayAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.LongArrayAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.PrimitiveBooleanAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.PrimitiveByteAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.PrimitiveCharacterAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.PrimitiveDoubleAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.PrimitiveFloatAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.PrimitiveIntegerAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.PrimitiveLongAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.PrimitiveShortAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.ShortArrayAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.StringAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.ValueExpressionAnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.ValueExpressionArrayAnnotationParameter;
import cn.dyr.rest.generator.util.ArrayStringBuilder;

/**
 * 表示一个注解的参数的语法标识
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class AnnotationParameterToken implements IToken {

    /**
     * 当遇到多长的注解时进行换行
     */
    private static final int NEW_LINE_LENGTH = 50;

    private AnnotationParameter parameter;

    /**
     * 根据注解参数对象创建一个对应的语法标识符
     *
     * @param parameter 注解参数
     */
    public AnnotationParameterToken(AnnotationParameter parameter) {
        this.parameter = parameter;
    }

    /**
     * 获得这个语法标识含有对应的参数信息
     *
     * @return 参数信息
     */
    public AnnotationParameter getParameter() {
        return parameter;
    }

    /**
     * 为当前标识设置参数信息
     *
     * @param parameter 要设置的参数信息
     * @return 这个标识本身
     */
    public AnnotationParameterToken setParameter(AnnotationParameter parameter) {
        this.parameter = parameter;
        return this;
    }

    /**
     * 将枚举的参数转换成相应的字符串
     *
     * @param enumTypeInfo 枚举类
     * @param value        成员
     * @return 这个枚举成员对应的字符串
     */
    private String fromEnumParameter(TypeInfo enumTypeInfo, String value) {
        TypeInfoToken typeInfoToken = new TypeInfoToken(enumTypeInfo);

        return String.format("%s.%s", typeInfoToken.toString(), value);
    }

    /**
     * 将类的参数转换为相应的字符串
     *
     * @param classInfo 类
     * @return 这个类参数对应的字符串
     */
    private String fromClassParameter(TypeInfo classInfo) {
        TypeInfoToken token = new TypeInfoToken(classInfo);

        return String.format("%s.class", token.toString());
    }

    @Override
    public String toString() {
        IndentManager indentManager = IndentManager.get();

        AnnotationParameter rawParameter = this.parameter;
        Class<?> parameterClass = rawParameter.getClass();

        ArrayStringBuilder builder = new ArrayStringBuilder();

        if (parameterClass == AnnotationAnnotationParameter.class) {
            // 注解类型的注解参数
            AnnotationInfo value = ((AnnotationAnnotationParameter) rawParameter).getValue();
            if (value == null) {
                return "null";
            }

            AnnotationToken token = new AnnotationToken(value);
            return token.toString();
        } else if (parameterClass == PrimitiveBooleanAnnotationParameter.class) {
            boolean value = ((PrimitiveBooleanAnnotationParameter) rawParameter).isValue();

            return value ? "true" : "false";
        } else if (parameterClass == PrimitiveByteAnnotationParameter.class) {
            byte value = ((PrimitiveByteAnnotationParameter) rawParameter).getValue();

            return "" + value;
        } else if (parameterClass == PrimitiveCharacterAnnotationParameter.class) {
            char value = ((PrimitiveCharacterAnnotationParameter) rawParameter).getValue();

            return "'" + value + "'";
        } else if (parameterClass == PrimitiveDoubleAnnotationParameter.class) {
            double value = ((PrimitiveDoubleAnnotationParameter) rawParameter).getValue();

            return "" + value;
        } else if (parameterClass == PrimitiveFloatAnnotationParameter.class) {
            float value = ((PrimitiveFloatAnnotationParameter) rawParameter).getValue();

            return "" + value;
        } else if (parameterClass == PrimitiveIntegerAnnotationParameter.class) {
            int value = ((PrimitiveIntegerAnnotationParameter) rawParameter).getValue();

            return "" + value;
        } else if (parameterClass == PrimitiveShortAnnotationParameter.class) {
            short value = ((PrimitiveShortAnnotationParameter) rawParameter).getValue();

            return "" + value;
        } else if (parameterClass == PrimitiveLongAnnotationParameter.class) {
            long value = ((PrimitiveLongAnnotationParameter) rawParameter).getValue();

            return "" + value;
        } else if (parameterClass == StringAnnotationParameter.class) {
            String value = ((StringAnnotationParameter) rawParameter).getValue();

            if (value == null) {
                return "null";
            } else {
                return String.format("\"%s\"", value);
            }
        } else if (parameterClass == ClassAnnotationParameter.class) {
            TypeInfo typeInfo = ((ClassAnnotationParameter) rawParameter).getTypeInfo();
            return fromClassParameter(typeInfo);
        } else if (parameterClass == EnumAnnotationParameter.class) {
            EnumAnnotationParameter enumAnnotationParameter = (EnumAnnotationParameter) rawParameter;
            return fromEnumParameter(enumAnnotationParameter.getEnumType(), enumAnnotationParameter.getValue());
        } else if (parameterClass == AnnotationArrayAnnotationParameter.class) {
            AnnotationInfo[] annotationInfoList = ((AnnotationArrayAnnotationParameter) rawParameter).getValue();

            if (annotationInfoList.length == 0) {
                return ElementsConstant.EMPTY_ARRAY;
            }

            for (AnnotationInfo annotationInfo : annotationInfoList) {
                String tokenString = new AnnotationToken(annotationInfo).toString();
                StringBuilder targetToken = new StringBuilder();

                if (tokenString.length() > NEW_LINE_LENGTH) {
                    targetToken.append(System.lineSeparator());
                    targetToken.append(indentManager.getIndentString());

                    indentManager.indent();
                    indentManager.indent();

                    targetToken.append(indentManager.getIndentString());
                    targetToken.append(tokenString);

                    indentManager.delIndent();
                    indentManager.delIndent();
                } else {
                    targetToken.append(tokenString);
                }

                builder.addRawString(targetToken.toString());
            }

            return builder.toString();
        } else if (parameterClass == ByteArrayAnnotationParameter.class) {
            byte[] value = ((ByteArrayAnnotationParameter) rawParameter).getValue();

            if (value != null && value.length > 0) {
                for (byte b : value) {
                    builder.addByte(b);
                }
            } else {
                return ElementsConstant.EMPTY_ARRAY;
            }

            return builder.toString();
        } else if (parameterClass == CharacterArrayAnnotationParameter.class) {
            char[] value = ((CharacterArrayAnnotationParameter) rawParameter).getValue();

            if (value != null && value.length > 0) {
                for (char c : value) {
                    builder.addCharacter(c);
                }
            } else {
                return ElementsConstant.EMPTY_ARRAY;
            }

            return builder.toString();
        } else if (parameterClass == ShortArrayAnnotationParameter.class) {
            short[] value = ((ShortArrayAnnotationParameter) rawParameter).getValue();

            if (value != null && value.length > 0) {
                for (short s : value) {
                    builder.addShort(s);
                }
            } else {
                return ElementsConstant.EMPTY_ARRAY;
            }

            return builder.toString();
        } else if (parameterClass == IntegerArrayAnnotationParameter.class) {
            int[] value = ((IntegerArrayAnnotationParameter) rawParameter).getValue();

            if (value != null && value.length > 0) {
                for (int i : value) {
                    builder.addInt(i);
                }
            } else {
                return ElementsConstant.EMPTY_ARRAY;
            }

            return builder.toString();
        } else if (parameterClass == LongArrayAnnotationParameter.class) {
            long[] value = ((LongArrayAnnotationParameter) rawParameter).getValue();

            if (value != null && value.length > 0) {
                for (long l : value) {
                    builder.addLong(l);
                }
            } else {
                return ElementsConstant.EMPTY_ARRAY;
            }

            return builder.toString();
        } else if (parameterClass == FloatArrayAnnotationParameter.class) {
            float[] value = ((FloatArrayAnnotationParameter) rawParameter).getValue();

            if (value != null && value.length > 0) {
                for (float f : value) {
                    builder.addFloat(f);
                }
            } else {
                return ElementsConstant.EMPTY_ARRAY;
            }

            return builder.toString();
        } else if (parameterClass == DoubleArrayAnnotationParameter.class) {
            double[] value = ((DoubleArrayAnnotationParameter) rawParameter).getValue();

            if (value != null && value.length > 0) {
                for (double d : value) {
                    builder.addDouble(d);
                }
            } else {
                return ElementsConstant.EMPTY_ARRAY;
            }

            return builder.toString();
        } else if (parameterClass == ClassArrayAnnotationParameter.class) {
            TypeInfo[] value = ((ClassArrayAnnotationParameter) rawParameter).getValue();

            if (value != null && value.length > 0) {
                for (TypeInfo typeInfo : value) {
                    String parameter = fromClassParameter(typeInfo);
                    builder.addRawString(parameter);
                }
            } else {
                return ElementsConstant.EMPTY_ARRAY;
            }

            return builder.toString();
        } else if (parameterClass == EnumArrayAnnotationParameter.class) {
            TypeInfo enumType = ((EnumArrayAnnotationParameter) rawParameter).getEnumType();
            String[] value = ((EnumArrayAnnotationParameter) rawParameter).getValue();

            if (enumType != null && value != null && value.length > 0) {
                for (String strValue : value) {
                    String enumParameter = fromEnumParameter(enumType, strValue);
                    builder.addRawString(enumParameter);
                }
            } else {
                return ElementsConstant.EMPTY_ARRAY;
            }

            return builder.toString();
        } else if (parameterClass == ValueExpressionAnnotationParameter.class) {
            IValueExpression expression = ((ValueExpressionAnnotationParameter) rawParameter).getValueExpression();
            ValueExpressionToken token = new ValueExpressionToken(expression);

            return token.toString();
        } else if (parameterClass == ValueExpressionArrayAnnotationParameter.class) {
            // 预留与上面一样的换行输出条件

            IValueExpression[] valueExpressions = ((ValueExpressionArrayAnnotationParameter) rawParameter).getValueExpressions();
            for (IValueExpression valueExpression : valueExpressions) {
                ValueExpressionToken token = new ValueExpressionToken(valueExpression);
                builder.addRawString(token.toString());
            }

            return builder.toString();
        }

        throw new IllegalArgumentException(
                "unsupported annotation parameter type: " + rawParameter.getClass().getName());
    }
}
