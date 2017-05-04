package cn.dyr.rest.generator.ui.web.util;

import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * 用于产生 token 的工具类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class TokenGenerator {

    /**
     * 产生一个随机而且不重复的 token
     *
     * @return 随机且不重复的 token
     */
    public static String tokenGenerate() {
        return DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes());
    }

}
