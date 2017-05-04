package cn.dyr.rest.generator.framework.jpa;

import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.java.meta.parameters.annotation.AnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.AnnotationParameterFactory;

import java.util.Objects;

/**
 * 用于构建 JPA 注解
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class JPARelationshipAnnotationBuilder {

    private TypeInfo annotationType;
    private String classType;
    private String mappedBy;
    private String[] cascade;

    private JPARelationshipAnnotationBuilder(TypeInfo annotationType) {
        this.annotationType = annotationType;
    }

    /**
     * 创建一个 OneToOne 关联关系的注解类型 Builder
     *
     * @return 用于构建这个注解的 Builder
     */
    public static JPARelationshipAnnotationBuilder oneToOne() {
        TypeInfo oneToMany = TypeInfoFactory.fromClass(JPAConstant.ONE_TO_ONE_ANNOTATION);
        JPARelationshipAnnotationBuilder builder = new JPARelationshipAnnotationBuilder(oneToMany);
        builder.classType = JPAConstant.ONE_TO_ONE_ANNOTATION;

        return builder;
    }

    /**
     * 创建一个 OneToMany 关联关系的注解类型 Builder
     *
     * @return 用于构建这个注解的 Builder
     */
    public static JPARelationshipAnnotationBuilder oneToMany() {
        TypeInfo oneToMany = TypeInfoFactory.fromClass(JPAConstant.ONE_TO_MANY_ANNOTATION);
        JPARelationshipAnnotationBuilder builder = new JPARelationshipAnnotationBuilder(oneToMany);
        builder.classType = JPAConstant.ONE_TO_MANY_ANNOTATION;

        return builder;
    }

    /**
     * 创建一个 ManyToMany 关联关系的注解类型 Builder
     *
     * @return 用于构建这个注解的 Builder
     */
    public static JPARelationshipAnnotationBuilder manyToMany() {
        TypeInfo manyToMany = TypeInfoFactory.fromClass(JPAConstant.MANY_TO_MANY_ANNOTATION);
        JPARelationshipAnnotationBuilder builder = new JPARelationshipAnnotationBuilder(manyToMany);
        builder.classType = JPAConstant.MANY_TO_MANY_ANNOTATION;

        return builder;
    }

    /**
     * 创建一个 ManyToOne 关联关系的注解类型 Builder
     *
     * @return 用于构建这个注解的 Builder
     */
    public static JPARelationshipAnnotationBuilder manyToOne() {
        TypeInfo manyToOne = TypeInfoFactory.fromClass(JPAConstant.MANY_TO_ONE_ANNOTATION);
        JPARelationshipAnnotationBuilder builder = new JPARelationshipAnnotationBuilder(manyToOne);
        builder.classType = JPAConstant.MANY_TO_ONE_ANNOTATION;

        return builder;
    }

    /**
     * 构建这个参数的注解信息
     *
     * @return 构建出来的注解信息
     */
    public AnnotationInfo build() {
        AnnotationInfo retValue = new AnnotationInfo();
        retValue.setType(this.annotationType);

        if (this.mappedBy != null && !this.mappedBy.trim().equals("")) {
            AnnotationParameter mappedByParameter = AnnotationParameterFactory.stringParameter(this.mappedBy);
            retValue.addParameter("mappedBy", mappedByParameter);
        }

        if (this.cascade != null) {
            AnnotationParameter cascadeParameter = AnnotationParameterFactory.enumerationArrayParameter(
                    TypeInfoFactory.fromClass(JPAConstant.CASCADE_TYPE_ENUM_CLASS), cascade);
            retValue.addParameter("cascade", cascadeParameter);
        }

        return retValue;
    }

    /**
     * 配置 mappedBy 属性
     *
     * @param mappedBy mappedBy 属性值
     * @return 这个 Builder 本身
     */
    public JPARelationshipAnnotationBuilder mappedBy(String mappedBy) {
        if (Objects.equals(this.classType, JPAConstant.MANY_TO_ONE_ANNOTATION)) {
            throw new IllegalArgumentException("mappedBy parameter cannot be set in @ManyToOne");
        }

        this.mappedBy = mappedBy;
        return this;
    }

    /**
     * 配置 cascade 属性
     *
     * @param cascade cascade 属性值
     * @return 这个 Builder 本身
     */
    public JPARelationshipAnnotationBuilder cascade(String[] cascade) {
        this.cascade = cascade;
        return this;
    }
}
