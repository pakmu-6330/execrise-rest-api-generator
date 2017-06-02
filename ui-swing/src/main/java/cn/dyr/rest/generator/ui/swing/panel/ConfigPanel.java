package cn.dyr.rest.generator.ui.swing.panel;

import cn.dyr.rest.generator.ui.swing.SwingUIApplication;
import cn.dyr.rest.generator.ui.swing.context.ProjectContext;
import cn.dyr.rest.generator.ui.swing.control.DataPanel;
import cn.dyr.rest.generator.ui.swing.model.ProjectConfigModel;
import cn.dyr.rest.generator.ui.swing.model.ProjectModel;
import cn.dyr.rest.generator.ui.swing.model.UUIDIdentifier;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
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

        this.saveButton = new JButton("保存");
        this.saveButton.addActionListener(this);

        setLayout(new BorderLayout());

        JPanel settingPanel = new JPanel(new GridLayout(2, 1));
        settingPanel.setBorder(BorderFactory.createTitledBorder("代码生成配置"));

        JPanel tablePrefix = new JPanel(new BorderLayout());
        JLabel tablePrefixLabel = new JLabel("  表前缀：");
        tablePrefix.add(tablePrefixLabel, BorderLayout.WEST);
        tablePrefix.add(this.tablePrefix, BorderLayout.CENTER);
        settingPanel.add(tablePrefix);

        JPanel uriPrefix = new JPanel(new BorderLayout());
        JLabel uriPrefixLabel = new JLabel("URI 前缀：");
        uriPrefix.add(uriPrefixLabel, BorderLayout.WEST);
        uriPrefix.add(this.uriPrefix, BorderLayout.CENTER);
        settingPanel.add(uriPrefix);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(this.saveButton);

        this.add(settingPanel, BorderLayout.NORTH);
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
            if (checkData()) {
                doSaveData();
            }
        };
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == this.saveButton) {
            if (checkData()) {
                doSaveData();

                JOptionPane.showMessageDialog(this, "保存成功",
                        SwingUIApplication.APP_NAME, JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private boolean checkData() {
        return true;
    }

    private void doSaveData() {
        ProjectContext projectContext = SwingUIApplication.getInstance().getCurrentProjectContext();
        ProjectModel projectModel = projectContext.getProjectModel();

        ProjectConfigModel configModel = projectModel.getConfigModel();
        configModel.setTablePrefix(tablePrefix.getText());
        configModel.setUriPrefix(uriPrefix.getText());
    }

    private void refillData() {
        ProjectContext projectContext = SwingUIApplication.getInstance().getCurrentProjectContext();
        ProjectModel projectModel = projectContext.getProjectModel();

        ProjectConfigModel configModel = projectModel.getConfigModel();
        tablePrefix.setText(configModel.getTablePrefix());
        uriPrefix.setText(configModel.getUriPrefix());
    }
}
