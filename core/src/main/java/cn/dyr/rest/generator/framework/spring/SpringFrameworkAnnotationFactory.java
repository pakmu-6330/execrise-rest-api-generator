package cn.dyr.rest.generator.framework.spring;

import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;

/**
 * 创建 Spring Framework 相应的注解
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SpringFrameworkAnnotationFactory {

    /**
     * 创建一个 component 注解
     *
     * @return 一个 Component 注解
     */
    public static AnnotationInfo component() {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SpringFrameworkConstant.STEREOTYPE_COMPONENT_ANNOTATION);
        return new AnnotationInfo().setType(typeInfo);
    }

    /**
     * 创建一个 repository 注解
     *
     * @return 一个 Repository 注解
     */
    public static AnnotationInfo repository() {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SpringFrameworkConstant.STEREOTYPE_REPOSITORY_ANNOTATION);
        return new AnnotationInfo().setType(typeInfo);
    }

    /**
     * 创建一个 controller 注解
     *
     * @return 一个 Controller 注解
     */
    public static AnnotationInfo controller() {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SpringFrameworkConstant.STEREOTYPE_CONTROLLER_ANNOTATION);
        return new AnnotationInfo().setType(typeInfo);
    }

    /**
     * 创建一个 service 注解
     *
     * @return 一个 Service 注解
     */
    public static AnnotationInfo service() {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SpringFrameworkConstant.STEREOTYPE_SERVICE_ANNOTATION);
        return new AnnotationInfo().setType(typeInfo);
    }

    /**
     * 创建一个 Transactional 注解
     *
     * @return 一个 Transactional 注解对象
     */
    public static AnnotationInfo transactional() {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SpringFrameworkConstant.TRANSACTIONAL_ANNOTATION);
        return new AnnotationInfo().setType(typeInfo);
    }

    /**
     * 创建一个 Autowired 注解对象
     *
     * @return Autowired 注解对象
     */
    public static AnnotationInfo autowired() {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SpringFrameworkConstant.AUTOWIRED_ANNOTATION);
        return new AnnotationInfo().setType(typeInfo);
    }

    /**
     * 创建一个 Configuration 注解对象
     *
     * @return Configuration 注解对象
     */
    public static AnnotationInfo configuration() {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SpringFrameworkConstant.CONFIGURATION_ANNOTATION);
        return new AnnotationInfo().setType(typeInfo);
    }

    /**
     * 创建一个 Bean 注解对象
     *
     * @return Bean 注解对象
     */
    public static AnnotationInfo bean() {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SpringFrameworkConstant.BEAN_ANNOTATION);
        return new AnnotationInfo().setType(typeInfo);
    }
}
