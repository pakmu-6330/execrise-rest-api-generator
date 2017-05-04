package cn.dyr.rest.generator.ui.web.component;

/**
 * 邮件相关属性
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IMailComponent {

    /**
     * 通过邮件给用户发送一则项目生成完成的通知消息
     *
     * @param email       目标邮箱地址
     * @param projectName 项目名称
     */
    void sendFinishEmail(String email, String projectName);

    /**
     * 通过邮件发送一则用户验证操作的通知消息
     *
     * @param email    目标邮箱地址
     * @param username 用户名
     * @param token    验证码
     */
    void sendVerifyEmail(String email, String username, String token);
}
