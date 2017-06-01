package cn.dyr.rest.generator.ui.swing.panel;

import cn.dyr.rest.generator.ui.swing.SwingUIApplication;
import cn.dyr.rest.generator.ui.swing.constant.Commands;
import cn.dyr.rest.generator.ui.swing.constant.UIConstant;
import cn.dyr.rest.generator.ui.swing.context.ProjectContext;
import cn.dyr.rest.generator.ui.swing.control.DataPanel;
import cn.dyr.rest.generator.ui.swing.model.BasicInfoModel;
import cn.dyr.rest.generator.ui.swing.model.DBInfoModel;
import cn.dyr.rest.generator.ui.swing.model.ProjectModel;
import cn.dyr.rest.generator.ui.swing.model.UUIDIdentifier;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.UUID;

/**
 * 项目信息的面板
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ProjectInfoPanel extends JPanel
        implements DataPanel, ActionListener, UUIDIdentifier, MouseListener {

    private String id;
    private boolean dirty;

    // <editor-fold> desc="界面相关"
    private JPanel projectInfoPanel;
    private JPanel databaseInfoPanel;

    private JPanel workPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;

    private JButton saveButton;
    private JButton passwordSwitchButton;

    private JTextField developerName;
    private JTextField projectName;
    private JTextField version;
    private JTextField packageName;

    private JComboBox<String> dbType;
    private JTextField dbHost;
    private JTextField dbName;
    private JTextField dbUsername;
    private JPasswordField dbPassword;

    private void initComponents() {
        this.setLayout(new BorderLayout());

        // 主面板和上下的导航面板
        this.workPanel = new JPanel(new GridLayout(2, 1));

        this.topPanel = new JPanel(new BorderLayout());

        this.bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        this.saveButton = new JButton("保存");
        this.saveButton.setActionCommand(Commands.CMD_SAVE_PROJECT_INFO);
        this.saveButton.addActionListener(this);

        this.bottomPanel.add(this.saveButton);

        // 项目基本信息的面板
        this.projectInfoPanel = new JPanel(new GridLayout(4, 1));
        this.projectInfoPanel.setBorder(BorderFactory.createTitledBorder("项目信息"));

        // 作者的标签
        JLabel lbDeveloper = new JLabel("作者：");

        // 作者的文本框
        this.developerName = new JTextField();

        // 作者的面板
        JPanel developerPanel = new JPanel(new BorderLayout());
        developerPanel.add(lbDeveloper, BorderLayout.WEST);
        developerPanel.add(this.developerName, BorderLayout.CENTER);
        this.projectInfoPanel.add(developerPanel);

        // 工程名称
        JLabel lbProject = new JLabel("名称：");

        // 工程名称文本框
        this.projectName = new JTextField();

        // 工程名称的面板
        JPanel ProjectNamePanel = new JPanel(new BorderLayout());
        ProjectNamePanel.add(lbProject, BorderLayout.WEST);
        ProjectNamePanel.add(this.projectName, BorderLayout.CENTER);
        this.projectInfoPanel.add(ProjectNamePanel);

        // 版本
        JLabel lbVersion = new JLabel("版本：");

        // 版本文本框
        this.version = new JTextField();

        // 版本信息面板
        JPanel versionPanel = new JPanel(new BorderLayout());
        versionPanel.add(lbVersion, BorderLayout.WEST);
        versionPanel.add(this.version, BorderLayout.CENTER);
        this.projectInfoPanel.add(versionPanel);

        // 包名
        JLabel basePackage = new JLabel("包名：");

        // 包名文本框
        this.packageName = new JTextField();

        // 包名信息面板
        JPanel packageNameInfoPanel = new JPanel(new BorderLayout());
        packageNameInfoPanel.add(basePackage, BorderLayout.WEST);
        packageNameInfoPanel.add(this.packageName, BorderLayout.CENTER);
        this.projectInfoPanel.add(packageNameInfoPanel);

        this.workPanel.add(this.projectInfoPanel);

        // 数据库信息的面板
        this.databaseInfoPanel = new JPanel(new GridLayout(5, 2));
        this.databaseInfoPanel.setBorder(BorderFactory.createTitledBorder("数据库信息"));

        // 数据库类型
        this.dbType = new JComboBox<>();
        UIConstant.SUPPORTED_DB_LIST.forEach(s -> dbType.addItem(s));

        JLabel lbDbType = new JLabel("数据库类型：");

        JPanel dbTypePanel = new JPanel(new BorderLayout());
        dbTypePanel.add(lbDbType, BorderLayout.WEST);
        dbTypePanel.add(this.dbType, BorderLayout.CENTER);
        this.databaseInfoPanel.add(dbTypePanel);

        // 数据库位置
        this.dbHost = new JTextField();

        JLabel lbDbHost = new JLabel("数据库地址：");

        JPanel dbHostPanel = new JPanel(new BorderLayout());
        dbHostPanel.add(lbDbHost, BorderLayout.WEST);
        dbHostPanel.add(this.dbHost, BorderLayout.CENTER);
        this.databaseInfoPanel.add(dbHostPanel);

        // 数据库名称
        this.dbName = new JTextField();

        JLabel lbDbName = new JLabel("数据库名称：");

        JPanel dbNamePanel = new JPanel(new BorderLayout());
        dbNamePanel.add(lbDbName, BorderLayout.WEST);
        dbNamePanel.add(this.dbName, BorderLayout.CENTER);
        this.databaseInfoPanel.add(dbNamePanel);

        // 数据库账号
        this.dbUsername = new JTextField();

        JLabel lbUsername = new JLabel("数据库账号：");

        JPanel dbUsernamePanel = new JPanel(new BorderLayout());
        dbUsernamePanel.add(lbUsername, BorderLayout.WEST);
        dbUsernamePanel.add(this.dbUsername, BorderLayout.CENTER);
        this.databaseInfoPanel.add(dbUsernamePanel);

        // 数据库密码
        this.dbPassword = new JPasswordField();
        this.passwordSwitchButton = new JButton("查看密码");
        this.passwordSwitchButton.addMouseListener(this);

        JLabel lbPassword = new JLabel("数据库密码：");

        JPanel dbPasswordPanel = new JPanel(new BorderLayout());
        dbPasswordPanel.add(lbPassword, BorderLayout.WEST);
        dbPasswordPanel.add(this.dbPassword, BorderLayout.CENTER);
        dbPasswordPanel.add(this.passwordSwitchButton, BorderLayout.EAST);

        this.databaseInfoPanel.add(dbPasswordPanel);

        this.workPanel.add(this.databaseInfoPanel);

        // 添加到主要的面板当中
        this.add(this.topPanel, BorderLayout.NORTH);
        this.add(this.workPanel, BorderLayout.CENTER);
        this.add(this.bottomPanel, BorderLayout.SOUTH);
    }

    // </editor-fold>

    public ProjectInfoPanel() {
        super(new GridLayout(2, 1));

        this.initComponents();

        this.fillData();

        this.id = UUID.randomUUID().toString();
    }

    private void fillData() {
        ProjectModel projectModel = SwingUIApplication.getInstance().getProjectModel();
        BasicInfoModel basicInfo = projectModel.getBasicInfo();
        DBInfoModel dbInfo = projectModel.getDbInfo();

        developerName.setText(basicInfo.getAuthorName());
        projectName.setText(basicInfo.getProjectName());
        version.setText(basicInfo.getVersion());
        packageName.setText(basicInfo.getPackageName());

        dbType.setSelectedItem(dbInfo.getDbType());
        dbName.setText(dbInfo.getDbName());
        dbUsername.setText(dbInfo.getDbUsername());
        dbPassword.setText(dbInfo.getDbPassword());
        dbHost.setText(dbInfo.getDbHost());
    }

    private BasicInfoModel fromBasicData() {
        BasicInfoModel basicInfoModel = new BasicInfoModel();
        basicInfoModel.setAuthorName(developerName.getText());
        basicInfoModel.setProjectName(projectName.getText());
        basicInfoModel.setVersion(version.getText());
        basicInfoModel.setPackageName(packageName.getText());

        return basicInfoModel;
    }

    public DBInfoModel fromDBInfo() {
        DBInfoModel dbInfoModel = new DBInfoModel();
        dbInfoModel.setDbType(dbType.getSelectedItem().toString());
        dbInfoModel.setDbUsername(dbUsername.getText());
        dbInfoModel.setDbName(dbName.getText());
        dbInfoModel.setDbPassword(new String(dbPassword.getPassword()));
        dbInfoModel.setDbHost(dbHost.getText());

        return dbInfoModel;
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
            ProjectContext projectContext = SwingUIApplication.getInstance().getCurrentProjectContext();
            projectContext.setBasicInfo(fromBasicData(), ProjectInfoPanel.this);
            projectContext.setDBInfo(fromDBInfo(), ProjectInfoPanel.this);
        };
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case Commands.CMD_SAVE_PROJECT_INFO:
                SwingUIApplication.getInstance().runBackground(saveData());
                break;
        }
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Object source = e.getSource();
        if (source == this.passwordSwitchButton) {
            dbPassword.setEchoChar('\0');
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Object source = e.getSource();
        if (source == this.passwordSwitchButton) {
            dbPassword.setEchoChar('*');
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
