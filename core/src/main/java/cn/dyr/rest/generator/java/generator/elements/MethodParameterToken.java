package cn.dyr.rest.generator.java.generator.elements;

import cn.dyr.rest.generator.java.generator.ImportedTypeManager;
import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.parameters.Parameter;
import cn.dyr.rest.generator.util.CommaStringBuilder;

import java.util.List;

/**
 * 用作方法形参列表的元素
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class MethodParameterToken implements IToken {

    private Parameter parameter;

    public MethodParameterToken(Parameter parameter) {
        this.parameter = parameter;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public MethodParameterToken setParameter(Parameter parameter) {
        this.parameter = parameter;
        return this;
    }

    @Override
    public String toString() {
        if (this.parameter == null) {
            throw new NullPointerException("null parameter cannot by null");
        }

        ImportedTypeManager typeManager = ImportedTypeManager.get();
        String typeStr;
        TypeInfo typeInfo = this.parameter.getTypeInfo();

        TypeInfo parameterType = parameter.getTypeInfo();
        if (typeManager.isTypeImported(typeInfo)) {
            typeStr = parameterType.getName();
        } else {
            typeStr = parameterType.getFullName();
        }

        // 如果参数类型含有泛型信息，则进行相应的处理
        List<TypeInfo> parameterizeType = parameterType.getParameterizeType();
        if (parameterizeType != null && parameterizeType.size() > 0) {
            CommaStringBuilder genericStringBuilder = new CommaStringBuilder();

            for (TypeInfo genericType : parameterizeType) {
                TypeInfoToken genericTypeToken = new TypeInfoToken(genericType);
                genericStringBuilder.addValue(genericTypeToken.toString());
            }

            typeStr = typeStr + "<" + genericStringBuilder.toString() + ">";
        }

        // 如果是数组则在类型的后面增加表示数组的方括号
        if (typeInfo.isArray()) {
            typeStr = typeStr + "[]";
        }

        // 在参数前增加相应的注解
        StringBuilder annotationStrBuilder = new StringBuilder();

        List<AnnotationInfo> annotationList = this.parameter.getAnnotationInfoList();
        for (AnnotationInfo annotation : annotationList) {
            AnnotationToken annotationToken = new AnnotationToken(annotation);

            annotationStrBuilder.append(annotationToken.toString());
            annotationStrBuilder.append(" ");
        }

        return String.format("%s%s %s", annotationStrBuilder.toString(), typeStr, parameter.getName());
    }
}
