package cn.dyr.rest.generator.ui.web.common.auth;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 这个注解定义了执行这个方法所需的权限信息
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthMethod {

    AuthType value() default AuthType.VERIFIED;

}
