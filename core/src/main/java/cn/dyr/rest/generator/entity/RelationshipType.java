package cn.dyr.rest.generator.entity;

/**
 * 表示这个关系之间
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public enum RelationshipType {

    /**
     * 一对一
     */
    ONE_TO_ONE,
    /**
     * 一对多
     */
    ONE_TO_MANY,
    /**
     * 多对一
     */
    MANY_TO_ONE,
    /**
     * 多对多
     */
    MANY_TO_MANY

}
