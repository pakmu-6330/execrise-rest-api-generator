package cn.dyr.rest.generator.ui.web.common.auth;

import cn.dyr.rest.generator.ui.web.common.TokenSession;
import cn.dyr.rest.generator.ui.web.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * 用于自动从 HTTP 的头部获得相应的 Token
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class AuthTokenSessionArgumentResolver extends HandlerMethodArgumentResolverComposite {

    @Autowired
    private ITokenService tokenService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (parameter.getParameterAnnotation(FromToken.class) != null);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String tokenStr = request.getHeader("Authorization");
        TokenSession tokenSession = this.tokenService.getSession(tokenStr);
        TokenSession sessionSession = (TokenSession) request.getSession().getAttribute("tokenSession");

        if (tokenSession != null) {
            return tokenSession;
        } else {
            return sessionSession;
        }
    }
}
