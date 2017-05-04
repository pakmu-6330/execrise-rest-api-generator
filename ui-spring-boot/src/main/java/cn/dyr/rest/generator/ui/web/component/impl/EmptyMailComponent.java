package cn.dyr.rest.generator.ui.web.component.impl;

import cn.dyr.rest.generator.ui.web.component.IMailComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 不会执行任何操作的邮箱组件
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@Component
public class EmptyMailComponent implements IMailComponent {

    private static Logger logger;

    static {
        logger = LoggerFactory.getLogger(EmptyMailComponent.class);
    }

    @Override
    public void sendFinishEmail(String email, String projectName) {
        logger.warn("empty email component, sendFinishEmail({}, {}) will be ignored", email, projectName);
    }

    @Override
    public void sendVerifyEmail(String email, String username, String token) {
        logger.warn("empty email component, sendVerifyMail({}, {}, {}) will be ignored", email, username, token);
    }
}
