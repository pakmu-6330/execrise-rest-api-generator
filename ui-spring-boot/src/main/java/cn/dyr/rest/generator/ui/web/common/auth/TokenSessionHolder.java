package cn.dyr.rest.generator.ui.web.common.auth;

import cn.dyr.rest.generator.ui.web.common.TokenSession;

/**
 * 将当前的 TokenSession 绑定到线程上下文当中，供组件获得会话数据
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class TokenSessionHolder {

    private static final TokenSessionHolder INSTANCE = new TokenSessionHolder();

    private ThreadLocal<TokenSession> threadLocal;

    private TokenSessionHolder() {
        this.threadLocal = new ThreadLocal<>();
    }

    public static TokenSessionHolder getInstance() {
        return INSTANCE;
    }

    public void setTokenSession(TokenSession tokenSession) {
        threadLocal.set(tokenSession);
    }

    public TokenSession getTokenSession() {
        return threadLocal.get();
    }

    public void removeTokenSession() {
        threadLocal.remove();
    }
}
