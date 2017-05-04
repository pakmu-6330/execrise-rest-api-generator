package cn.dyr.rest.generator.ui.swing.panel;

import cn.dyr.rest.generator.ui.swing.model.UUIDIdentifier;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.UUID;

/**
 * 首页
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class HomePanel extends JPanel implements UUIDIdentifier {

    private String id;

    public HomePanel() {
        super(new BorderLayout());

        JLabel tipWord = new JLabel("   <- 从左边导航栏开始");
        this.add(tipWord, BorderLayout.CENTER);

        this.id = UUID.randomUUID().toString();
    }

    @Override
    public String getId() {
        return this.id;
    }
}
