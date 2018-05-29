package cn.dyr.rest.generator.framework.spring.mvc;

import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;

import java.util.Objects;

/**
 * Spring MVC 框架
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SpringMVCTypeFactory {

    /**
     * 创建一个 ServletUriComponentsBuilder 的类型
     *
     * @return ServletUriComponentsBuilder 的类型
     */
    public static TypeInfo servletUriComponentsBuilder() {
        return TypeInfoFactory.fromClass(SpringMVCConstant.SERVLET_URI_COMPONENTS_BUILDER_CLASS);
    }

    /**
     * 创建一个 HttpEntity 泛型的类型信息
     *
     * @param typeInfo 泛型类型信息
     * @return 带有泛型信息的类型信息
     */
    public static TypeInfo httpEntity(TypeInfo typeInfo) {
        Objects.requireNonNull(typeInfo, "type info is null");

        TypeInfo entityType = TypeInfoFactory.fromClass(SpringMVCConstant.HTTP_ENTITY_CLASS);
        return TypeInfoFactory.wrapGenerics(entityType, typeInfo);
    }

    /**
     * 创建一个 ResponseEntity 泛型的类型信息
     *
     * @param typeInfo 泛型类型信息
     * @return 带有泛型信息的类型信息
     */
    public static TypeInfo responseEntity(TypeInfo typeInfo) {
        Objects.requireNonNull(typeInfo, "type info is null");

        TypeInfo responseTypeInfo = TypeInfoFactory.fromClass(SpringMVCConstant.RESPONSE_ENTITY_CLASS);
        return TypeInfoFactory.wrapGenerics(responseTypeInfo, typeInfo);
    }

    /**
     * 创建一个 HttpStatus 的枚举类类型
     *
     * @return HttpStatus 的枚举类类型
     */
    public static TypeInfo httpStatus() {
        return TypeInfoFactory.fromClass(SpringMVCConstant.HTTP_STATUS_ENUM_CLASS);
    }

    /**
     * 创建一个 CorsRegistry 类的类型信息
     *
     * @return CorsRegistry 类型信息
     */
    public static TypeInfo corsRegistry() {
        return TypeInfoFactory.fromClass(SpringMVCConstant.CORS_REGISTRY_CLASS);
    }

    /**
     * 创建一个 WebMvcConfigurerAdapter 类的类型信息
     *
     * @return WebMvcConfigurerAdapter 类型信息
     */
    public static TypeInfo webMvcConfigurerAdapter() {
        return TypeInfoFactory.fromClass(SpringMVCConstant.WEB_MVC_CONFIGURER_ADAPTER);
    }

    /**
     * 创建一个 WebMvcConfigurer 接口的类型信息
     *
     * @return 类型信息
     */
    public static TypeInfo webMvcConfigurer() {
        return TypeInfoFactory.fromClass(SpringMVCConstant.WEB_MVC_CONFIGURER_INTERFACE);
    }
}
