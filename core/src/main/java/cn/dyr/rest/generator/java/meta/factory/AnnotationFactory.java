package cn.dyr.rest.generator.java.meta.factory;

import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;

/**
 * 一些比较常用的注解创建方法
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class AnnotationFactory {

    /**
     * 根据类全名创建一个注解对象
     *
     * @param className 类全名
     * @return 这个类全名对应注解对象
     */
    public static AnnotationInfo fromClassName(String className) {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(className);
        return new AnnotationInfo().setType(typeInfo);
    }

}
