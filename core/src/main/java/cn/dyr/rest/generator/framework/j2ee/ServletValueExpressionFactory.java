package cn.dyr.rest.generator.framework.j2ee;

import cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;

/**
 * Servlet 相关的值表达式的工厂类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ServletValueExpressionFactory {

    /**
     * 创建一个整型表示 HTTP OK 响应码的值表达式
     *
     * @return 表示 HTTP OK 响应码的值表达式
     */
    public static IValueExpression okHttpStatusCode() {
        return ValueExpressionFactory
                .classForStatic(ServletTypeFactory.httpServletResponse())
                .accessField("SC_OK");
    }

    /**
     * 创建一个整型表示 HTTP NOT FOUND 响应码的值表达式
     *
     * @return 表示 HTTP NOT FOUND 响应码的值表达式
     */
    public static IValueExpression notFoundHttpStatusCode() {
        return ValueExpressionFactory
                .classForStatic(ServletTypeFactory.httpServletResponse())
                .accessField("SC_NOT_FOUND");
    }

    /**
     * 创建一个整型表示 HTTP CREATED 响应码的值表达式
     *
     * @return 表示 HTTP CREATED 响应码的值表达式
     */
    public static IValueExpression createdHttpStatusCode() {
        return ValueExpressionFactory
                .classForStatic(ServletTypeFactory.httpServletResponse())
                .accessField("SC_CREATED");
    }

    /**
     * 创建一个整型表示 HTTP BAD REQUEST 响应码的值表达式
     *
     * @return 表示 HTTP BAD REQUEST 响应码的值表达式
     */
    public static IValueExpression createBadRequestStatusCode() {
        return ValueExpressionFactory
                .classForStatic(ServletTypeFactory.httpServletResponse())
                .accessField("SC_BAD_REQUEST");
    }

}
