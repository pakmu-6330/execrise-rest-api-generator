package cn.dyr.rest.generator.ui.web.service.impl;

import cn.dyr.rest.generator.ui.web.common.TokenSession;
import cn.dyr.rest.generator.ui.web.common.auth.exception.AuthenticationException;
import cn.dyr.rest.generator.ui.web.dao.IUserDAO;
import cn.dyr.rest.generator.ui.web.entity.UserEntity;
import cn.dyr.rest.generator.ui.web.service.ITokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Token 相关的 Service 类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@Service
public class TokenService implements ITokenService {
    private static final Object lockObject;
    private static Logger logger;

    static {
        lockObject = new Object();
        logger = LoggerFactory.getLogger(TokenService.class);
    }

    private Map<String, String> usernameToToken;
    private Map<String, TokenSession> tokenSessionMap;

    public TokenService() {
        this.usernameToToken = new HashMap<>();
        this.tokenSessionMap = new HashMap<>();
    }

    @Autowired
    private IUserDAO userDAO;

    @Override
    public TokenSession login(String username, String password) {
        UserEntity userEntity = userDAO.findByUsernameAndPassword(username, password);
        if (userEntity == null) {
            throw new AuthenticationException();
        }

        TokenSession session = null;

        synchronized (lockObject) {
            String token = this.usernameToToken.remove(username);
            if (token != null) {
                this.tokenSessionMap.remove(token);
            }

            session = new TokenSession(userEntity);
            String newToken = session.getSessionId();
            this.tokenSessionMap.put(newToken, session);
        }

        logger.debug("token {} generated for {}", session.getSessionId(), session.getUserEntity().getUsername());

        return session;
    }

    @Override
    public boolean invalidate(TokenSession tokenSession) {
        if (tokenSession == null) {
            return false;
        }

        UserEntity userEntity = tokenSession.getUserEntity();
        String username = userEntity.getUsername();

        synchronized (lockObject) {
            this.tokenSessionMap.remove(tokenSession.getSessionId());
            this.usernameToToken.remove(username);
        }

        logger.debug("token {} invalidated", tokenSession.getSessionId());

        return true;
    }

    @Override
    public TokenSession getSession(String token) {
        TokenSession tokenSession = this.tokenSessionMap.get(token);
        if (tokenSession == null) {
            return null;
        }

        if (tokenSession.getExpired() < System.currentTimeMillis()) {
            invalidate(tokenSession);

            return null;
        }

        return tokenSession;
    }

    @Override
    public boolean renew(String token) {
        TokenSession tokenSession = this.tokenSessionMap.get(token);
        if (tokenSession == null) {
            return false;
        }

        long newExpired = System.currentTimeMillis() + 3600000;
        if (newExpired > tokenSession.getExpired()) {
            tokenSession.setExpired(newExpired);
        }

        return true;
    }
}
