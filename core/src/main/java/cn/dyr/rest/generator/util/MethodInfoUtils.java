package cn.dyr.rest.generator.util;

import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.MethodInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.parameters.Parameter;

import java.util.List;
import java.util.Objects;

/**
 * 方法信息相关的工具类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class MethodInfoUtils {

    /**
     * 判断方法是否含有特定类型的注解
     *
     * @param methodInfo 要进行判断的方法信息
     * @param typeInfo   注解类型
     * @return 如果这个方法包含这个注解，则返回 true；否则返回 false
     */
    public static boolean isMethodContainsAnnotationType(MethodInfo methodInfo, TypeInfo typeInfo) {
        Objects.requireNonNull(methodInfo);
        Objects.requireNonNull(typeInfo);

        List<AnnotationInfo> annotationInfoList = methodInfo.getAnnotationInfoList();
        if (annotationInfoList != null && annotationInfoList.size() > 0) {
            for (AnnotationInfo annotationInfo : annotationInfoList) {
                if (annotationInfo.getType().getFullName().equals(typeInfo.getFullName())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 创建一个具备相同签名的方法
     *
     * @param src 原始方法信息对象
     * @return 签名与原始方法相同的对象
     */
    public static MethodInfo createMethodWithSameSignature(MethodInfo src) {
        MethodInfo retValue = new MethodInfo();

        retValue.setModifier(CloneUtils.cloneModifier(src.getModifier()));
        retValue.setName(src.getName());
        retValue.setReturnValueType(src.getReturnValueType());

        List<Parameter> parameters = src.getParameters();
        if (parameters != null) {
            for (Parameter parameter : parameters) {
                retValue.addParameter(CloneUtils.cloneParameterWithoutAnnotations(parameter));
            }
        }

        return retValue;
    }

}
