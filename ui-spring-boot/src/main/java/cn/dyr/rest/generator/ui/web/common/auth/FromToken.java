package cn.dyr.rest.generator.ui.web.common.auth;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用在参数当中，表示通过 Authorization 头获得相应的 SessionToken 对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FromToken {

    

}
