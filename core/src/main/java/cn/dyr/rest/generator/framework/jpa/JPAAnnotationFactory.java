package cn.dyr.rest.generator.framework.jpa;

import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.java.meta.parameters.annotation.AnnotationParameterFactory;

/**
 * 用于产生 JPA 注解的注解工厂类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class JPAAnnotationFactory {

    /**
     * 创建一个用于指明实体对应表名的注解信息
     *
     * @param tableName 表名
     * @return 指定表名的注解信息
     */
    public static AnnotationInfo tableAnnotation(String tableName) {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(JPAConstant.TABLE_ANNOTATION);

        AnnotationInfo retValue = new AnnotationInfo();
        retValue.setType(typeInfo);
        retValue.addParameter("name", AnnotationParameterFactory.stringParameter(tableName));

        return retValue;
    }

    /**
     * 创建一个实体标记的注解
     *
     * @return 一个用于标记实体的注解
     */
    public static AnnotationInfo entityAnnotation() {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(JPAConstant.ENTITY_ANNOTATION);

        AnnotationInfo retValue = new AnnotationInfo();
        retValue.setType(typeInfo);

        return retValue;
    }

    /**
     * 创建一个标记主键的注解
     *
     * @return 一个标记主键的注解
     */
    public static AnnotationInfo idAnnotation() {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(JPAConstant.ID_ANNOTATION);

        AnnotationInfo retValue = new AnnotationInfo();
        retValue.setType(typeInfo);

        return retValue;
    }

    /**
     * 创建一个 strategy 参数为 IDENTITY 的注解
     *
     * @return IDENTITY 策略生成值的 GeneratedValue 注解
     */
    public static AnnotationInfo createIdentityGeneratedValued() {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(JPAConstant.GENERATED_VALUE_ANNOTATION);
        TypeInfo generationTypeEnum = TypeInfoFactory.fromClass(JPAConstant.GENERATION_TYPE_ENUM_CLASS);

        AnnotationInfo retValue = new AnnotationInfo();
        retValue.setType(typeInfo);
        retValue.addParameter(
                "strategy",
                AnnotationParameterFactory.enumerationParameter(generationTypeEnum, JPAConstant.GENERATION_TYPE_MEMBER_IDENTITY));

        return retValue;
    }

    /**
     * 创建一个 idClass 注解
     *
     * @param clazz 参数
     * @return 对应的 idClass 注解对象
     */
    public static AnnotationInfo createIdClassAnnotation(TypeInfo clazz) {
        TypeInfo idClassTypeInfo = TypeInfoFactory.fromClass(JPAConstant.ID_CLASS_ANNOTATION);

        AnnotationInfo retValue = new AnnotationInfo();
        retValue.setType(idClassTypeInfo);
        retValue.setDefaultValue(AnnotationParameterFactory.classParameter(clazz));

        return retValue;
    }

    /**
     * 创建一个 Column 注解
     *
     * @return 一个 Column 注解
     */
    public static AnnotationInfo columnAnnotation() {
        TypeInfo columnTypeInfo = TypeInfoFactory.fromClass(JPAConstant.COLUMN_ANNOTATION);

        AnnotationInfo retValue = new AnnotationInfo();
        retValue.setType(columnTypeInfo);

        return retValue;
    }

    /**
     * 创建一个 JoinColumn 注解信息
     *
     * @param name 外键列名
     * @return 相应的 JoinColumn 注解
     */
    public static AnnotationInfo joinColumn(String name) {
        TypeInfo joinColumnTypeInfo = TypeInfoFactory.fromClass(JPAConstant.JOIN_COLUMN_ANNOTATION);

        AnnotationInfo retValue = new AnnotationInfo();
        retValue.setType(joinColumnTypeInfo);
        retValue.addParameter("name", name);

        return retValue;
    }
}
