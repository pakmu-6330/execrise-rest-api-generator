package cn.dyr.rest.generator.java.meta.parameters.annotation;

import cn.dyr.rest.generator.java.generator.analysis.IImportProcessor;

/**
 * 这个接口用于对各个不同数据类型注解参数的一组抽象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface AnnotationParameter extends IImportProcessor {

    int TYPE_PRIMITIVE_BYTE = 0x1;
    int TYPE_PRIMITIVE_SHORT = 0x2;
    int TYPE_PRIMITIVE_INT = 0x3;
    int TYPE_PRIMITIVE_LONG = 0x4;

    int TYPE_PRIMITIVE_FLOAT = 0x5;
    int TYPE_PRIMITIVE_DOUBLE = 0x6;

    int TYPE_PRIMITIVE_BOOLEAN = 0x7;
    int TYPE_PRIMITIVE_CHAR = 0x8;

    int TYPE_STRING = 0x9;
    int TYPE_CLASS = 0xA;
    int TYPE_ENUM = 0xB;
    int TYPE_ANNOTATION = 0xC;

    int TYPE_VALUE_EXPRESSION = 0xD;

    int TYPE_PRIMITIVE_ARRAY_BYTE = 0x11;
    int TYPE_PRIMITIVE_ARRAY_SHORT = 0x12;
    int TYPE_PRIMITIVE_ARRAY_INT = 0x13;
    int TYPE_PRIMITIVE_ARRAY_LONG = 0x14;

    int TYPE_PRIMITIVE_ARRAY_FLOAT = 0x15;
    int TYPE_PRIMITIVE_ARRAY_DOUBLE = 0x16;

    int TYPE_PRIMITIVE_ARRAY_BOOLEAN = 0x17;
    int TYPE_PRIMITIVE_ARRAY_CHAR = 0x18;

    int TYPE_ARRAY_STRING = 0x19;
    int TYPE_ARRAY_CLASS = 0x1A;
    int TYPE_ARRAY_ENUM = 0x1B;
    int TYPE_ARRAY_ANNOTATION = 0x1C;

    int TYPE_ARRAY_VALUE_EXPRESSION = 0x1D;

    /**
     * 获得注解参数的类型
     *
     * @return 表示这个注解参数类型的整数
     */
    int getParameterType();

    /**
     * 获得注解参数的名称
     *
     * @return 这个注解参数的名称
     */
    String getName();
}
