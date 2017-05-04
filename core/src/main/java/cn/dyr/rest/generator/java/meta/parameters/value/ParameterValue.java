package cn.dyr.rest.generator.java.meta.parameters.value;

import cn.dyr.rest.generator.java.generator.analysis.IImportProcessor;

/**
 * 用于表示在调用方法时传入形参当中的实参
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface ParameterValue extends IImportProcessor {

    /**
     * 表示 byte 类型
     */
    int TYPE_PRIMITIVE_BYTE = 1;

    /**
     * 表示 short 类型
     */
    int TYPE_PRIMITIVE_SHORT = 2;

    /**
     * 表示 int 类型
     */
    int TYPE_PRIMITIVE_INT = 3;

    /**
     * 表示 long 类型
     */
    int TYPE_PRIMITIVE_LONG = 4;

    /**
     * 表示 float 类型
     */
    int TYPE_PRIMITIVE_FLOAT = 5;

    /**
     * 表示 double 类型
     */
    int TYPE_PRIMITIVE_DOUBLE = 6;

    /**
     * 表示 boolean 类型
     */
    int TYPE_PRIMITIVE_BOOLEAN = 7;

    /**
     * 表示 char 类型的变量
     */
    int TYPE_PRIMITIVE_CHAR = 8;

    /**
     * 表示类的 class 对象
     */
    int TYPE_PRIMITIVE_CLASS = 9;

    /**
     * 表示一个字符串对象
     */
    int TYPE_OBJECT_STRING = 10;

    /**
     * 表示这个参数值是一个 null
     */
    int TYPE_OBJECT_NULL = 11;

    /**
     * 表示这个值是一个表达式
     */
    int TYPE_VALUE_EXPRESSION = 12;

    /**
     * 获得这个实参的类型
     *
     * @return 这个实参的类型
     */
    int getType();
}
