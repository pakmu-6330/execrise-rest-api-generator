package cn.dyr.rest.generator.framework.jdk;

import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;

/**
 * 用于快速创建 JDK 自带集合类型的工厂类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class CollectionsTypeFactory {

    /**
     * 创建一个泛型的 List 类型
     *
     * @param typeInfo 类型
     * @return 对应的类型
     */
    public static TypeInfo listWithGeneric(TypeInfo typeInfo) {
        if (typeInfo == null) {
            throw new NullPointerException("type is null");
        }

        TypeInfo listTypeInfo = TypeInfoFactory.fromClass(CollectionsConstant.LIST_CLASS);
        return TypeInfoFactory.wrapGenerics(listTypeInfo, typeInfo);
    }

    /**
     * 创建一个带泛型信息的 ArrayList 类型
     *
     * @param typeInfo 类型
     * @return 对应的类型
     */
    public static TypeInfo arrayListWithGeneric(TypeInfo typeInfo) {
        if (typeInfo == null) {
            throw new NullPointerException("type is null");
        }

        TypeInfo listTypeInfo = TypeInfoFactory.fromClass(CollectionsConstant.ARRAY_LIST_CLASS);
        return TypeInfoFactory.wrapGenerics(listTypeInfo, typeInfo);
    }

    /**
     * 返回 Collections 工具类的类型
     *
     * @return 工具类的类型
     */
    public static TypeInfo collections() {
        return TypeInfoFactory.fromClass(CollectionsConstant.COLLECTION_UTILS_CLASS);
    }

    /**
     * 创建一个带泛型信息的 Map 的接口类类型
     *
     * @param key   作为 key 的类型
     * @param value 作为 value 的类型
     * @return 带有泛型信息的 Map 的接口类型信息
     */
    public static TypeInfo mapWithGeneric(TypeInfo key, TypeInfo value) {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(CollectionsConstant.MAP_CLASS);
        return TypeInfoFactory.wrapGenerics(typeInfo, new TypeInfo[]{key, value});
    }

    /**
     * 创建一个带泛型信息的 HashMap 的接口类类型
     *
     * @param key   key 类型
     * @param value value 类型
     * @return 带有泛型信息的 HashMap 的接口类型信息
     */
    public static TypeInfo hashMapWithGeneric(TypeInfo key, TypeInfo value) {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(CollectionsConstant.HASH_MAP_CLASS);
        return TypeInfoFactory.wrapGenerics(typeInfo, new TypeInfo[]{key, value});
    }

    /**
     * 创建一个带泛型信息的 LinkedHashMap 的接口类类型
     *
     * @param key   key 类型
     * @param value value 类型
     * @return 带有泛型信息的 LinkedHashMap 的接口类型信息
     */
    public static TypeInfo linkedHashMapWithGeneric(TypeInfo key, TypeInfo value) {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(CollectionsConstant.LINKED_HASH_MAP_CLASS);
        return TypeInfoFactory.wrapGenerics(typeInfo, new TypeInfo[]{key, value});
    }
}
