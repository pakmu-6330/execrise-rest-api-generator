package cn.dyr.rest.generator.ui.swing.frame;

import cn.dyr.rest.generator.ui.swing.SwingUIApplication;
import cn.dyr.rest.generator.ui.swing.constant.Commands;
import cn.dyr.rest.generator.ui.swing.context.ProjectContext;
import cn.dyr.rest.generator.ui.swing.context.ProjectContextEventListener;
import cn.dyr.rest.generator.ui.swing.control.DataPanel;
import cn.dyr.rest.generator.ui.swing.control.JClosableTabbedPane;
import cn.dyr.rest.generator.ui.swing.control.TabbedPaneEventListener;
import cn.dyr.rest.generator.ui.swing.model.BasicInfoModel;
import cn.dyr.rest.generator.ui.swing.model.DBInfoModel;
import cn.dyr.rest.generator.ui.swing.model.EntityModel;
import cn.dyr.rest.generator.ui.swing.model.ProjectModel;
import cn.dyr.rest.generator.ui.swing.model.RelationshipModel;
import cn.dyr.rest.generator.ui.swing.model.UUIDIdentifier;
import cn.dyr.rest.generator.ui.swing.panel.EntityInfoPanel;
import cn.dyr.rest.generator.ui.swing.panel.GenerationPanel;
import cn.dyr.rest.generator.ui.swing.panel.HomePanel;
import cn.dyr.rest.generator.ui.swing.panel.ProjectInfoPanel;
import cn.dyr.rest.generator.ui.swing.panel.RelationshipInfoPanel;
import cn.dyr.rest.generator.ui.swing.util.MessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.bind.JAXBException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * 主窗口
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class MainWindow
        extends JFrame
        implements ActionListener, TabbedPaneEventListener, MouseListener, ProjectContextEventListener {

    private SwingUIApplication application = SwingUIApplication.getInstance();
    private static Logger logger;

    static {
        logger = LoggerFactory.getLogger(MainWindow.class);
    }

    private Map<String, String> nodeToPanelId;
    private Map<String, String> nodeToModelId;
    private Map<String, JPanel> panelIdMap;
    private Map<String, Object> modelIdMap;

    private ProjectContext projectContext;

    // <editor-fold desc="界面相关代码">

    // 菜单部分
    private JMenuBar mainMenuBar;
    private JMenu fileMenu;
    private JMenuItem createProjectMenuItem;
    private JMenuItem openProjectMenuItem;
    private JMenuItem saveProjectMenuItem;
    private JMenuItem closeProjectMenuItem;
    private JMenuItem quitMenuItem;

    // 工具栏部分
    private JToolBar mainToolBar;
    private JButton btnNewProject;
    private JButton btnOpenProject;
    private JButton btnSaveProject;
    private JButton btnNewEntity;
    private JButton btnNewRelationship;

    // 工作区区域
    private JSplitPane splitPane;
    private JTree projectTree;
    private JClosableTabbedPane tabbedPane;

    // 状态栏部分
    private JPanel statusPanel;
    private JLabel statusLabel;

    // 用于进行项目文件操作的文件选框
    private JFileChooser prjFileChooser;

    private void initComponents() {
        this.setLocationByPlatform(true);
        this.setSize(1000, 600);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(0, 0));
        this.setTitle("REST API 生成器 - 未打开工程");

        // 菜单栏
        this.mainMenuBar = new JMenuBar();
        this.setJMenuBar(this.mainMenuBar);

        // 文件菜单
        this.fileMenu = new JMenu();
        this.fileMenu.setText("文件(F)");
        this.fileMenu.setMnemonic('F');
        this.mainMenuBar.add(this.fileMenu);

        this.createProjectMenuItem = new JMenuItem();
        this.createProjectMenuItem.setText("创建工程(N)");
        this.createProjectMenuItem.setMnemonic('N');
        this.createProjectMenuItem.setActionCommand(Commands.CMD_NEW_PROJECT);
        this.fileMenu.add(this.createProjectMenuItem);

        this.fileMenu.addSeparator();

        this.openProjectMenuItem = new JMenuItem();
        this.openProjectMenuItem.setText("打开工程(O)");
        this.openProjectMenuItem.setMnemonic('O');
        this.openProjectMenuItem.setActionCommand(Commands.CMD_OPEN_PROJECT);
        this.openProjectMenuItem.addActionListener(this);
        this.fileMenu.add(this.openProjectMenuItem);

        this.saveProjectMenuItem = new JMenuItem();
        this.saveProjectMenuItem.setText("保存工程(S)");
        this.saveProjectMenuItem.setMnemonic('S');
        this.saveProjectMenuItem.setActionCommand(Commands.CMD_SAVE_PROJECT);
        this.saveProjectMenuItem.addActionListener(this);
        this.fileMenu.add(this.saveProjectMenuItem);

        this.fileMenu.addSeparator();

        this.closeProjectMenuItem = new JMenuItem();
        this.closeProjectMenuItem.setText("关闭项目(C)");
        this.closeProjectMenuItem.setMnemonic('C');
        this.closeProjectMenuItem.setActionCommand(Commands.CMD_CLOSE_PROJECT);
        // this.fileMenu.add(this.closeProjectMenuItem);

        this.quitMenuItem = new JMenuItem();
        this.quitMenuItem.setText("退出(Q)");
        this.quitMenuItem.setMnemonic('Q');
        this.quitMenuItem.setActionCommand(Commands.CMD_QUIT);
        this.fileMenu.add(this.quitMenuItem);

        // 工具栏
        this.mainToolBar = new JToolBar();
        this.add(this.mainToolBar, BorderLayout.NORTH);

        // 工具栏创建工程按钮
        this.btnNewProject = new JButton("创建工程");
        this.btnNewProject.setActionCommand(Commands.CMD_NEW_PROJECT);
        this.mainToolBar.add(this.btnNewProject);

        // 工具栏打开项目按钮
        this.btnOpenProject = new JButton("打开工程");
        this.btnOpenProject.setActionCommand(Commands.CMD_OPEN_PROJECT);
        this.btnOpenProject.addActionListener(this);
        this.mainToolBar.add(this.btnOpenProject);

        // 工具栏关闭项目按钮
        this.btnSaveProject = new JButton("保存工程");
        this.btnSaveProject.setActionCommand(Commands.CMD_SAVE_PROJECT);
        this.btnSaveProject.addActionListener(this);
        this.mainToolBar.add(this.btnSaveProject);

        this.mainToolBar.addSeparator();

        // 工具栏创建实体按钮
        this.btnNewEntity = new JButton("创建实体");
        this.btnNewEntity.setActionCommand(Commands.CMD_NEW_ENTITY);
        this.btnNewEntity.addActionListener(this);
        this.mainToolBar.add(this.btnNewEntity);

        // 工具栏创建关系按钮
        this.btnNewRelationship = new JButton("创建关系");
        this.btnNewRelationship.setActionCommand(Commands.CMD_NEW_RELATIONSHIP);
        this.btnNewRelationship.addActionListener(this);
        this.mainToolBar.add(this.btnNewRelationship);

        // 工作区
        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        this.add(this.splitPane, BorderLayout.CENTER);

        // 工作区左边的工程树图
        this.projectTree = new JTree();
        this.projectTree.addMouseListener(this);
        this.splitPane.setLeftComponent(new JScrollPane(this.projectTree));

        // 工作区右边的标签框
        this.tabbedPane = new JClosableTabbedPane();
        this.tabbedPane.setListener(this);
        this.splitPane.setRightComponent(this.tabbedPane);

        // 状态栏
        this.statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.add(this.statusPanel, BorderLayout.SOUTH);

        // 状态栏的状态消息
        this.statusLabel = new JLabel();
        this.statusPanel.add(this.statusLabel);

        this.prjFileChooser = new JFileChooser();
        this.prjFileChooser.setFileFilter(new FileNameExtensionFilter("工程文件 (*.prj)", "prj"));
        this.prjFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.prjFileChooser.setMultiSelectionEnabled(false);

        updateStatusText("系统准备就绪");
    }

    // </editor-fold>

    private void initCollections() {
        this.nodeToPanelId = new HashMap<>();
        this.nodeToModelId = new HashMap<>();

        this.panelIdMap = new HashMap<>();
        this.modelIdMap = new HashMap<>();
    }

    public MainWindow() {
        this.initComponents();

        this.updateTitle();
        this.initCollections();

        this.projectContext = application.getCurrentProjectContext();
        this.projectContext.addListener(this);

        // 窗口启动清空工程树
        DefaultTreeModel model = new DefaultTreeModel(new WithIdNode("empty project"));
        this.projectTree.setModel(model);

        // 重新绘制工程树
        this.updateModelTree();
        this.expandRoot();

        HomePanel homePanel = new HomePanel();
        this.tabbedPane.addTab("首页", homePanel);
    }

    private void expandRoot() {
        TreePath rootTreePath = new TreePath(this.projectTree.getModel().getRoot());
        this.projectTree.expandPath(rootTreePath);
    }

    private void expandEntityNode() {
        TreeNode rootNode = (TreeNode) this.projectTree.getModel().getRoot();
        TreeNode entitiesNode = rootNode.getChildAt(1);

        TreePath path = new TreePath(new Object[]{rootNode, entitiesNode});
        this.projectTree.expandPath(path);
    }

    private void expandRelationshipNode() {
        TreeNode root = (TreeNode) this.projectTree.getModel().getRoot();
        TreeNode relationshipNode = root.getChildAt(2);

        TreePath path = new TreePath(new Object[]{root, relationshipNode});
        this.projectTree.expandPath(path);
    }

    private void cleanCurrentTree(WithIdNode root) {
        initCollections();
    }

    private Object getModelFromNodeId(String id) {
        String modelId = this.nodeToModelId.get(id);
        return this.modelIdMap.get(modelId);
    }

    private JPanel getPanelFromNodeId(String id) {
        String modelId = this.nodeToPanelId.get(id);
        return this.panelIdMap.get(modelId);
    }

    private void saveNodeToModel(String nodeId, UUIDIdentifier model) {
        if (model != null && nodeId != null) {
            String modelId = model.getId();
            this.nodeToModelId.put(nodeId, modelId);
            this.modelIdMap.put(modelId, model);
        }
    }

    private void removeNodeToModel(String nodeId) {
        if (nodeId != null) {
            String modelId = this.nodeToModelId.get(nodeId);
            this.modelIdMap.remove(modelId);
        }
    }

    private void saveNodeToComponent(String nodeId, UUIDIdentifier component) {
        if (component != null && nodeId != null) {
            String componentId = component.getId();
            this.nodeToPanelId.put(nodeId, componentId);
            this.panelIdMap.put(componentId, (JPanel) component);
        }
    }

    private void removeNodeToComponent(String nodeId) {
        if (nodeId != null) {
            String panelId = this.nodeToPanelId.get(nodeId);
            this.panelIdMap.remove(panelId);
        }
    }

    private void updateModelTree() {
        ProjectModel projectModel = application.getProjectModel();
        TreeModel model = this.projectTree.getModel();

        TreeNode root = (TreeNode) model.getRoot();
        if (root != null && root instanceof WithIdNode &&
                !((WithIdNode) root).getId().equals(projectModel.getId())) {
            // 这棵树的结点已经不是与本项目对应的情况
            this.cleanCurrentTree((WithIdNode) root);

            // 创建一个新的树状模型
            WithIdNode rootNode = new WithIdNode(String.format("工程：%s", projectModel.getBasicInfo().getProjectName()));
            saveNodeToModel(rootNode.getId(), projectModel);

            model = new DefaultTreeModel(rootNode);
            this.projectTree.setModel(model);

            // 添加基本信息结点
            WithIdNode basicNode = new WithIdNode("1.基本信息");
            saveNodeToModel(basicNode.getId(), projectModel.getBasicInfo());
            rootNode.add(basicNode);

            // 添加实体信息结点
            WithIdNode entityNode = new WithIdNode("2.实体信息");
            ProjectModel.IdArrayList<EntityModel> entityList = projectModel.getEntityList();
            saveNodeToModel(entityNode.getId(), entityList);
            rootNode.add(entityNode);

            for (EntityModel entityModel : entityList) {
                WithIdNode treeNode = fromEntityModel(entityModel);
                entityNode.add(treeNode);

                saveNodeToModel(treeNode.getId(), entityModel);
            }

            // 添加关联关系信息结点
            WithIdNode relationshipNode = new WithIdNode("3.关系信息");
            ProjectModel.IdArrayList<RelationshipModel> relationshipList = projectModel.getRelationshipList();
            saveNodeToModel(relationshipNode.getId(), relationshipList);
            rootNode.add(relationshipNode);

            for (RelationshipModel relationshipModel : relationshipList) {
                WithIdNode withIdNode = fromRelationshipModel(relationshipModel);
                relationshipNode.add(withIdNode);

                saveNodeToModel(withIdNode.getId(), relationshipModel);
            }

            // 添加生成页面结点
            WithIdNode generatingNode = new WithIdNode("4.代码生成");
            DBInfoModel dbInfo = projectModel.getDbInfo();
            saveNodeToModel(generatingNode.getId(), dbInfo);

            rootNode.add(generatingNode);
        }
    }

    private void updateTitle() {
        ProjectModel projectModel = application.getProjectModel();
        if (projectModel != null) {
            BasicInfoModel basicInfo = projectModel.getBasicInfo();
            if (basicInfo != null) {
                setTitle(String.format("%s - %s", SwingUIApplication.APP_NAME, basicInfo.getProjectName()));
            }
        }
    }

    private void debugPanelAdd() {

    }

    public void display() {
        this.setVisible(true);
        this.splitPane.setDividerLocation(0.2);

        debugPanelAdd();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        switch (actionCommand) {
            case Commands.CMD_NEW_ENTITY:
                EntityInfoPanel newEntityPanel = EntityInfoPanel.newEntity();
                this.tabbedPane.addTab("新实体：" + newEntityPanel.getEntityName(), newEntityPanel);
                this.tabbedPane.setSelectedComponent(newEntityPanel);
                break;
            case Commands.CMD_NEW_RELATIONSHIP:
                if (projectContext.getEntityCount() == 0) {
                    JOptionPane.showMessageDialog(
                            this, "当前的工程当中没有实体，请先创建实体再创建关系",
                            "REST API Generator", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                RelationshipInfoPanel infoPanel = RelationshipInfoPanel.newRelationship();
                this.tabbedPane.addTab("新关系：" + infoPanel.getRelationshipName(), infoPanel);
                this.tabbedPane.setSelectedComponent(infoPanel);
                break;
            case Commands.CMD_SAVE_PROJECT:
                int result = this.prjFileChooser.showSaveDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        File targetFile = null;
                        String absolutePath = this.prjFileChooser.getSelectedFile().getAbsolutePath();
                        if (!absolutePath.toLowerCase().endsWith(".prj")) {
                            targetFile = new File(absolutePath + ".prj");
                        } else {
                            targetFile = new File(absolutePath);
                        }

                        application.saveProjectToFile(targetFile);

                        JOptionPane.showMessageDialog(this, "工程成功保存",
                                "REST API Generator", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception exception) {
                        logger.error("exception during saving project...", exception);

                        JOptionPane.showMessageDialog(this,
                                "保存工程文件时发生异常：" + exception.getMessage(),
                                "REST API Generator", JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
            case Commands.CMD_OPEN_PROJECT:
                result = this.prjFileChooser.showOpenDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = this.prjFileChooser.getSelectedFile();
                    if (selectedFile.isDirectory() || !selectedFile.exists()) {
                        JOptionPane.showMessageDialog(
                                this, "请选择有效的文件",
                                "REST API Generator", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        ProjectContext oldProjectContext = application.getCurrentProjectContext();
                        List<ProjectContextEventListener> listeners = oldProjectContext.getListeners();

                        application.loadProjectFromFile(selectedFile);

                        this.projectContext = application.getCurrentProjectContext();
                        this.projectContext.setListeners(listeners);

                        Collection<JPanel> openedPanel = this.panelIdMap.values();

                        // 清空相关数据
                        nodeToPanelId = new HashMap<>();
                        nodeToModelId = new HashMap<>();
                        panelIdMap = new HashMap<>();
                        modelIdMap = new HashMap<>();

                        EventQueue.invokeLater(() -> {
                            // 清空跟旧项目相关的标签l
                            for (JPanel panel : openedPanel) {
                                if (panel instanceof DataPanel) {
                                    ((DataPanel) panel).setDirty(false);
                                }

                                int index = tabbedPane.indexOfComponent(panel);
                                if (index != -1) {
                                    tabbedPane.removeTabAt(index);
                                }
                            }

                            updateModelTree();
                            this.projectTree.updateUI();

                            this.expandRoot();
                            this.expandEntityNode();
                            this.expandRelationshipNode();

                            // 更新标题栏
                            String title = String.format(
                                    "%s - %s", SwingUIApplication.APP_NAME,
                                    application.getProjectModel().getBasicInfo().getProjectName());
                            setTitle(title);
                        });

                    } catch (JAXBException exception) {
                        logger.error("exception occurred during loading project data", exception);
                        JOptionPane.showMessageDialog(
                                this, "打开工程时出现异常：" + exception.getMessage(),
                                "REST API Generator", JOptionPane.ERROR_MESSAGE);
                    }
                }

                break;
        }
    }

    /**
     * 变更主窗口状态栏的状态文字消息
     *
     * @param text 新的状态栏文字消息
     */
    private void updateStatusText(String text) {
        EventQueue.invokeLater(new UpdateStatusLabelText(text));
    }

    @Override
    public void onTabAdd(Component component) {

    }

    @Override
    public void onTabRemoved(Component component) {
        UUIDIdentifier identifier = null;

        if (component instanceof UUIDIdentifier) {
            identifier = (UUIDIdentifier) component;
        } else {
            return;
        }

        Set<Map.Entry<String, String>> entries = this.nodeToPanelId.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            if (entry.getValue().equals(identifier.getId())) {
                this.nodeToPanelId.remove(entry.getKey());
                this.panelIdMap.remove(entry.getValue());
                break;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Object source = e.getSource();
        if (source == this.projectTree) {
            int selectedRow = this.projectTree.getRowForLocation(e.getX(), e.getY());
            TreePath selectedNodePath = this.projectTree.getPathForLocation(e.getX(), e.getY());

            if (selectedRow != -1 && selectedNodePath != null && e.getClickCount() == 2) {
                Object[] objectsPath = selectedNodePath.getPath();

                switch (objectsPath.length) {
                    case 2: {
                        WithIdNode secondLevelTreeNode = (WithIdNode) objectsPath[1];
                        JPanel panel = this.getPanelFromNodeId(secondLevelTreeNode.getId());

                        if (panel == null) {
                            Object o = this.getModelFromNodeId(secondLevelTreeNode.getId());

                            if (o instanceof BasicInfoModel) {
                                ProjectInfoPanel infoPane = new ProjectInfoPanel();
                                this.tabbedPane.addTab("1.基本信息", infoPane);

                                saveNodeToComponent(secondLevelTreeNode.getId(), infoPane);
                                this.tabbedPane.setSelectedComponent(infoPane);
                            } else if (o instanceof DBInfoModel) {
                                GenerationPanel generationPanel = new GenerationPanel();
                                this.tabbedPane.addTab("4.代码生成", generationPanel);

                                saveNodeToComponent(secondLevelTreeNode.getId(), generationPanel);
                                this.tabbedPane.setSelectedComponent(generationPanel);
                            }
                        } else {
                            this.tabbedPane.setSelectedComponent(panel);
                        }
                    }
                    break;
                    case 3: {
                        WithIdNode thirdLevelTreeNode = (WithIdNode) objectsPath[2];
                        JPanel panel = getPanelFromNodeId(thirdLevelTreeNode.getId());
                        if (panel == null || this.tabbedPane.indexOfComponent(panel) == -1) {
                            Object o = this.getModelFromNodeId(thirdLevelTreeNode.getId());

                            // 防止内存泄露
                            if (panel != null) {
                                removeNodeToModel(thirdLevelTreeNode.getId());
                                removeNodeToComponent(thirdLevelTreeNode.getId());
                            }

                            if (o instanceof EntityModel) {
                                EntityInfoPanel entityInfoPanel = EntityInfoPanel.fromExistsEntity((EntityModel) o);

                                this.tabbedPane.addTab(String.format("2.实体：%s", ((EntityModel) o).getName()), entityInfoPanel);
                                saveNodeToComponent(thirdLevelTreeNode.getId(), entityInfoPanel);
                                saveNodeToModel(thirdLevelTreeNode.getId(), (EntityModel) o);
                                this.tabbedPane.setSelectedComponent(entityInfoPanel);
                            } else if (o instanceof RelationshipModel) {
                                RelationshipInfoPanel relationshipInfoPanel = RelationshipInfoPanel.fromExists((RelationshipModel) o);

                                this.tabbedPane.addTab(String.format("3.关系：%s", ((RelationshipModel) o).getName()), relationshipInfoPanel);
                                saveNodeToComponent(thirdLevelTreeNode.getId(), relationshipInfoPanel);
                                saveNodeToModel(thirdLevelTreeNode.getId(), (RelationshipModel) o);
                                this.tabbedPane.setSelectedComponent(relationshipInfoPanel);
                            }
                        } else {
                            this.tabbedPane.setSelectedComponent(panel);
                        }
                    }
                    break;
                }
            }
        }
    }

    private JPopupMenu createEntityContextMenu(String nodeId) {
        JPopupMenu retValue = new JPopupMenu();

        // 打开实体
        JMenuItem openMenuItem = new JMenuItem("打开");
        openMenuItem.setMnemonic('O');

        retValue.add(openMenuItem);

        // 删除实体
        JMenuItem deleteMenuItem = new JMenuItem("删除");
        deleteMenuItem.setMnemonic('D');
        deleteMenuItem.addActionListener(new EntityDeletePopMenuAction(nodeId));

        retValue.add(deleteMenuItem);

        return retValue;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Object source = e.getSource();
        if (source == this.projectTree && e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3) {
            int selectedRow = this.projectTree.getRowForLocation(e.getX(), e.getY());
            TreePath selectedNodePath = this.projectTree.getPathForLocation(e.getX(), e.getY());

            // 根据选中的结点类型弹出不同的上下文菜单
            if (selectedRow != -1 && selectedNodePath != null) {
                Object[] pathNodes = selectedNodePath.getPath();

                if (pathNodes.length == 3) {
                    WithIdNode targetNode = (WithIdNode) pathNodes[2];
                    Object model = this.getModelFromNodeId(targetNode.getId());

                    if (model instanceof EntityModel) {
                        JPopupMenu contextMenu = createEntityContextMenu(targetNode.getId());
                        contextMenu.show(this.projectTree, e.getX(), e.getY());
                    } else if (model instanceof RelationshipModel) {
                        System.out.println("右键单击了关系实体");
                    }
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private WithIdNode fromEntityModel(EntityModel entityModel) {
        return new WithIdNode(String.format("实体：%s", entityModel.getName()));
    }

    private WithIdNode fromRelationshipModel(RelationshipModel relationshipModel) {
        return new WithIdNode(String.format("关系：%s", relationshipModel.getName()));
    }

    @Override
    public void onBasicInfoChange(BasicInfoModel basicInfoModel, Component component) {
        // 1. 需要变更工程树上面的工程名
        EventQueue.invokeLater(() -> {
            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) projectTree.getModel().getRoot();
            ProjectModel model = projectContext.getProjectModel();

            rootNode.setUserObject(String.format("工程：%s", model.getBasicInfo().getProjectName()));
            projectTree.updateUI();
        });
    }

    @Override
    public void onDBInfoChange(DBInfoModel dbInfoModel, Component component) {

    }

    @Override
    public void onEntityAdded(EntityModel entityModel, Component component) {
        TreeNode projectTreeNode = (TreeNode) this.projectTree.getModel().getRoot();

        // 直接操作实体类的结点
        TreeNode entitiesNode = projectTreeNode.getChildAt(1);

        // 创建一个对应的结点，并保存与实体之间的关联关系
        WithIdNode treeNode = fromEntityModel(entityModel);
        ((DefaultMutableTreeNode) entitiesNode).add(treeNode);

        if (component != null) {
            saveNodeToComponent(treeNode.getId(), (UUIDIdentifier) component);
        }

        saveNodeToModel(treeNode.getId(), entityModel);

        // 更新工程树
        EventQueue.invokeLater(() -> {
            projectTree.updateUI();

            TreeNode rootNode = (TreeNode) projectTree.getModel().getRoot();
            TreeNode entityRootNode = rootNode.getChildAt(1);
            TreePath path = new TreePath(new Object[]{rootNode, entityRootNode});

            projectTree.expandPath(path);
        });

        // 变更顶部标签标题
        if (component != null) {
            EventQueue.invokeLater(() -> {
                int index = tabbedPane.indexOfComponent(component);
                if (index != -1) {
                    tabbedPane.setTitleAt(index, String.format("实体：%s", entityModel.getName()));
                }
            });
        }
    }

    @Override
    public void onEntityUpdated(EntityModel oldEntityModel, EntityModel newEntityModel, Component component) {
        String entityModelId = newEntityModel.getId();

        // 寻找到树结点信息
        TreeNode root = (TreeNode) projectTree.getModel().getRoot();
        TreeNode entitiesNode = root.getChildAt(1);
        for (int i = 0; i < entitiesNode.getChildCount(); i++) {
            WithIdNode entityNode = (WithIdNode) entitiesNode.getChildAt(i);

            if (this.nodeToModelId.get(entityNode.getId()).equals(entityModelId)) {
                entityNode.setUserObject(String.format("实体：%s", newEntityModel.getName()));

                EventQueue.invokeLater(() -> projectTree.updateUI());
                break;
            }
        }

        // 变更标题信息
        EventQueue.invokeLater(() -> {
            int index = tabbedPane.indexOfComponent(component);
            if (index != -1) {
                tabbedPane.setTitleAt(index, String.format("实体：%s", newEntityModel.getName()));
            }
        });
    }

    @Override
    public void onEntityDeleted(EntityModel entityModel, Component component) {
        // 1. 根据实体的唯一标识删除工程树上面的结点
        String entityModelId = entityModel.getId();
        String nodeId;

        DefaultTreeModel model = (DefaultTreeModel) projectTree.getModel();
        TreeNode root = (TreeNode) model.getRoot();
        DefaultMutableTreeNode entitiesNode = (DefaultMutableTreeNode) root.getChildAt(1);
        nodeId = removeNodeByModelId(entityModelId, (WithIdNode) entitiesNode);

        // 2. 如果实体对应的页面已经在窗口打开，在关闭之
        JPanel panel = getPanelFromNodeId(nodeId);
        if (panel != null) {
            int index = this.tabbedPane.indexOfComponent(panel);
            if (index != -1) {
                EventQueue.invokeLater(() -> tabbedPane.removeTabAt(index));
            }
        }

        // 3. 清除关联关系数据
        removeNodeToComponent(nodeId);
        removeNodeToModel(nodeId);
    }

    @Override
    public void onRelationshipAdded(RelationshipModel relationshipModel, Component component) {
        DefaultTreeModel model = (DefaultTreeModel) this.projectTree.getModel();
        WithIdNode rootNode = (WithIdNode) model.getRoot();
        WithIdNode relationshipRootNode = (WithIdNode) rootNode.getChildAt(2);

        // 1. 创建一个树结点
        WithIdNode relationshipNode = fromRelationshipModel(relationshipModel);
        relationshipRootNode.add(relationshipNode);

        // 2. 添加到工程树上
        if (component != null) {
            saveNodeToComponent(relationshipNode.getId(), (UUIDIdentifier) component);
        }

        saveNodeToModel(relationshipNode.getId(), relationshipModel);

        // 3. 刷新工程树
        EventQueue.invokeLater(() -> {
            projectTree.updateUI();

            TreePath path = new TreePath(new Object[]{rootNode, relationshipRootNode});
            projectTree.expandPath(path);
        });

        // 4. 变更信息页的标题
        if (component != null) {
            EventQueue.invokeLater(() -> {
                int index = tabbedPane.indexOfComponent(component);
                if (index != -1) {
                    tabbedPane.setTitleAt(index, String.format("关系：%s", relationshipModel.getName()));
                }
            });
        }
    }

    @Override
    public void onRelationshipUpdated(RelationshipModel oldRelationship, RelationshipModel newRelationship, Component component) {
        String relationshipModelId = newRelationship.getId();

        // 寻找工程树的根节点
        TreeNode treeRoot = (TreeNode) projectTree.getModel().getRoot();
        TreeNode relationshipNodes = treeRoot.getChildAt(2);
        for (int i = 0; i < relationshipNodes.getChildCount(); i++) {
            WithIdNode relationshipNode = (WithIdNode) relationshipNodes.getChildAt(i);

            if (this.nodeToModelId.get(relationshipNode.getId()).equals(relationshipModelId)) {
                relationshipNode.setUserObject(String.format("关系：%s", newRelationship.getName()));

                EventQueue.invokeLater(() -> projectTree.updateUI());
                break;
            }
        }

        // 变更标题信息
        EventQueue.invokeLater(() -> {
            int index = tabbedPane.indexOfComponent(component);
            if (index != -1) {
                tabbedPane.setTitleAt(index, String.format("关系：%s", newRelationship.getName()));
            }
        });
    }

    private String removeNodeByModelId(String modelId, WithIdNode parentNode) {
        DefaultTreeModel model = (DefaultTreeModel) projectTree.getModel();

        String retValue = "";

        for (int i = 0; i < parentNode.getChildCount(); i++) {
            WithIdNode node = (WithIdNode) parentNode.getChildAt(i);
            if (modelId.equals(this.nodeToModelId.get(node.getId()))) {
                retValue = node.getId();
                model.removeNodeFromParent(node);
                break;
            }
        }

        return retValue;
    }

    @Override
    public void onRelationshipDeleted(RelationshipModel relationshipModel, Component component) {
        // 1. 根据关联关系的唯一标识删除工程树上的结点
        String relationshipModelId = relationshipModel.getId();
        String nodeId;

        DefaultTreeModel model = (DefaultTreeModel) projectTree.getModel();
        TreeNode root = (TreeNode) model.getRoot();
        WithIdNode relationshipNode = (WithIdNode) root.getChildAt(2);
        nodeId = removeNodeByModelId(relationshipModelId, relationshipNode);

        // 2. 如果对应的窗口已经打开，关闭
        JPanel panel = getPanelFromNodeId(nodeId);
        if (panel != null) {
            int index = this.tabbedPane.indexOfComponent(panel);
            if (index != -1) {
                EventQueue.invokeLater(() -> tabbedPane.removeTabAt(index));
            }
        }

        // 3. 清除相应的数据
        removeNodeToComponent(nodeId);
        removeNodeToModel(nodeId);
    }

    /**
     * 执行实体信息的删除操作，用于工程树的右键单击菜单
     *
     * @param nodeId 工程树中结点的 id
     */
    private void deleteEntityByNodeId(String nodeId) {
        EntityModel entityModel = null;
        Object rawModel = this.getModelFromNodeId(nodeId);
        if (rawModel instanceof EntityModel) {
            entityModel = (EntityModel) rawModel;
        }

        if (entityModel == null) {
            return;
        }

        List<RelationshipModel> handled = projectContext.getRelationshipListEntityHandled(entityModel);
        if (handled != null && handled.size() > 0) {
            String msg = MessageBuilder.entityDeletedDueToHandled(handled);
            JOptionPane.showMessageDialog(this, msg, SwingUIApplication.APP_NAME, JOptionPane.ERROR_MESSAGE);
            return;
        }
        int result = JOptionPane.showConfirmDialog(
                this, "您确定要删除这个实体吗？该操作不可恢复！",
                SwingUIApplication.APP_NAME, JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            List<RelationshipModel> handles = projectContext.getRelationshipListEntityHandles(entityModel);
            if (handles != null && handles.size() > 0) {
                String msg = MessageBuilder.confirmMsgForHandles(handles);
                result = JOptionPane.showConfirmDialog(
                        this, msg, SwingUIApplication.APP_NAME,
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    projectContext.deleteEntity(entityModel, this.getPanelFromNodeId(nodeId));
                }
            } else {
                projectContext.deleteEntity(entityModel, this.getPanelFromNodeId(nodeId));
            }
        }
    }

    private final class EntityDeletePopMenuAction implements ActionListener {

        private String nodeId;

        EntityDeletePopMenuAction(String nodeId) {
            Objects.requireNonNull(nodeId);

            this.nodeId = nodeId;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            deleteEntityByNodeId(nodeId);
        }
    }

    /**
     * 根据模型对象寻找到对应的面板
     *
     * @param model 模型对象
     * @return 如果找到这个模型对应的面板，则返回这个面板对象；否则返回 null
     */
    private JPanel findPanelByModel(UUIDIdentifier model) {
        String nodeId = null;

        Set<Map.Entry<String, String>> entries = this.nodeToModelId.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            if (entry.getValue().equals(model.getId())) {
                nodeId = entry.getKey();
                break;
            }
        }

        if (nodeId == null) {
            return null;
        }
        return this.panelIdMap.get(this.nodeToPanelId.get(nodeId));
    }

    private class WithIdNode extends DefaultMutableTreeNode {

        private String id;

        public WithIdNode() {
            super();
            id = UUID.randomUUID().toString();
        }

        WithIdNode(Object userObject) {
            super(userObject);

            id = UUID.randomUUID().toString();
        }

        public WithIdNode(Object userObject, boolean allowsChildren) {
            super(userObject, allowsChildren);

            id = UUID.randomUUID().toString();
        }

        public String getId() {
            return id;
        }
    }

    private final class UpdateStatusLabelText extends Thread {
        private String text;

        UpdateStatusLabelText(String text) {
            this.text = text;
        }

        @Override
        public void run() {
            statusLabel.setText(text);
        }
    }
}
