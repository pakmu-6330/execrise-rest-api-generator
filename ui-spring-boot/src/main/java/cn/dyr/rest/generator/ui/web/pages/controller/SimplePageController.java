package cn.dyr.rest.generator.ui.web.pages.controller;

import cn.dyr.rest.generator.ui.web.common.TokenSession;
import cn.dyr.rest.generator.ui.web.common.auth.PageAuth;
import cn.dyr.rest.generator.ui.web.common.auth.exception.AuthenticationException;
import cn.dyr.rest.generator.ui.web.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 用于显示普通页面的 Controller 类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@Controller
public class SimplePageController {

    @Autowired
    private ITokenService tokenService;

    @RequestMapping(value = "/function/home", method = RequestMethod.GET)
    public String home() {
        return "home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpSession session) {
        if (session.getAttribute("tokenSession") != null) {
            return "redirect:index";
        }

        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(Map<String, Object> map, HttpSession session,
                        String username, String password) {
        if (session.getAttribute("tokenSession") != null) {
            return "direct:index";
        }

        try {
            TokenSession login = tokenService.login(username, password);
            session.setAttribute("tokenSession", login);
            return "redirect:index";
        } catch (AuthenticationException e) {
            map.put("errMsg", "用户名或者密码错误");
            return "login";
        }
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        TokenSession tokenSession = (TokenSession) session.getAttribute("tokenSession");
        if (tokenSession != null) {
            tokenService.invalidate(tokenSession);
        }

        return "redirect:index";
    }

    @PageAuth
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

}
