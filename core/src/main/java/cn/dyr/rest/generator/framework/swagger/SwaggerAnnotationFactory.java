package cn.dyr.rest.generator.framework.swagger;

import cn.dyr.rest.generator.framework.j2ee.ServletValueExpressionFactory;
import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
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
     * 创建一个用于 Spring Data 分页的参数描述
     *
     * @return 参数描述的注解
     */
    public static AnnotationInfo springDataPageableParameter(int defaultSize) {
        TypeInfo implicitParams = TypeInfoFactory.fromClass(SwaggerTypeConstant.API_IMPLICIT_PARAMS);
        TypeInfo implicitParam = TypeInfoFactory.fromClass(SwaggerTypeConstant.API_IMPLICIT_PARAM);

        AnnotationInfo paramsAnnotation = new AnnotationInfo().setType(implicitParams);

        AnnotationInfo pageAnnotation = new AnnotationInfo().setType(implicitParam)
                .addParameter("name", AnnotationParameterFactory.stringParameter("page"))
                .addParameter("dataType", AnnotationParameterFactory.stringParameter("int"))
                .addParameter("paramType", AnnotationParameterFactory.stringParameter("query"))
                .addParameter("value", AnnotationParameterFactory.stringParameter("页码"))
                .addParameter("defaultValue", AnnotationParameterFactory.stringParameter("0"));
        AnnotationInfo sizeAnnotation = new AnnotationInfo().setType(implicitParam)
                .addParameter("name", AnnotationParameterFactory.stringParameter("size"))
                .addParameter("dataType", AnnotationParameterFactory.stringParameter("int"))
                .addParameter("paramType", AnnotationParameterFactory.stringParameter("query"))
                .addParameter("value", AnnotationParameterFactory.stringParameter("分页大小"))
                .addParameter("defaultValue", AnnotationParameterFactory.stringParameter(String.valueOf(defaultSize)));
        paramsAnnotation.setDefaultValue(AnnotationParameterFactory.annotationArray(pageAnnotation, sizeAnnotation));

        return paramsAnnotation;
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
