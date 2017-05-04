package cn.dyr.rest.generator.framework.spring.boot;

import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.factory.AnnotationFactory;

/**
 * Spring Boot 相关注解的工厂类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SpringBootAnnotationFactory {

    /**
     * 创建一个 SpringBootApplication 注解
     *
     * @return SpringBootApplication 注解
     */
    public static AnnotationInfo appAnnotation() {
        return AnnotationFactory.fromClassName(SpringBootConstant.SPRING_BOOT_APPLICATION_ANNOTATION);
    }

}
