package cn.dyr.rest.generator.ui.web.controller;

import cn.dyr.rest.generator.ui.web.common.APIResponse;
import cn.dyr.rest.generator.ui.web.common.ResponseMeta;
import cn.dyr.rest.generator.ui.web.common.TokenSession;
import cn.dyr.rest.generator.ui.web.common.auth.AuthMethod;
import cn.dyr.rest.generator.ui.web.common.auth.AuthType;
import cn.dyr.rest.generator.ui.web.common.auth.FromToken;
import cn.dyr.rest.generator.ui.web.common.factory.ResponseMetaFactory;
import cn.dyr.rest.generator.ui.web.dto.TokenDTO;
import cn.dyr.rest.generator.ui.web.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于 token 相关管理的控制器，主要是用于登录注销等操作
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private ITokenService tokenService;


    public HttpEntity<APIResponse<Map<String, Object>>> test() {
        return null;
    }

    @PostMapping("/new")
    @AuthMethod(AuthType.NOT_VERIFIED)
    public HttpEntity<APIResponse<TokenDTO>> renew(
            @FromToken TokenSession tokenSession
    ) {
        long oldExpired = tokenSession.getExpired();
        this.tokenService.renew(tokenSession.getSessionId());
        long newExpired = tokenSession.getExpired();

        TokenDTO tokenDTO = TokenDTO.fromTokenSession(tokenSession);
        ResponseMeta meta;

        if (newExpired == oldExpired) {
            meta = ResponseMetaFactory.unnecessary();
        } else {
            meta = ResponseMetaFactory.okMeta();
        }

        return new ResponseEntity<>(new APIResponse<>(meta, tokenDTO), HttpStatus.OK);
    }

    @PostMapping("")
    public HttpEntity<APIResponse<TokenDTO>> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        TokenSession session = tokenService.login(username, password);
        TokenDTO tokenDTO = TokenDTO.fromTokenSession(session);

        return new ResponseEntity<>(new APIResponse<>(ResponseMetaFactory.okMeta(), tokenDTO), HttpStatus.OK);
    }

    @DeleteMapping("")
    public HttpEntity<APIResponse<HashMap<String, Object>>> logout(
            @FromToken TokenSession session
    ) {
        if (session == null) {
            ResponseMeta meta = ResponseMetaFactory.notFound();
            return new ResponseEntity<>(new APIResponse<>(meta, new HashMap<String, Object>()), HttpStatus.NOT_FOUND);
        }

        boolean result = tokenService.invalidate(session);
        if (result) {
            ResponseMeta meta = ResponseMetaFactory.okMeta();
            return new ResponseEntity<>(new APIResponse<>(meta, new HashMap<String, Object>()), HttpStatus.OK);
        } else {
            ResponseMeta meta = ResponseMetaFactory.notFound();
            return new ResponseEntity<>(new APIResponse<>(meta, new HashMap<String, Object>()), HttpStatus.NOT_FOUND);
        }
    }

}
