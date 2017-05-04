package cn.dyr.rest.generator.util;

import cn.dyr.rest.generator.java.meta.TypeInfo;

import java.util.HashSet;
import java.util.Set;

/**
 * 封装一些常用的与类型相关的方法
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class TypeInfoUtils {

    private static Set<String> NUMBER_TYPE_SET;

    static {
        NUMBER_TYPE_SET = new HashSet<>();
        NUMBER_TYPE_SET.add("byte");
        NUMBER_TYPE_SET.add("short");
        NUMBER_TYPE_SET.add("int");
        NUMBER_TYPE_SET.add("long");
        NUMBER_TYPE_SET.add("java.lang.Byte");
        NUMBER_TYPE_SET.add("java.lang.Short");
        NUMBER_TYPE_SET.add("java.lang.Integer");
        NUMBER_TYPE_SET.add("java.lang.Long");
    }

    /**
     * 判断某个特定的类型是否为数字类型
     *
     * @param typeInfo 要判定是否为数字类型的类型对象
     * @return 如果这个类型是整数类型，则返回 true；否则返回 false
     */
    public static boolean isNumber(TypeInfo typeInfo) {
        if (typeInfo.isArray()) {
            return false;
        }

        if (typeInfo.isPrimitiveType()) {
            return (NUMBER_TYPE_SET.contains(typeInfo.getName()));
        } else {
            return NUMBER_TYPE_SET.contains(typeInfo.getFullName());
        }
    }

}
