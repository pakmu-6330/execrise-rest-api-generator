package cn.dyr.rest.generator.entity;

/**
 * 表示属性的类型
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public enum AttributeType {

    /**
     * 1 字节长的整型
     */
    BYTE,
    /**
     * 2 字节长的整型
     */
    SHORT,
    /**
     * 4 字节长的整型
     */
    INT,
    /**
     * 8 字节长的整型
     */
    LONG,
    /**
     * 4 字节的浮点型
     */
    FLOAT,
    /**
     * 8 字节的浮点型
     */
    DOUBLE,
    /**
     * 固定长度字符串
     */
    FIXED_STRING,
    /**
     * 可变程度字符串
     */
    VAR_STRING,
    /**
     * 布尔值
     */
    BOOLEAN,
    /**
     * 时间类型
     */
    DATETIME

}
