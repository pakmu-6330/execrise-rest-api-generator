package cn.dyr.rest.generator.ui.web.common.auth;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 这个注解表示这是一个跳转到页面的控制器，需要进行权限的认证
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PageAuth {
}
