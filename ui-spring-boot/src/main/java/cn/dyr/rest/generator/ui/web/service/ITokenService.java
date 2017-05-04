package cn.dyr.rest.generator.ui.web.service;

import cn.dyr.rest.generator.ui.web.common.TokenSession;

/**
 * Token 相关的
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface ITokenService {

    /**
     * 利用用户名和密码进行登录，返回一个基于 Token 的 Session 对象
     *
     * @param username 用户名
     * @param password 密码
     * @return 如果登录成功则返回对应的 TokenSession；否则抛出 AuthenticationException
     */
    TokenSession login(String username, String password);

    /**
     * 注销 TokenSession
     *
     * @param tokenSession 注销一个 session 对象
     * @return 如果成功注销 tokenSession，则返回 true；否则返回 false
     */
    boolean invalidate(TokenSession tokenSession);

    /**
     * 根据 Token 串获得 TokenSession 对象
     *
     * @param token token 字符串
     * @return 这个 token 对应的 TokenSession 对象
     */
    TokenSession getSession(String token);

    /**
     * 对 TokenSession 进行续约
     *
     * @param token 要进行续约的 TokenSession 对象
     * @return 如果成功续约，则返回 true；否则返回 false
     */
    boolean renew(String token);

}
