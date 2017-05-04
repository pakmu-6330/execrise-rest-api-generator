package cn.dyr.rest.generator.java.meta.factory;

import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.parameters.Parameter;

/**
 * 用于创建参数信息的工厂类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ParameterFactory {

    /**
     * 创建一个参数类
     *
     * @param typeInfo 这个参数的类型
     * @param name     参数名称
     * @return 这个参数对象
     */
    public static Parameter create(TypeInfo typeInfo, String name) {
        return new Parameter().setTypeInfo(typeInfo).setName(name);
    }

}
