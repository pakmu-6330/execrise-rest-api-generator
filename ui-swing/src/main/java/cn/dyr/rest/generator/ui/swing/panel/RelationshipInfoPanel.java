package cn.dyr.rest.generator.ui.swing.panel;

import cn.dyr.rest.generator.ui.swing.SwingUIApplication;
import cn.dyr.rest.generator.ui.swing.context.ProjectContext;
import cn.dyr.rest.generator.ui.swing.control.DataPanel;
import cn.dyr.rest.generator.ui.swing.control.JClosableTabbedPane;
import cn.dyr.rest.generator.ui.swing.model.EntityModel;
import cn.dyr.rest.generator.ui.swing.model.RelationshipModel;
import cn.dyr.rest.generator.ui.swing.model.UUIDIdentifier;
import cn.dyr.rest.generator.ui.swing.util.IdGenerator;
import cn.dyr.rest.generator.util.StringUtils;
import net.oschina.util.Inflector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.UUID;

/**
 * 关联关系信息的面板
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class RelationshipInfoPanel
        extends JPanel
        implements UUIDIdentifier, DataPanel, ChangeListener, ActionListener {

    private static final String ONE = "一个";

    private static final String MANY = "多个";

    private static final String VERB = "关联";

    private String id;
    private boolean newRelationship;
    private boolean dirty;

    private ProjectContext projectContext;
    private RelationshipModel relationshipModel;

    // <editor-fold desc="界面相关代码">

    private JPanel mainPanel;
    private JPanel entityChooserPanel;

    private JComboBox<String> entityANameComboBox;
    private JComboBox<String> entityBNameComboBox;

    private JRadioButton entityAHandle;
    private JRadioButton entityBHandle;
    private ButtonGroup handlerButtonGroup;

    private JTextField relationshipName;

    private JRadioButton typeOneToOne;
    private JRadioButton typeOneToMany;
    private JRadioButton typeManyToOne;
    private JRadioButton typeManyToMany;
    private ButtonGroup typeButtonGroup;

    private JButton btnSaveButton;
    private JButton btnDeleteRelationship;

    private JLabel descAtoB;
    private JLabel descBtoA;

    private JCheckBox directionAToB;
    private JTextField endAAttributeName;
    private JLabel endAAttributeType;

    private JCheckBox directionBToA;
    private JTextField endBAttributeName;
    private JLabel endBAttributeType;

    private void initComponents() {
        this.setLayout(new BorderLayout());

        // 顶部的双方实体选择面板
        this.entityChooserPanel = new JPanel(new GridLayout(1, 2));

        this.entityANameComboBox = new JComboBox<>();
        this.entityANameComboBox.setEditable(false);
        this.entityANameComboBox.addActionListener(this);

        this.entityBNameComboBox = new JComboBox<>();
        this.entityBNameComboBox.setEditable(false);
        this.entityBNameComboBox.addActionListener(this);

        this.entityAHandle = new JRadioButton("该端维护关联关系");
        this.entityBHandle = new JRadioButton("该端维护关联关系");

        this.handlerButtonGroup = new ButtonGroup();
        this.handlerButtonGroup.add(this.entityAHandle);
        this.handlerButtonGroup.add(this.entityBHandle);

        JPanel entityAChooserPanel = new JPanel(new GridLayout(3, 1));
        JPanel entityBChooserPanel = new JPanel(new GridLayout(3, 1));

        JLabel lbEntityA = new JLabel("关联关系 A 端：");
        JLabel lbEntityB = new JLabel("关联关系 B 端：");

        entityAChooserPanel.add(lbEntityA);
        entityAChooserPanel.add(this.entityANameComboBox);
        entityAChooserPanel.add(this.entityAHandle);

        entityBChooserPanel.add(lbEntityB);
        entityBChooserPanel.add(this.entityBNameComboBox);
        entityBChooserPanel.add(this.entityBHandle);

        this.entityChooserPanel.setBorder(BorderFactory.createTitledBorder("关联关系双方信息"));
        this.entityChooserPanel.add(entityAChooserPanel);
        this.entityChooserPanel.add(entityBChooserPanel);

        // 中间的关系配置
        JPanel middleCenterPanel = new JPanel(new BorderLayout());

        // 关联关系名称的面板
        JPanel namePanel = new JPanel(new BorderLayout());

        this.relationshipName = new JTextField();
        namePanel.add(this.relationshipName, BorderLayout.CENTER);

        JLabel lbName = new JLabel("关联关系名称：");
        namePanel.add(lbName, BorderLayout.WEST);

        // 描述关联关系信息的面板
        JPanel descPanel = new JPanel(new BorderLayout());

        this.descAtoB = new JLabel("描述 A 端到 B 端的关联关系");
        this.descBtoA = new JLabel("描述 B 端到 A 端的关联关系");

        descPanel.add(this.descAtoB, BorderLayout.NORTH);
        descPanel.add(this.descBtoA, BorderLayout.SOUTH);

        // 用于配置关联关系类型的面板
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        this.typeOneToOne = new JRadioButton("一对一");
        this.typeOneToOne.setSelected(true);
        this.typeOneToOne.addChangeListener(this);
        typePanel.add(this.typeOneToOne);

        this.typeOneToMany = new JRadioButton("一对多");
        this.typeOneToMany.addChangeListener(this);
        typePanel.add(this.typeOneToMany);

        this.typeManyToOne = new JRadioButton("多对一");
        this.typeManyToOne.addChangeListener(this);
        typePanel.add(this.typeManyToOne);

        this.typeManyToMany = new JRadioButton("多对多");
        this.typeManyToMany.addChangeListener(this);
        typePanel.add(this.typeManyToMany);

        this.typeButtonGroup = new ButtonGroup();
        this.typeButtonGroup.add(this.typeOneToOne);
        this.typeButtonGroup.add(this.typeOneToMany);
        this.typeButtonGroup.add(this.typeManyToOne);
        this.typeButtonGroup.add(this.typeManyToMany);

        // A->B 关联关系的配置
        JPanel endAAttributePanel = new JPanel(new GridLayout(3, 1));
        JPanel endAAttributeTBPanel = new JPanel(new BorderLayout());

        JLabel endAAttributeNameLabel = new JLabel("属性名：");

        this.directionAToB = new JCheckBox("可以通过 A 获得 B");
        this.directionAToB.setSelected(true);
        this.directionAToB.addChangeListener(this);

        this.endAAttributeName = new JTextField();

        endAAttributeTBPanel.add(endAAttributeNameLabel, BorderLayout.WEST);
        endAAttributeTBPanel.add(this.endAAttributeName, BorderLayout.CENTER);

        endAAttributePanel.add(this.directionAToB);
        endAAttributePanel.add(endAAttributeTBPanel);

        endAAttributePanel.setBorder(BorderFactory.createTitledBorder("关联关系 A 端到 B 端的配置"));

        JPanel targetAToBPanel = new JPanel(new BorderLayout());
        targetAToBPanel.add(endAAttributePanel, BorderLayout.NORTH);

        // B->A 关联关系的配置
        JPanel endBAttributePanel = new JPanel(new GridLayout(3, 1));
        JPanel endBAttributeTBPanel = new JPanel(new BorderLayout());

        JLabel endBAttributeNameLabel = new JLabel("属性名：");

        this.directionBToA = new JCheckBox("可以通过 B 获得 A");
        this.directionBToA.setSelected(true);
        this.directionBToA.addChangeListener(this);

        this.endBAttributeName = new JTextField();

        endBAttributeTBPanel.add(endBAttributeNameLabel, BorderLayout.WEST);
        endBAttributeTBPanel.add(this.endBAttributeName, BorderLayout.CENTER);

        endBAttributePanel.add(this.directionBToA);
        endBAttributePanel.add(endBAttributeTBPanel);

        endBAttributePanel.setBorder(BorderFactory.createTitledBorder("关联关系 B 端到 A 端的配置"));

        JPanel targetBToAPanel = new JPanel(new BorderLayout());
        targetBToAPanel.add(endBAttributePanel, BorderLayout.NORTH);

        // 配置双方属性的面板
        JPanel attributeDetailedPanel = new JPanel(new GridLayout(1, 2));
        attributeDetailedPanel.add(targetAToBPanel);
        attributeDetailedPanel.add(targetBToAPanel);

        // 放顶部的面板
        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        topPanel.add(namePanel);
        topPanel.add(descPanel);
        topPanel.add(typePanel);

        topPanel.setBorder(BorderFactory.createTitledBorder("关系属性"));

        middleCenterPanel.add(topPanel, BorderLayout.NORTH);
        middleCenterPanel.add(attributeDetailedPanel, BorderLayout.CENTER);

        // 底部的按钮框
        this.btnSaveButton = new JButton("保存关联关系");
        this.btnSaveButton.addActionListener(this);

        this.btnDeleteRelationship = new JButton("删除关联关系");
        this.btnDeleteRelationship.addActionListener(this);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(this.btnSaveButton);
        buttonPanel.add(this.btnDeleteRelationship);

        // 主面板
        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.add(this.entityChooserPanel, BorderLayout.NORTH);
        this.mainPanel.add(middleCenterPanel, BorderLayout.CENTER);
        this.mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.add(this.mainPanel, BorderLayout.CENTER);
        this.add(new JPanel(), BorderLayout.WEST);
        this.add(new JPanel(), BorderLayout.EAST);
        this.add(new JPanel(), BorderLayout.NORTH);
        this.add(new JPanel(), BorderLayout.SOUTH);

        updateAttributeTextBoxEditable();
    }

    // </editor-fold>

    /**
     * 根据 A B 两端是否持有对方引用来将属性名称的可编辑参数进行更改
     */
    private void updateAttributeTextBoxEditable() {
        this.endAAttributeName.setEditable(this.directionAToB.isSelected());
        this.endBAttributeName.setEditable(this.directionBToA.isSelected());
    }

    /**
     * 重新填充两个实体选择下拉框的数据
     */
    private void fillComboBoxData() {
        this.entityANameComboBox.removeAllItems();
        this.entityBNameComboBox.removeAllItems();

        Iterator<String> namesIterator = projectContext.allEntityNames();
        while (namesIterator.hasNext()) {
            String entityName = namesIterator.next();

            this.entityANameComboBox.addItem(entityName);
            this.entityBNameComboBox.addItem(entityName);
        }
    }

    /**
     * 根据关联关系的类型自动生成 A 端的属性名称
     */
    private void autoEndAAttributeName() {
        String entityBName = (String) this.entityBNameComboBox.getSelectedItem();
        Inflector inflector = Inflector.getInstance();

        if (this.typeOneToOne.isSelected() ||
                this.typeManyToOne.isSelected()) {
            String attributeName = StringUtils.lowerFirstLatter(inflector.singularize(entityBName));
            this.endAAttributeName.setText(attributeName);
        } else if (this.typeOneToMany.isSelected() ||
                this.typeManyToMany.isSelected()) {
            String attributeName = StringUtils.lowerFirstLatter(inflector.pluralize(entityBName));
            this.endAAttributeName.setText(attributeName);
        }
    }

    /**
     * 根据关联关系的类型自动生成 B 端的属性名称
     */
    private void autoEndBAttributeName() {
        String entityAName = (String) this.entityANameComboBox.getSelectedItem();
        Inflector inflector = Inflector.getInstance();

        if (this.typeOneToOne.isSelected() ||
                this.typeOneToMany.isSelected()) {
            String attributeName = StringUtils.lowerFirstLatter(inflector.singularize(entityAName));
            this.endBAttributeName.setText(attributeName);
        } else if (this.typeManyToOne.isSelected() ||
                this.typeManyToMany.isSelected()) {
            String attributeName = StringUtils.lowerFirstLatter(inflector.pluralize(entityAName));
            this.endBAttributeName.setText(attributeName);
        }
    }

    private void updateDescription(boolean aToB) {
        String entityAName = (String) this.entityANameComboBox.getSelectedItem();
        String entityBName = (String) this.entityBNameComboBox.getSelectedItem();

        if (entityAName == null || entityBName == null) {
            return;
        }

        if (!aToB) {
            String tmp = entityAName;
            entityAName = entityBName;
            entityBName = tmp;
        }

        String desc = "";

        if (this.typeOneToOne.isSelected()) {
            desc = String.format("%s %s %s%s %s", ONE, entityAName, VERB, ONE, entityBName);
        } else if (this.typeOneToMany.isSelected()) {
            desc = String.format("%s %s %s%s %s",
                    aToB ? ONE : MANY, entityAName, VERB, aToB ? MANY : ONE, entityBName);
        } else if (this.typeManyToOne.isSelected()) {
            desc = String.format("%s %s %s%s %s",
                    aToB ? MANY : ONE, entityAName, VERB, aToB ? ONE : MANY, entityBName);
        } else if (this.typeManyToMany.isSelected()) {
            desc = String.format("%s %s %s%s %s", MANY, entityAName, VERB, MANY, entityBName);
        }

        if (aToB) {
            this.descAtoB.setText(desc);
        } else {
            this.descBtoA.setText(desc);
        }
    }

    private RelationshipInfoPanel() {
        super();

        this.dirty = false;
        this.id = UUID.randomUUID().toString();
        this.initComponents();

        this.projectContext = SwingUIApplication.getInstance().getCurrentProjectContext();
        this.fillComboBoxData();
    }

    public static RelationshipInfoPanel newRelationship() {
        RelationshipInfoPanel retValue = new RelationshipInfoPanel();
        retValue.newRelationship = true;

        retValue.relationshipModel = new RelationshipModel();
        String newRelationshipName = String.format("relationship_%d", IdGenerator.relationship());

        retValue.relationshipModel.setName(newRelationshipName);
        retValue.refillData();

        return retValue;
    }

    public static RelationshipInfoPanel fromExists(RelationshipModel relationshipModel) {
        RelationshipInfoPanel retValue = new RelationshipInfoPanel();
        retValue.newRelationship = false;

        retValue.relationshipModel = relationshipModel;
        retValue.refillData();

        return retValue;
    }

    private boolean bindData() {
        // 进行简单的校验
        // 如果A B 双向的方向都没有选择，弹出相应的提示框
        if (!this.directionBToA.isSelected() && !this.directionAToB.isSelected()) {
            JOptionPane.showMessageDialog(this, "关联关系至少必须选择一个方向",
                    "REST API Generator", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.entityAHandle.isSelected() && !this.directionAToB.isSelected()) {
            JOptionPane.showMessageDialog(this, "主方必须持有从方的引用方能实现关联关系的管理",
                    "REST API Generator", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.entityBHandle.isSelected() && !this.directionBToA.isSelected()) {
            JOptionPane.showMessageDialog(this, "主方必须持有从方的应用方能实现关联关系的管理",
                    "REST API Generator", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 绑定不需要转换的基本信息
        relationshipModel.setName(relationshipName.getText());
        relationshipModel.setEndAAttributeName(endAAttributeName.getText());
        relationshipModel.setEndBAttributeName(endBAttributeName.getText());

        // 绑定需要简单转换的数据
        if (this.typeOneToOne.isSelected()) {
            relationshipModel.setType(RelationshipModel.TYPE_ONE_TO_ONE);
        } else if (this.typeOneToMany.isSelected()) {
            relationshipModel.setType(RelationshipModel.TYPE_ONE_TO_MANY);
        } else if (this.typeManyToOne.isSelected()) {
            relationshipModel.setType(RelationshipModel.TYPE_MANY_TO_ONE);
        } else if (this.typeManyToMany.isSelected()) {
            relationshipModel.setType(RelationshipModel.TYPE_MANY_TO_MANY);
        }

        relationshipModel.setDirection(0);
        int direction = 0;

        if (this.directionAToB.isSelected()) {
            direction |= RelationshipModel.DIRECTION_A_TO_B;
        }

        if (this.directionBToA.isSelected()) {
            direction |= RelationshipModel.DIRECTION_B_TO_A;
        }

        relationshipModel.setDirection(direction);

        // 将需要从工程中取数据的数据绑定
        EntityModel entityA = projectContext.getEntityByName((String) entityANameComboBox.getSelectedItem());
        EntityModel entityB = projectContext.getEntityByName((String) entityBNameComboBox.getSelectedItem());

        relationshipModel.setEntityModelA(entityA);
        relationshipModel.setEntityModelB(entityB);

        if (entityAHandle.isSelected()) {
            relationshipModel.setEntityModelHandler(entityA);
        } else {
            relationshipModel.setEntityModelHandler(entityB);
        }

        return true;
    }

    private void refillData() {
        if (this.relationshipModel == null) {
            return;
        }

        EventQueue.invokeLater(() -> {
            // 获得选中的两个实体对象
            EntityModel entityModelA = relationshipModel.getEntityModelA();
            if (entityModelA != null) {
                entityANameComboBox.setSelectedItem(entityModelA.getName());
            }

            EntityModel entityModelB = relationshipModel.getEntityModelB();
            if (entityModelB != null) {
                entityBNameComboBox.setSelectedItem(entityModelB.getName());
            }

            // 处理关联的维护方
            EntityModel modelHandler = relationshipModel.getEntityModelHandler();
            if (modelHandler == entityModelA) {
                entityAHandle.setSelected(true);
            } else if (modelHandler == entityModelB) {
                entityBHandle.setSelected(true);
            }

            // 回显关联关系的名称
            relationshipName.setText(relationshipModel.getName());

            // 根据关联关系的类型选中单选框
            switch (relationshipModel.getType()) {
                case RelationshipModel.TYPE_ONE_TO_ONE:
                    typeOneToOne.setSelected(true);
                    break;
                case RelationshipModel.TYPE_ONE_TO_MANY:
                    typeOneToMany.setSelected(true);
                    break;
                case RelationshipModel.TYPE_MANY_TO_ONE:
                    typeManyToOne.setSelected(true);
                    break;
                case RelationshipModel.TYPE_MANY_TO_MANY:
                    typeManyToMany.setSelected(true);
                    break;
            }

            // 根据方向勾选下面相应的复选框
            int direction = relationshipModel.getDirection();
            switch (direction) {
                case RelationshipModel.DIRECTION_A_TO_B:
                    directionAToB.setSelected(true);
                    directionBToA.setSelected(false);
                    break;
                case RelationshipModel.DIRECTION_B_TO_A:
                    directionAToB.setSelected(false);
                    directionBToA.setSelected(true);
                    break;
                case RelationshipModel.DIRECTION_BOTH:
                    directionBToA.setSelected(true);
                    directionAToB.setSelected(true);
                    break;
            }

            // 关联属性名称的回显
            if (newRelationship) {
                autoEndAAttributeName();
                autoEndBAttributeName();
            } else {
                if (StringUtils.isStringEmpty(relationshipModel.getEndBAttributeName())) {
                    endBAttributeName.setText(relationshipModel.getEndBAttributeName());
                }

                if (StringUtils.isStringEmpty(relationshipModel.getEndAAttributeName())) {
                    endAAttributeName.setText(relationshipModel.getEndAAttributeName());
                }
            }

            // 关联关系主控方
            if (modelHandler == entityModelA) {
                entityAHandle.setSelected(true);
            } else if (modelHandler == entityModelB) {
                entityBHandle.setSelected(true);
            }
        });
    }

    public String getRelationshipName() {
        if (this.relationshipModel == null) {
            return null;
        } else {
            return this.relationshipModel.getName();
        }
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
        return null;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();
        if (source == this.directionAToB || source == this.directionBToA) {
            updateAttributeTextBoxEditable();
        } else if (source == this.typeOneToOne ||
                source == this.typeOneToMany ||
                source == this.typeManyToOne ||
                source == this.typeManyToMany) {
            autoEndAAttributeName();
            autoEndBAttributeName();

            updateDescription(true);
            updateDescription(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == this.entityANameComboBox) {
            autoEndBAttributeName();

            updateDescription(true);
            updateDescription(false);
        } else if (source == this.entityBNameComboBox) {
            autoEndAAttributeName();

            updateDescription(true);
            updateDescription(false);
        } else if (source == this.btnSaveButton) {
            // 先尝试进行数据绑定，如果成功绑定则进行提交操作
            boolean canBindData = bindData();
            if (canBindData) {
                if (newRelationship) {
                    projectContext.addRelationship(relationshipModel, this);
                    this.relationshipModel = projectContext.findRelationshipByName(relationshipModel.getName());

                    newRelationship = false;
                } else {
                    projectContext.saveRelationship(relationshipModel, this);
                }
            }

        } else if (source == this.btnDeleteRelationship) {
            int result = JOptionPane.showConfirmDialog(
                    this, "您确定要删除这个关联关系吗？",
                    "REST API Generator", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                if (newRelationship) {
                    this.dirty = false;

                    JClosableTabbedPane parent = (JClosableTabbedPane) this.getParent();
                    int index = parent.indexOfComponent(this);
                    if (index != -1) {
                        parent.remove(index);
                    }
                } else {
                    if (bindData()) {
                        this.dirty = false;

                        projectContext.deleteRelationship(relationshipModel, this);
                    }
                }
            }
        }
    }
}
