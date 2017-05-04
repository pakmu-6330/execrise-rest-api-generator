package cn.dyr.rest.generator.util;

import cn.dyr.rest.generator.java.meta.TypeInfo;

/**
 * 用于对类型信息作出断言的工具类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class TypeInfoAssertUtils {

    /**
     * 断定这个类型不是基本数据类型
     *
     * @param typeInfo 要进行断定的数据类型
     */
    public static void assertNotPrimitiveType(TypeInfo typeInfo) {
        if (typeInfo.isPrimitiveType()) {
            throw new IllegalArgumentException("typeInfo is primitive type");
        }
    }

    /**
     * 断定这个类型不是数组类型
     *
     * @param typeInfo 要进行断定的数据类型
     */
    public static void assertNotArrayType(TypeInfo typeInfo) {
        if (typeInfo.isArray()) {
            throw new IllegalArgumentException("typeInfo is array type");
        }
    }

}
