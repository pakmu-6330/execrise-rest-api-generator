package cn.dyr.rest.generator.converter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 这个注解用于转换数据的注入
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface DataInject {

    /**
     * 表示注入的数据容器类型
     *
     * @return 要进行注入的容器的类型
     */
    DataInjectType value();

}
