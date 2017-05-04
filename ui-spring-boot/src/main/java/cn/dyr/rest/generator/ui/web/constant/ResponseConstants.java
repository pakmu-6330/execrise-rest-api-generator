package cn.dyr.rest.generator.ui.web.constant;

/**
 * 这个类当定义了 API 的返回常量
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ResponseConstants {

    /**
     * 这个表示指定的操作执行完毕
     */
    public static final int RESPONSE_OK = 0;

    /**
     * 表示必填参数缺失或者参数验证错误
     */
    public static final int BAD_PARAMETER = 1;

    /**
     * 保存或者更新数据时与原有的数据发生冲突
     */
    public static final int RESPONSE_DUPLICATE = 2;

    /**
     * 用于验证操作的验证码或者用户会话超时失效
     */
    public static final int TOKEN_EXPIRED = 3;

    /**
     * 表示权限认证失败
     */
    public static final int TOKEN_AUTH_FAILED = 4;

    /**
     * 表示指定的资源没有找到
     */
    public static final int NOT_FOUND = 5;

    /**
     * 表示操作没有执行的必要，操作没有被执行
     */
    public static final int UNNECESSARY = 6;
}
