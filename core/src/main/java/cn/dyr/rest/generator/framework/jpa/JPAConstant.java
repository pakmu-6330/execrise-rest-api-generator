package cn.dyr.rest.generator.framework.jpa;

/**
 * 这个类中定义了 JPA 当中的注解类型的类全名
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class JPAConstant {

    /**
     * Table 注解的全名
     */
    public static final String TABLE_ANNOTATION = "javax.persistence.Table";

    /**
     * Entity 注解的全名
     */
    public static final String ENTITY_ANNOTATION = "javax.persistence.Entity";

    /**
     * Id 注解的全名
     */
    public static final String ID_ANNOTATION = "javax.persistence.Id";

    /**
     * IdClass 注解的全名
     */
    public static final String ID_CLASS_ANNOTATION = "javax.persistence.IdClass";

    /**
     * Column 注解的全名
     */
    public static final String COLUMN_ANNOTATION = "javax.persistence.Column";

    /**
     * GeneratedValue 注解的类全名
     */
    public static final String GENERATED_VALUE_ANNOTATION = "javax.persistence.GeneratedValue";

    /**
     * OneToOne 注解的类全名
     */
    public static final String ONE_TO_ONE_ANNOTATION = "javax.persistence.OneToOne";

    /**
     * OneToMany 注解类全名
     */
    public static final String ONE_TO_MANY_ANNOTATION = "javax.persistence.OneToMany";

    /**
     * ManyToOne 注解类全名
     */
    public static final String MANY_TO_ONE_ANNOTATION = "javax.persistence.ManyToOne";

    /**
     * ManyToMany 注解类全名
     */
    public static final String MANY_TO_MANY_ANNOTATION = "javax.persistence.ManyToMany";

    /**
     * GenerationType 枚举类全名，用在 GeneratedValue 注解里面
     */
    public static final String GENERATION_TYPE_ENUM_CLASS = "javax.persistence.GenerationType";

    /**
     * GenerationType 枚举成员 identity
     */
    public static final String GENERATION_TYPE_MEMBER_IDENTITY = "IDENTITY";

    /**
     * GenerationType 枚举成员 sequence
     */
    public static final String GENERATION_TYPE_MEMBER_SEQUENCE = "SEQUENCE";

    /**
     * 级联 Cascade 枚举类的类全名
     */
    public static final String CASCADE_TYPE_ENUM_CLASS = "javax.persistence.CascadeType";

    /**
     * 级联枚举当中的 ALL 成员
     */
    public static final String CASCADE_TYPE_MEMBER_ALL = "ALL";

    /**
     * 级联枚举当中的 PERSIST 成员
     */
    public static final String CASCADE_TYPE_MEMBER_PERSIST = "PERSIST";

    /**
     * 级联枚举当中的 MERGE 成员
     */
    public static final String CASCADE_TYPE_MEMBER_MERGE = "MERGE";

    /**
     * 级联枚举当中的 REMOVE 成员
     */
    public static final String CASCADE_TYPE_MEMBER_REMOVE = "REMOVE";

    /**
     * 级联枚举当中的 REFRESH 成员
     */
    public static final String CASCADE_TYPE_MEMBER_REFRESH = "REFRESH";

}
