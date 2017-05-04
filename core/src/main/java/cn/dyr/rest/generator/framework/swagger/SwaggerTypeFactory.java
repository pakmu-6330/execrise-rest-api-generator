package cn.dyr.rest.generator.framework.swagger;

import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;

/**
 * Swagger 相关的类型工厂
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SwaggerTypeFactory {

    /**
     * 创建一个 ApiInfoBuilder 类型
     *
     * @return ApiInfoBuilder 类型
     */
    public static TypeInfo apiInfoBuilder() {
        return TypeInfoFactory.fromClass(SwaggerTypeConstant.API_INFO_BUILDER_CLASS);
    }

    /**
     * 创建一个 ApiInfo 类型
     *
     * @return ApiInfo 类型
     */
    public static TypeInfo apiInfo() {
        return TypeInfoFactory.fromClass(SwaggerTypeConstant.API_INFO_CLASS);
    }

    /**
     * 创建一个 RequestHandlerSelector 类型
     *
     * @return RequestHandlerSelector 类型
     */
    public static TypeInfo requestHandlerSelectors() {
        return TypeInfoFactory.fromClass(SwaggerTypeConstant.REQUEST_HANDLER_SELECTOR_CLASS);
    }

    /**
     * 创建一个 PathSelector 类型
     *
     * @return PathSelector 类型
     */
    public static TypeInfo pathSelector() {
        return TypeInfoFactory.fromClass(SwaggerTypeConstant.PATH_SELECTOR_CLASS);
    }

    /**
     * 创建一个 Docket 类型
     *
     * @return Docket 类型
     */
    public static TypeInfo docket() {
        return TypeInfoFactory.fromClass(SwaggerTypeConstant.DOCKET_CLASS);
    }

    /**
     * 创建一个 DocumentationType 类型
     *
     * @return DocumentationType 类型
     */
    public static TypeInfo documentType() {
        return TypeInfoFactory.fromClass(SwaggerTypeConstant.DOCUMENTATION_TYPE_CLASS);
    }
}
