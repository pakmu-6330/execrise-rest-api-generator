package cn.dyr.rest.generator.java.generator.elements;

import cn.dyr.rest.generator.java.generator.ImportedTypeManager;
import cn.dyr.rest.generator.java.generator.IndentManager;
import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.parameters.annotation.AnnotationParameter;

import java.util.Map;
import java.util.Set;

/**
 * 表示一个注解的元素
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class AnnotationToken implements IToken {

    private AnnotationInfo annotationInfo;

    public AnnotationToken(AnnotationInfo annotationInfo) {
        this.annotationInfo = annotationInfo;
    }

    public AnnotationInfo getAnnotationInfo() {
        return annotationInfo;
    }

    public AnnotationToken setAnnotationInfo(AnnotationInfo annotationInfo) {
        this.annotationInfo = annotationInfo;
        return this;
    }

    @Override
    public String toString() {
        String header;

        ImportedTypeManager typeManager = ImportedTypeManager.get();
        TypeInfo annotationType = this.annotationInfo.getType();

        if (typeManager.isTypeImported(annotationType)) {
            header = String.format("@%s", annotationType.getName());
        } else {
            header = String.format("@%s", annotationType.getFullName());
        }

        // 什么参数都没有，直接返回
        AnnotationParameter defaultParameter = this.annotationInfo.getDefaultValue();
        Map<String, AnnotationParameter> parameters = this.annotationInfo.getParameterMap();

        if (defaultParameter == null &&
                parameters.size() == 0) {
            return header;
        }

        // 仅仅含有默认参数的情况
        if (defaultParameter != null &&
                parameters.size() == 0) {
            AnnotationParameterToken token = new AnnotationParameterToken(defaultParameter);
            return String.format("%s(%s)", header, token.toString());
        }

        // 含有默认参数以外的参数
        StringBuilder parameterStringBuilder = new StringBuilder();
        boolean firstParameter = true;

        if (defaultParameter != null) {
            AnnotationParameterToken token = new AnnotationParameterToken(defaultParameter);

            parameterStringBuilder.append("value = ");
            parameterStringBuilder.append(token.toString());
            firstParameter = false;
        }

        Set<Map.Entry<String, AnnotationParameter>> entrySet = parameters.entrySet();
        for (Map.Entry<String, AnnotationParameter> entry : entrySet) {
            AnnotationParameterToken token = new AnnotationParameterToken(entry.getValue());

            if (firstParameter) {
                firstParameter = false;
            } else {
                parameterStringBuilder.append(", ");
            }

            parameterStringBuilder.append(entry.getKey());
            parameterStringBuilder.append(" = ");
            parameterStringBuilder.append(token.toString());
        }

        return String.format("%s(%s)", header, parameterStringBuilder.toString());
    }
}
