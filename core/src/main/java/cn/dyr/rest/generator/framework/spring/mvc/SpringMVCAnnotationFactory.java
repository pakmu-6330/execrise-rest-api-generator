package cn.dyr.rest.generator.framework.spring.mvc;

import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.java.meta.parameters.annotation.AnnotationParameterFactory;

/**
 * SpringMVC 注解工厂类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SpringMVCAnnotationFactory {

    /**
     * 创建一个 RestController 注解
     *
     * @return 一个 RestController 注解类
     */
    public static AnnotationInfo restController() {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SpringMVCConstant.REST_CONTROLLER_ANNOTATION);
        return new AnnotationInfo().setType(typeInfo);
    }

    /**
     * 创建一个 ControllerAdvice 注解
     *
     * @return ControllerAdvice 注解类
     */
    public static AnnotationInfo controllerAdvice() {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SpringMVCConstant.CONTROLLER_ADVICE_ANNOTATION);
        return new AnnotationInfo().setType(typeInfo);
    }

    /**
     * 创建一个 ExceptionHandler 的注解信息
     *
     * @param typeInfo 要处理的异常类的类型
     * @return ExceptionHandler 注解信息
     */
    public static AnnotationInfo exceptionHandler(TypeInfo typeInfo) {
        TypeInfo annotationType = TypeInfoFactory.fromClass(SpringMVCConstant.EXCEPTION_HANDLER_ANNOTATION);
        return new AnnotationInfo()
                .setType(annotationType)
                .setDefaultValue(AnnotationParameterFactory.classParameter(typeInfo));
    }

    /**
     * 创建一个 ExceptionHandler 的注解信息
     *
     * @param clazz 要处理的异常类的类型
     * @return ExceptionHandler 注解信息
     */
    public static AnnotationInfo exceptionHandler(Class<?> clazz) {
        TypeInfo annotationType = TypeInfoFactory.fromClass(SpringMVCConstant.EXCEPTION_HANDLER_ANNOTATION);
        TypeInfo typeInfo = TypeInfoFactory.fromClass(clazz.getName());
        return new AnnotationInfo()
                .setType(annotationType)
                .setDefaultValue(AnnotationParameterFactory.classParameter(typeInfo));
    }

    /**
     * 创建一个 ResponseStatus 注解
     *
     * @param statusMember 注解的参数值
     * @return 注解的对象
     */
    public static AnnotationInfo responseStatus(String statusMember) {
        TypeInfo annotationType = TypeInfoFactory.fromClass(SpringMVCConstant.RESPONSE_STATUS_ANNOTATION);
        TypeInfo enumType = TypeInfoFactory.fromClass(SpringMVCConstant.HTTP_STATUS_ENUM_CLASS);

        return new AnnotationInfo()
                .setType(annotationType)
                .setDefaultValue(AnnotationParameterFactory.enumerationParameter(enumType, statusMember));
    }

    /**
     * 创建一个 ResponseBody 注解
     *
     * @return 注解的对象
     */
    public static AnnotationInfo responseBody() {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SpringMVCConstant.RESPONSE_BODY_CLASS);
        return new AnnotationInfo().setType(typeInfo);
    }

    /**
     * 创建一个带 value 的 RequestMapping 注解
     *
     * @param value value 值
     * @return 注解对象
     */
    public static AnnotationInfo requestMapping(String value) {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SpringMVCConstant.REQUEST_MAPPING_ANNOTATION);
        return new AnnotationInfo()
                .setType(typeInfo)
                .setDefaultValue(AnnotationParameterFactory.stringParameter(value));
    }

    /**
     * 创建一个 RequestBody 注解
     *
     * @return RequestBody 注解对象
     */
    public static AnnotationInfo requestBody() {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SpringMVCConstant.REQUEST_BODY_ANNOTATION);
        return new AnnotationInfo().setType(typeInfo);
    }

    /**
     * 创建一个带 value 的 GetMapping 注解
     *
     * @param value 注解的 value 参数
     * @return 注解对象
     */
    public static AnnotationInfo getMapping(String value) {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SpringMVCConstant.GET_MAPPING_ANNOTATION);
        return new AnnotationInfo()
                .setType(typeInfo)
                .setDefaultValue(AnnotationParameterFactory.stringParameter(value));
    }

    /**
     * 创建一个带 value 的 DeleteMapping 注解
     *
     * @param value 注解的 value 参数
     * @return 注解对象
     */
    public static AnnotationInfo deleteMapping(String value) {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SpringMVCConstant.DELETE_MAPPING_ANNOTATION);
        return new AnnotationInfo()
                .setType(typeInfo)
                .setDefaultValue(AnnotationParameterFactory.stringParameter(value));
    }

    /**
     * 创建一个带 value 的 PostMapping 注解
     *
     * @param value 注解的参数
     * @return 注解对象
     */
    public static AnnotationInfo postMapping(String value) {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SpringMVCConstant.POST_MAPPING_ANNOTATION);
        return new AnnotationInfo()
                .setType(typeInfo)
                .setDefaultValue(AnnotationParameterFactory.stringParameter(value));
    }

    /**
     * 创建一个带 value 的 PutMapping 注解
     *
     * @param value 注解的参数
     * @return 注解对象
     */
    public static AnnotationInfo putMapping(String value) {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SpringMVCConstant.PUT_MAPPING_ANNOTATION);
        return new AnnotationInfo()
                .setType(typeInfo)
                .setDefaultValue(AnnotationParameterFactory.stringParameter(value));
    }

    /**
     * 创建一个默认的 PageableDefault 注解类型
     *
     * @param defaultPageSize 表示默认页面大小的整数值
     * @return 对应的注解对象
     */
    public static AnnotationInfo pageableDefault(int defaultPageSize) {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SpringMVCConstant.PAGEABLE_DEFAULT_ANNOTATION);
        return new AnnotationInfo()
                .setType(typeInfo)
                .setDefaultValue(AnnotationParameterFactory.intParameter(defaultPageSize));
    }

    /**
     * 创建一个 PathVariable 的注解
     *
     * @param name 参数
     * @return 对应的注解信息
     */
    public static AnnotationInfo pathVariable(String name) {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SpringMVCConstant.PATH_VARIABLE_ANNOTATION);
        return new AnnotationInfo()
                .setType(typeInfo)
                .setDefaultValue(AnnotationParameterFactory.stringParameter(name));
    }
}
