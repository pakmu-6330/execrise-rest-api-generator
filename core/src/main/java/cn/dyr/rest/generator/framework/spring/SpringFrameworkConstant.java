package cn.dyr.rest.generator.framework.spring;

/**
 * 这个类中含有一些 Spring 框架常用的类全名等信息
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SpringFrameworkConstant {

    /**
     * 表示 Component 类型 bean 注解
     */
    public static final String STEREOTYPE_COMPONENT_ANNOTATION = "org.springframework.stereotype.Component";

    /**
     * 表示 Controller 类型 bean 注解
     */
    public static final String STEREOTYPE_CONTROLLER_ANNOTATION = "org.springframework.stereotype.Controller";

    /**
     * 表示 Repository 类型的 bean 注解
     */
    public static final String STEREOTYPE_REPOSITORY_ANNOTATION = "org.springframework.stereotype.Repository";

    /**
     * 表示 Service 类型的 bean 注解
     */
    public static final String STEREOTYPE_SERVICE_ANNOTATION = "org.springframework.stereotype.Service";

    /**
     * 表示 Converter 接口的类全名
     */
    public static final String INTERFACE_CONVERTER = "org.springframework.core.convert.converter.Converter";

    /**
     * 表示 Transactional 注解的类全名
     */
    public static final String TRANSACTIONAL_ANNOTATION = "org.springframework.transaction.annotation.Transactional";

    /**
     * Converter 接口的类全名
     */
    public static final String CONVERTER_INTERFACE_CLASS = "org.springframework.core.convert.converter.Converter";

    /**
     * BeanUtils 类的类全名
     */
    public static final String BEAN_UTILS_CLASS = "org.springframework.beans.BeanUtils";

    /**
     * Autowired 注解的类全名
     */
    public static final String AUTOWIRED_ANNOTATION = "org.springframework.beans.factory.annotation.Autowired";

    /**
     * Configuration 注解的类全名
     */
    public static final String CONFIGURATION_ANNOTATION = "org.springframework.context.annotation.Configuration";

    /**
     * Bean 注解的类全名
     */
    public static final String BEAN_ANNOTATION = "org.springframework.context.annotation.Bean";
}
