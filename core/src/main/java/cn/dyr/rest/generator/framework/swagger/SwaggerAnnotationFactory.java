package cn.dyr.rest.generator.framework.swagger;

import cn.dyr.rest.generator.framework.j2ee.ServletValueExpressionFactory;
import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.java.meta.parameters.annotation.AnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.AnnotationParameterFactory;

/**
 * Swagger 相关的注解工厂
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SwaggerAnnotationFactory {

    /**
     * 创建 EnableSwagger2 的注解
     *
     * @return 相应的注解
     */
    public static AnnotationInfo enableSwagger() {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SwaggerTypeConstant.ENABLE_SWAGGER_2_ANNOTATION);
        return new AnnotationInfo().setType(typeInfo);
    }

    /**
     * 创建一个 @Api 注解
     *
     * @param description description 参数值
     * @return 对应的 @Api 注解
     */
    public static AnnotationInfo apiAnnotation(String description) {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SwaggerTypeConstant.API_ANNOTATION);

        return new AnnotationInfo()
                .setType(typeInfo)
                .addParameter("description", AnnotationParameterFactory.stringParameter(description));
    }

    /**
     * 创建一个 ApiOperation 的注解
     *
     * @param value 注解的参数值
     * @return 含有这个参数值的注解信息
     */
    public static AnnotationInfo apiOperation(String value) {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SwaggerTypeConstant.API_OPERATION_ANNOTATION);

        return new AnnotationInfo()
                .setType(typeInfo)
                .setDefaultValue(AnnotationParameterFactory.stringParameter(value));
    }

    /**
     * 创建分页大小的参数注解
     *
     * @param defaultSize 默认分页大小
     * @return 分页大小 Swagger 参数
     */
    public static AnnotationInfo springDataPageableSizeAnnotation(int defaultSize) {
        TypeInfo implicitParam = TypeInfoFactory.fromClass(SwaggerTypeConstant.API_IMPLICIT_PARAM);
        return new AnnotationInfo().setType(implicitParam)
                .addParameter("name", AnnotationParameterFactory.stringParameter("size"))
                .addParameter("dataType", AnnotationParameterFactory.stringParameter("int"))
                .addParameter("paramType", AnnotationParameterFactory.stringParameter("query"))
                .addParameter("value", AnnotationParameterFactory.stringParameter("分页大小"))
                .addParameter("defaultValue", AnnotationParameterFactory.stringParameter(String.valueOf(defaultSize)));
    }

    /**
     * 创建分页页码的注解参数
     *
     * @return 分页页码参数的 Swagger 注解
     */
    public static AnnotationInfo springDataPageablePageAnnotation() {
        TypeInfo implicitParam = TypeInfoFactory.fromClass(SwaggerTypeConstant.API_IMPLICIT_PARAM);
        return new AnnotationInfo().setType(implicitParam)
                .addParameter("name", AnnotationParameterFactory.stringParameter("page"))
                .addParameter("dataType", AnnotationParameterFactory.stringParameter("int"))
                .addParameter("paramType", AnnotationParameterFactory.stringParameter("query"))
                .addParameter("value", AnnotationParameterFactory.stringParameter("页码"))
                .addParameter("defaultValue", AnnotationParameterFactory.stringParameter("0"));
    }

    /**
     * 创建一个 path 类型的注解参数
     *
     * @param name         参数名称
     * @param type         参数类型
     * @param value        参数说明
     * @param defaultValue 参数默认值
     * @return 这个参数对应的 Swagger 注解
     */
    public static AnnotationInfo implicitPathParam(String name, String type, String value, String defaultValue) {
        TypeInfo implicitParam = TypeInfoFactory.fromClass(SwaggerTypeConstant.API_IMPLICIT_PARAM);
        return new AnnotationInfo().setType(implicitParam)
                .addParameter("name", AnnotationParameterFactory.stringParameter(name))
                .addParameter("dataType", AnnotationParameterFactory.stringParameter(type))
                .addParameter("paramType", AnnotationParameterFactory.stringParameter("path"))
                .addParameter("value", AnnotationParameterFactory.stringParameter(value))
                .addParameter("defaultValue", AnnotationParameterFactory.stringParameter(defaultValue));
    }

    /**
     * 根据一系列的参数注解创建一个 ImplicitParams 数组
     *
     * @param annotationInfoList 数组列表
     * @return 相应的注解数组
     */
    public static AnnotationInfo implicitParams(AnnotationInfo... annotationInfoList) {
        TypeInfo implicitParams = TypeInfoFactory.fromClass(SwaggerTypeConstant.API_IMPLICIT_PARAMS);
        return new AnnotationInfo().setType(implicitParams)
                .setDefaultValue(AnnotationParameterFactory.annotationArray(annotationInfoList));
    }

    /**
     * 创建一个用于 Spring Data 分页的参数描述
     *
     * @return 参数描述的注解
     */
    public static AnnotationInfo springDataPageableParameter(int defaultSize) {
        AnnotationInfo pageAnnotation = springDataPageablePageAnnotation();
        AnnotationInfo sizeAnnotation = springDataPageableSizeAnnotation(defaultSize);

        return implicitParams(pageAnnotation, sizeAnnotation);
    }

    /**
     * 创建一个 ApiParam 注解信息
     *
     * @param name        注解名称
     * @param description 注解的描述
     * @return ApiParam 注解对象
     */
    public static AnnotationInfo apiParam(String name, String description) {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SwaggerTypeConstant.API_PARAM);
        return new AnnotationInfo().setType(typeInfo)
                .setDefaultValue(AnnotationParameterFactory.stringParameter(description))
                .addParameter("name", AnnotationParameterFactory.stringParameter("id"));
    }

    /**
     * 创建一个 Api Response 注解对象
     *
     * @param list ApiResponse 注解对象列表
     * @return ApiResponse 注解对象
     */
    public static AnnotationInfo apiResponses(AnnotationInfo... list) {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SwaggerTypeConstant.API_RESPONSES);
        return new AnnotationInfo().setType(typeInfo)
                .setDefaultValue(AnnotationParameterFactory.annotationArray(list));
    }

    /**
     * 创建一个 HTTP OK 的 API Response 注解对象
     *
     * @param message message 参数
     * @return 对应的 HTTP OK ApiResponse 注解对象
     */
    public static AnnotationInfo createHttpOkResponse(String message) {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SwaggerTypeConstant.API_RESPONSE);
        return new AnnotationInfo()
                .setType(typeInfo)
                .addParameter("code", ServletValueExpressionFactory.okHttpStatusCode())
                .addParameter("message", message);
    }

    /**
     * 创建一个 HTTP CREATED 的 API Response 注解对象
     *
     * @param message message 参数
     * @return 对应的 HTTP CREATED ApiResponse 注解对象
     */
    public static AnnotationInfo createHttpCreatedResponse(String message) {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SwaggerTypeConstant.API_RESPONSE);
        return new AnnotationInfo()
                .setType(typeInfo)
                .addParameter("code", ServletValueExpressionFactory.createdHttpStatusCode())
                .addParameter("message", message);
    }

    /**
     * 创建一个 HTTP NOT FOUND 的 API Response 注解对象
     *
     * @param message message 参数
     * @return 对应的 HTTP NOT FOUND ApiResponse 注解对象
     */
    public static AnnotationInfo createNotFoundResponse(String message) {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SwaggerTypeConstant.API_RESPONSE);
        return new AnnotationInfo()
                .setType(typeInfo)
                .addParameter("code", ServletValueExpressionFactory.notFoundHttpStatusCode())
                .addParameter("message", message);
    }

    /**
     * 创建一个 HTTP BAD REQUEST 的 API Response 注解对象
     *
     * @param message message 参数
     * @return 对应的 HTTP BAD REQUEST ApiResponse 注解对象
     */
    public static AnnotationInfo createBadRequestResponse(String message) {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SwaggerTypeConstant.API_RESPONSE);
        return new AnnotationInfo()
                .setType(typeInfo)
                .addParameter("code", ServletValueExpressionFactory.createBadRequestStatusCode())
                .addParameter("message", message);
    }

}
