package cn.dyr.rest.generator.ui.web.component.impl;

import cn.dyr.rest.generator.ui.web.MainApplication;
import cn.dyr.rest.generator.ui.web.component.IMailComponent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 邮件组件的发送测试
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class DefaultMailComponentTest {

    @Autowired
    private IMailComponent mailComponent;

    @Test
    public void sendFinishEmail() throws Exception {
        mailComponent.sendFinishEmail("dengyurong6330@163.com", "xxx");
    }

}