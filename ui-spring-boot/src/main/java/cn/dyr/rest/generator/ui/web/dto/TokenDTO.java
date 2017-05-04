package cn.dyr.rest.generator.ui.web.dto;

import cn.dyr.rest.generator.ui.web.common.TokenSession;

/**
 * TokenSession 对应的 DTO 对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class TokenDTO {

    private String token;
    private long expired;
    private UserDTO user;

    /**
     * 从 TokenSession 对象转化为相应的 DTO 对象
     *
     * @param tokenSession TokenSession 对象
     * @return 对应的 DTO 对象
     */
    public static TokenDTO fromTokenSession(TokenSession tokenSession) {
        TokenDTO retValue = new TokenDTO();

        retValue.setToken(tokenSession.getSessionId());
        retValue.setExpired(tokenSession.getExpired());
        retValue.setUser(UserDTO.fromUserEntity(tokenSession.getUserEntity()));

        return retValue;
    }

    public String getToken() {
        return token;
    }

    public TokenDTO setToken(String token) {
        this.token = token;
        return this;
    }

    public long getExpired() {
        return expired;
    }

    public TokenDTO setExpired(long expired) {
        this.expired = expired;
        return this;
    }

    public UserDTO getUser() {
        return user;
    }

    public TokenDTO setUser(UserDTO user) {
        this.user = user;
        return this;
    }
}
