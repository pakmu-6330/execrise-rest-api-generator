package cn.dyr.rest.generator.ui.web.common;

import cn.dyr.rest.generator.ui.web.entity.UserEntity;
import cn.dyr.rest.generator.ui.web.util.TokenGenerator;

/**
 * 表示一个会话
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class TokenSession {

    private String sessionId;
    private UserEntity userEntity;
    private long expired;

    public TokenSession(UserEntity userEntity) {
        this.sessionId = TokenGenerator.tokenGenerate();
        this.userEntity = userEntity;
        this.expired = System.currentTimeMillis() + 7200000;
    }

    public TokenSession setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public TokenSession setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
        return this;
    }

    public TokenSession setExpired(long expired) {
        this.expired = expired;
        return this;
    }

    public String getSessionId() {
        return sessionId;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public long getExpired() {
        return expired;
    }
}
