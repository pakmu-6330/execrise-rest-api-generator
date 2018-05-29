package cn.dyr.rest.generator.framework.spring.data;

import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;

/**
 * 用于创建 Spring Data 相关类型的工厂类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SpringDataTypeFactory {

    /**
     * 创建一个带泛型信息的 Page 接口
     *
     * @param typeInfo 泛型信息
     * @return 含有指定的类型作为泛型数据的对象
     */
    public static TypeInfo pageTypeWithGeneric(TypeInfo typeInfo) {
        TypeInfo pageType = TypeInfoFactory.fromClass(SpringDataConstant.INTERFACE_PAGE);
        return TypeInfoFactory.wrapGenerics(pageType, typeInfo);
    }

    /**
     * 创建一个 Pageable 类的接口类型
     *
     * @return Pageable 接口类型
     */
    public static TypeInfo pageable() {
        return TypeInfoFactory.fromClass(SpringDataConstant.INTERFACE_PAGEABLE);
    }

    /**
     * Optional 对象类型
     * @param typeInfo 原始类型
     * @return 经过 Optional 包装的类型
     */
    public static TypeInfo optional(TypeInfo typeInfo) {
        TypeInfo optionalType = TypeInfoFactory.fromClass(SpringDataConstant.TYPE_OPTIONAL);
        return TypeInfoFactory.wrapGenerics(optionalType, typeInfo);
    }

}
