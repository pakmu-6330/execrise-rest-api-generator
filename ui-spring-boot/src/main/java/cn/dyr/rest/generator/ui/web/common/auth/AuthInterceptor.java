package cn.dyr.rest.generator.ui.web.common.auth;

import cn.dyr.rest.generator.ui.web.common.APIResponse;
import cn.dyr.rest.generator.ui.web.common.TokenSession;
import cn.dyr.rest.generator.ui.web.common.factory.ResponseMetaFactory;
import cn.dyr.rest.generator.ui.web.constant.EntityConstants;
import cn.dyr.rest.generator.ui.web.service.ITokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 这个拦截器用于进行相应的权限管理
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private ITokenService tokenService;

    private ObjectMapper objectMapper;

    public AuthInterceptor() {
        this.objectMapper = new ObjectMapper();
    }

    private void writeAuthPermissionDeniedResponse(HttpServletResponse response) throws IOException {
        APIResponse<HashMap<String, Object>> retValue = new APIResponse<>(
                ResponseMetaFactory.authFailed(),
                new HashMap<String, Object>());
        String jsonString = objectMapper.writeValueAsString(retValue);

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonString);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.preHandle(request, response, handler);
        HttpSession httpSession = request.getSession();
        String headerToken = request.getHeader("Authorization");

        // DEBUG
        if (httpSession.getAttribute("tokenSession") == null) {
            TokenSession login = tokenService.login("user", "deng669593");
            httpSession.setAttribute("tokenSession", login);
        }

        TokenSession headerSession = tokenService.getSession(headerToken);
        TokenSession sessionSession = (TokenSession) httpSession.getAttribute("tokenSession");
        TokenSession targetSession = (headerSession == null ? sessionSession : headerSession);

        TokenSessionHolder.getInstance().setTokenSession(headerSession);

        if (handler instanceof HandlerMethod) {
            Method handlerMethod = ((HandlerMethod) handler).getMethod();

            AuthMethod apiAuth = handlerMethod.getAnnotation(AuthMethod.class);
            PageAuth pageAuth = handlerMethod.getAnnotation(PageAuth.class);

            if (apiAuth != null) {
                // 如果需要权限，然鹅你并没有登录
                if (headerToken == null || targetSession == null) {
                    writeAuthPermissionDeniedResponse(response);
                    return false;
                }

                if (apiAuth.value() == AuthType.VERIFIED) {
                    if (targetSession.getUserEntity().getStatus() != EntityConstants.UserEntityConstant.USER_STATUS_VERIFIED) {
                        writeAuthPermissionDeniedResponse(response);
                        return false;
                    }
                }
            } else if (pageAuth != null && sessionSession == null) {
                response.sendRedirect(request.getContextPath() + "/login.html");
                return false;
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        TokenSessionHolder.getInstance().removeTokenSession();

        super.afterCompletion(request, response, handler, ex);
    }
}
