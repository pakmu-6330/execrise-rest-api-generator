package cn.dyr.rest.generator.framework.spring;

import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;

/**
 * 用于创建 Spring 框架当中一些异常类的类型
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SpringFrameworkExceptionTypeFactory {

    /**
     * 创建一个 DataIntegrityViolationException 异常类的类型信息
     *
     * @return 对应的类型信息
     */
    public static TypeInfo dataIntegrityViolationException() {
        return TypeInfoFactory.fromClass(SpringFrameworkExceptionConstant.DATA_INTEGRITY_VIOLATION_EXCEPTION);
    }

}
