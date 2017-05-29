package cn.dyr.rest.generator.framework.spring.data;

import cn.dyr.rest.generator.framework.spring.mvc.SpringMVCAnnotationFactory;
import cn.dyr.rest.generator.java.meta.parameters.Parameter;

/**
 * Spring Data 相关的方法参数创建工厂
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SpringDataParameterFactory {

    /**
     * 创建一个 Pageable 接口类型的参数
     *
     * @return Pageable 类型的参数
     */
    public static Parameter pageable() {
        return new Parameter()
                .setName("pageable")
                .setTypeInfo(SpringDataTypeFactory.pageable());
    }

    /**
     * 创建一个带默认页大小的 Pageable 接口类型的参数
     *
     * @param defaultSize 默认大小
     * @return Pageable 类型的参数
     */
    public static Parameter pageable(int defaultSize) {
        return new Parameter()
                .setName("pageable")
                .setTypeInfo(SpringDataTypeFactory.pageable())
                .addAnnotationInfo(SpringMVCAnnotationFactory.pageableDefault(defaultSize));
    }
}
