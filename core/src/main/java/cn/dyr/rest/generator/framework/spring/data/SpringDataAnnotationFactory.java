package cn.dyr.rest.generator.framework.spring.data;

import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.java.meta.parameters.annotation.AnnotationParameterFactory;

/**
 * Spring Data 相关注解的工厂类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SpringDataAnnotationFactory {

    /**
     * 创建一个启用 JPA 注解形式 DAO 的注解
     *
     * @param daoPackageName DAO 包的包名
     * @return 对应 JPA 注解对象
     */
    public static AnnotationInfo enableJPARepositories(String daoPackageName) {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(SpringDataConstant.ENABLE_JPA_REPOSITORY_ANNOTATION);
        AnnotationInfo info = new AnnotationInfo();
        info.setType(typeInfo);
        info.setDefaultValue(AnnotationParameterFactory.stringParameter(daoPackageName));

        return info;
    }

    /**
     * 创建一个用于 DAO 接口方法的 Query 注解
     *
     * @param jpql JPQL 语句
     * @return 含有这个 JPQL 的注解对象
     */
    public static AnnotationInfo query(String jpql) {
        TypeInfo queryType = TypeInfoFactory.fromClass(SpringDataConstant.QUERY_ANNOTATION);
        return new AnnotationInfo()
                .setType(queryType)
                .setDefaultValue(AnnotationParameterFactory.stringParameter(jpql));
    }

}
