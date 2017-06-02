package cn.dyr.rest.generator.ui.swing.panel;

import cn.dyr.rest.generator.ui.swing.SwingUIApplication;
import cn.dyr.rest.generator.ui.swing.context.ProjectContext;
import cn.dyr.rest.generator.ui.swing.control.DataPanel;
import cn.dyr.rest.generator.ui.swing.model.ProjectConfigModel;
import cn.dyr.rest.generator.ui.swing.model.ProjectModel;
import cn.dyr.rest.generator.ui.swing.model.UUIDIdentifier;
import cn.dyr.rest.generator.util.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

/**
 * 用于进行代码生成相关的配置
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ConfigPanel
        extends JPanel
        implements DataPanel, UUIDIdentifier, ActionListener {

    private String id;
    private boolean dirty;

    private JTextField tablePrefix;
    private JTextField uriPrefix;
    private JTextField pageSize;
    private JTextField port;

    private JButton saveButton;

    public ConfigPanel() {
        this.id = UUID.randomUUID().toString();
        this.dirty = true;

        initUI();
        refillData();
    }

    private void initUI() {
        this.tablePrefix = new JTextField();

        this.uriPrefix = new JTextField();

        this.port = new JTextField();

        this.pageSize = new JTextField();

        this.saveButton = new JButton("保存");
        this.saveButton.addActionListener(this);

        setLayout(new BorderLayout());

        // 程序行为相关配置
        JPanel behaviourPanel = new JPanel(new GridLayout(4, 1));
        behaviourPanel.setBorder(BorderFactory.createTitledBorder("程序行为相关配置"));

        JPanel tablePrefix = new JPanel(new BorderLayout());
        JLabel tablePrefixLabel = new JLabel("  表前缀：");

        this.tablePrefix.setPreferredSize(new Dimension(600, 30));

        tablePrefix.add(tablePrefixLabel, BorderLayout.WEST);
        tablePrefix.add(this.tablePrefix, BorderLayout.CENTER);
        behaviourPanel.add(tablePrefix);

        JPanel uriPrefix = new JPanel(new BorderLayout());
        JLabel uriPrefixLabel = new JLabel("URI 前缀：");
        uriPrefix.add(uriPrefixLabel, BorderLayout.WEST);
        uriPrefix.add(this.uriPrefix, BorderLayout.CENTER);
        behaviourPanel.add(uriPrefix);

        JPanel pageSizePanel = new JPanel(new BorderLayout());
        JLabel pageSizeLabel = new JLabel("分页大小：");
        pageSizePanel.add(pageSizeLabel, BorderLayout.WEST);
        pageSizePanel.add(this.pageSize, BorderLayout.CENTER);
        behaviourPanel.add(pageSizePanel);

        JPanel portPanel = new JPanel(new BorderLayout());
        JLabel portLabel = new JLabel("容器监听端口：");
        portPanel.add(portLabel, BorderLayout.WEST);
        portPanel.add(this.port, BorderLayout.CENTER);
        behaviourPanel.add(portPanel);

        // 底部按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(this.saveButton);

        this.add(behaviourPanel, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isDirty() {
        return this.dirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public Runnable saveData() {
        return () -> {
            if (checkDataQuietly()) {
                doSaveData();
            }
        };
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == this.saveButton) {
            if (checkDataWithOption()) {
                doSaveData();

                JOptionPane.showMessageDialog(this, "保存成功",
                        SwingUIApplication.APP_NAME, JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private boolean checkDataQuietly() {
        if (!StringUtils.canBeConvertedToInteger(pageSize.getText())) {
            return false;
        }

        if (!StringUtils.canBeConvertedToInteger(port.getText())) {
            return false;
        }

        int port = Integer.parseInt(this.port.getText());
        return !(port < 0 || port > 65530);
    }

    private boolean checkDataWithOption() {
        if (!StringUtils.canBeConvertedToInteger(pageSize.getText())) {
            JOptionPane.showMessageDialog(ConfigPanel.this, "分页大小必须为正整数",
                    SwingUIApplication.APP_NAME, JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!StringUtils.canBeConvertedToInteger(port.getText())) {
            JOptionPane.showMessageDialog(ConfigPanel.this, "端口号必须为正整数");
            return false;
        }

        int port = Integer.parseInt(this.port.getText());
        if (port < 0 || port > 65530) {
            JOptionPane.showMessageDialog(ConfigPanel.this,
                    "端口号的范围必须位于 0~65530 之间",
                    SwingUIApplication.APP_NAME, JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void doSaveData() {
        ProjectContext projectContext = SwingUIApplication.getInstance().getCurrentProjectContext();
        ProjectModel projectModel = projectContext.getProjectModel();

        ProjectConfigModel configModel = projectModel.getConfigModel();
        configModel.setTablePrefix(tablePrefix.getText());
        configModel.setUriPrefix(uriPrefix.getText());
        configModel.setPageSize(Integer.parseInt(pageSize.getText()));
        configModel.setPort(Integer.parseInt(port.getText()));
    }

    private void refillData() {
        ProjectContext projectContext = SwingUIApplication.getInstance().getCurrentProjectContext();
        ProjectModel projectModel = projectContext.getProjectModel();

        ProjectConfigModel configModel = projectModel.getConfigModel();
        tablePrefix.setText(configModel.getTablePrefix());
        uriPrefix.setText(configModel.getUriPrefix());
        pageSize.setText(String.valueOf(configModel.getPageSize()));
        port.setText(String.valueOf(configModel.getPort()));
    }
}
