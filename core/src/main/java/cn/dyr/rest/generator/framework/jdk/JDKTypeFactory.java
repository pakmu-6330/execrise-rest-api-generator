package cn.dyr.rest.generator.framework.jdk;

import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;

/**
 * 用于创建 JDK 内置常见类型的工厂类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class JDKTypeFactory {

    /**
     * 创建一个 RuntimeException 异常类的类型
     *
     * @return RuntimeException 异常类的类型
     */
    public static TypeInfo runtimeException() {
        return TypeInfoFactory.fromClass(JDKConstant.RUNTIME_EXCEPTION_CLASS);
    }

}
