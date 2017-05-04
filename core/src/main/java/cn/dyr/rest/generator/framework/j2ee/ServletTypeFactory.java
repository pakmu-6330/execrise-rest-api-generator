package cn.dyr.rest.generator.framework.j2ee;

import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;

/**
 * 用于创建 Servlet 相关的类型
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ServletTypeFactory {

    /**
     * 创建一个 Http Servlet Request 的类型信息
     *
     * @return Http Servlet Request 的类型信息
     */
    public static TypeInfo httpServletRequest() {
        return TypeInfoFactory.fromClass(ServletConstant.HTTP_SERVLET_REQUEST);
    }

    /**
     * 创建一个 Http Servlet Response 的类型信息
     *
     * @return Http Servlet Response 的类型信息
     */
    public static TypeInfo httpServletResponse() {
        return TypeInfoFactory.fromClass(ServletConstant.HTTP_SERVLET_RESPONSE);
    }

}
