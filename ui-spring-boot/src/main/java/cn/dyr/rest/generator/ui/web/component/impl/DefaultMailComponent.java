package cn.dyr.rest.generator.ui.web.component.impl;

import cn.dyr.rest.generator.ui.web.component.IMailComponent;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认的邮件实现类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DefaultMailComponent implements IMailComponent {

    private static Logger logger;

    static {
        logger = LoggerFactory.getLogger(DefaultMailComponent.class);
    }

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Override
    public void sendFinishEmail(String email, String projectName) {

    }

    @Override
    public void sendVerifyEmail(String email, String username, String token) {
        Map<String, Object> allParameters = new HashMap<>();
        allParameters.put("username", username);
        allParameters.put("token", token);
        String body = getBody("verify-code.ftl", allParameters);

        sendBody(email, body, "验证码");
    }

    private void sendBody(String target, String body, String subject) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("alexdd9593@126.com");
            helper.setTo(target);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("during created message...", e);
        }
    }

    private String getBody(String templateName, Map<String, Object> attrs) {
        String body = "";

        try {
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templateName);
            body = FreeMarkerTemplateUtils.processTemplateIntoString(template, attrs);
        } catch (IOException | TemplateException e) {
            logger.error("during merging body...", e);
            throw new RuntimeException(e);
        }

        return body;
    }
}
