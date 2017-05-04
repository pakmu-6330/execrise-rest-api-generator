package cn.dyr.rest.generator.framework.spring.mvc;

import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;

/**
 * 用于创建 SpringMVC 常见的值表达式
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SpringMVCValueExpressionFactory {

    /**
     * 创建一个表示 HTTP 200 的状态枚举
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.3.1">HTTP/1.1: Semantics and Content, section 6.3.1</a>
     * @return 表示 200 的状态枚举
     */
    public static IValueExpression httpStatusOK() {
        TypeInfo typeInfo = SpringMVCTypeFactory.httpStatus();
        return ValueExpressionFactory.enumerationValue(typeInfo, SpringMVCConstant.HTTP_STATUS_MEMBER_OK);
    }

    /**
     * 创建一个表示 HTTP 404 的状态枚举
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.5.4">HTTP/1.1: Semantics and Content, section 6.5.4</a>
     * @return 表示 404 的状态枚举
     */
    public static IValueExpression httpStatusNotFound() {
        TypeInfo typeInfo = SpringMVCTypeFactory.httpStatus();
        return ValueExpressionFactory.enumerationValue(typeInfo, SpringMVCConstant.HTTP_STATUS_MEMBER_NOT_FOUND);
    }

    /**
     * 创建一个 HTTP 201 的状态枚举
     *
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.3.2">HTTP/1.1: Semantics and Content, section 6.3.2</a>
     * @return 表示 201 的枚举状态
     */
    public static IValueExpression httpStatusCreated() {
        TypeInfo typeInfo = SpringMVCTypeFactory.httpStatus();
        return ValueExpressionFactory.enumerationValue(typeInfo, SpringMVCConstant.HTTP_STATUS_MEMBER_CREATED);
    }
}
