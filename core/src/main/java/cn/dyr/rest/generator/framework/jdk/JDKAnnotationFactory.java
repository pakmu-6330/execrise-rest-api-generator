package cn.dyr.rest.generator.framework.jdk;

import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;

/**
 * 用于创建 JDK 自带注解的工厂类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class JDKAnnotationFactory {

    /**
     * 创建一个 Override 注解
     *
     * @return Override 注解
     */
    public static AnnotationInfo override() {
        return new AnnotationInfo().setType(TypeInfoFactory.fromClass(JDKConstant.OVERRIDE_ANNOTATION));
    }

}
