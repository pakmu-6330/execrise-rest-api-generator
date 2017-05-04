package cn.dyr.rest.generator.framework.spring;

import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;

/**
 * Spring 框架的类型工厂类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SpringFrameworkTypeFactory {

    /**
     * 创建一个 Converter 接口类型
     *
     * @param srcType    源类型
     * @param targetType 目标类型
     * @return 含有源类型和目标类型泛型信息的 Converter 接口类型
     */
    public static TypeInfo converter(TypeInfo srcType, TypeInfo targetType) {
        TypeInfo baseInterfaceType = TypeInfoFactory.fromClass(SpringFrameworkConstant.CONVERTER_INTERFACE_CLASS);
        return TypeInfoFactory.wrapGenerics(baseInterfaceType, new TypeInfo[]{srcType, targetType});
    }

    /**
     * 创建 BeanUtils 类的类型信息
     *
     * @return BeanUtils 类型信息对象
     */
    public static TypeInfo beanUtils() {
        return TypeInfoFactory.fromClass(SpringFrameworkConstant.BEAN_UTILS_CLASS);
    }

}
