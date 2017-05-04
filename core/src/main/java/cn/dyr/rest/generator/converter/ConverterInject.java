package cn.dyr.rest.generator.converter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 这个注解用于在转换器之间的相互注入
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ConverterInject {

    /**
     * 要注入的转换器的类型
     *
     * @return 要注入的转换器的类型
     */
    ConverterInjectType value();

}
