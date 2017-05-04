package cn.dyr.rest.generator.framework.jpa;

import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;

/**
 * 用于创建 JPA 相关的类型
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class JPATypeFactory {

    /**
     * 创建一个 Id 注解的类型
     *
     * @return Id 注解的类型对象
     */
    public static TypeInfo idAnnotationType() {
        return TypeInfoFactory.fromClass(JPAConstant.ID_ANNOTATION);
    }

}
