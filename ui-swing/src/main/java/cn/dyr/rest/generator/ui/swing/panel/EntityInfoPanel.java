package cn.dyr.rest.generator.ui.swing.panel;

import cn.dyr.rest.generator.ui.swing.SwingUIApplication;
import cn.dyr.rest.generator.ui.swing.context.ProjectContext;
import cn.dyr.rest.generator.ui.swing.control.AttributeTable;
import cn.dyr.rest.generator.ui.swing.control.DataPanel;
import cn.dyr.rest.generator.ui.swing.control.JClosableTabbedPane;
import cn.dyr.rest.generator.ui.swing.exception.DuplicatedNameException;
import cn.dyr.rest.generator.ui.swing.exception.EntityIdentifierNotFoundException;
import cn.dyr.rest.generator.ui.swing.model.AttributeModel;
import cn.dyr.rest.generator.ui.swing.model.EntityModel;
import cn.dyr.rest.generator.ui.swing.model.RelationshipModel;
import cn.dyr.rest.generator.ui.swing.model.UUIDIdentifier;
import cn.dyr.rest.generator.ui.swing.util.IdGenerator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 用于进行显示实体信息的面板
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class EntityInfoPanel extends JPanel implements DataPanel, ActionListener, UUIDIdentifier {

    private String id;
    private boolean newEntity;
    private boolean dirty;

    private EntityModel entityModel;
    private ProjectContext projectContext;

    // <editor-fold desc="界面相关">

    private JTextField tfEntityName;
    private JTextField tfDescription;

    private JButton btnEntityNameCheck;
    private JButton btnSaveEntity;
    private JButton btnDeleteEntity;
    private JButton btnNewAttribute;
    private JButton btnDeleteAttribute;

    private JPanel mainPanel;

    private AttributeTable attributeTable;

    private void initComponents() {
        // 为了在面板的四周增加一遍间隙
        this.mainPanel = new JPanel(new BorderLayout());

        // 顶端信息面板
        JPanel topInfoPanel = new JPanel(new GridLayout(2, 1));

        // 用于处理实体信息的面板
        JPanel namePanel = new JPanel(new BorderLayout());

        JLabel lbName = new JLabel("实体名称：");
        namePanel.add(lbName, BorderLayout.WEST);

        this.tfEntityName = new JTextField();
        namePanel.add(this.tfEntityName, BorderLayout.CENTER);

        this.btnEntityNameCheck = new JButton("检查重名");
        this.btnEntityNameCheck.addActionListener(this);
        namePanel.add(this.btnEntityNameCheck, BorderLayout.EAST);

        // 用于处理实体描述的面板
        JPanel descriptionPanel = new JPanel(new BorderLayout());

        JLabel lbDescription = new JLabel("实体描述：");
        descriptionPanel.add(lbDescription, BorderLayout.WEST);

        this.tfDescription = new JTextField();
        descriptionPanel.add(this.tfDescription, BorderLayout.CENTER);

        topInfoPanel.add(namePanel);
        topInfoPanel.add(descriptionPanel);

        mainPanel.add(topInfoPanel, BorderLayout.NORTH);

        // 中间的属性表格
        this.attributeTable = new AttributeTable();
        mainPanel.add(new JScrollPane(this.attributeTable), BorderLayout.CENTER);

        // 底部的按钮
        this.btnSaveEntity = new JButton("保存实体");
        this.btnSaveEntity.addActionListener(this);

        this.btnDeleteEntity = new JButton("删除这个实体");
        this.btnDeleteEntity.addActionListener(this);

        this.btnNewAttribute = new JButton("新建属性");
        this.btnNewAttribute.addActionListener(this);

        this.btnDeleteAttribute = new JButton("删除属性");
        this.btnDeleteAttribute.addActionListener(this);

        JPanel buttonPanel = new JPanel(new BorderLayout());

        JPanel attributeButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        attributeButtonsPanel.add(btnNewAttribute);
        attributeButtonsPanel.add(btnDeleteAttribute);

        JPanel forGoodLooking = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        forGoodLooking.add(this.btnDeleteEntity);
        forGoodLooking.add(this.btnSaveEntity);

        buttonPanel.add(attributeButtonsPanel, BorderLayout.WEST);
        buttonPanel.add(forGoodLooking, BorderLayout.EAST);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 将主面板添加到宿主面板当中
        this.add(mainPanel, BorderLayout.CENTER);
        this.add(new JPanel(), BorderLayout.NORTH);
        this.add(new JPanel(), BorderLayout.SOUTH);
        this.add(new JPanel(), BorderLayout.WEST);
        this.add(new JPanel(), BorderLayout.EAST);
    }

    private EntityInfoPanel() {
        super(new BorderLayout());

        initComponents();

        this.id = UUID.randomUUID().toString();
        this.dirty = true;
    }

    // </editor-fold>

    public static EntityInfoPanel newEntity() {
        EntityInfoPanel retValue = new EntityInfoPanel();
        retValue.newEntity = true;

        retValue.projectContext = SwingUIApplication.getInstance().getCurrentProjectContext();

        retValue.entityModel = new EntityModel();
        String newName = String.format("entity_%d", IdGenerator.entity());

        retValue.entityModel.setName(newName);
        retValue.entityModel.setDescription(newName);
        retValue.fillFromData();

        return retValue;
    }

    public static EntityInfoPanel fromExistsEntity(EntityModel entityModel) {
        EntityInfoPanel retValue = new EntityInfoPanel();
        retValue.newEntity = false;
        retValue.projectContext = SwingUIApplication.getInstance().getCurrentProjectContext();

        retValue.entityModel = entityModel;
        retValue.fillFromData();

        return retValue;
    }

    public String getEntityName() {
        if (this.entityModel == null) {
            return "null";
        } else {
            return this.entityModel.getName();
        }
    }

    private void bindData() {
        int idCount = 0;

        // 检查属性当中是否存在
        List<AttributeModel> attributeModelList = attributeTable.getAttributeModelList();
        for (AttributeModel attributeModel : attributeModelList) {
            if (attributeModel.isPrimaryIdentifier()) {
                idCount++;
            }
        }

        if (idCount == 0) {
            throw new EntityIdentifierNotFoundException(tfEntityName.getName());
        }

        List<AttributeModel> tmpArrayList = new ArrayList<>();
        tmpArrayList.addAll(attributeModelList);

        entityModel.setName(tfEntityName.getText());
        entityModel.setDescription(tfDescription.getText());
        entityModel.getAttributeModelList().clear();
        entityModel.getAttributeModelList().addAll(tmpArrayList);
    }

    private void fillFromData() {
        if (entityModel == null) {
            return;
        }

        EventQueue.invokeLater(() -> {
            tfEntityName.setText(entityModel.getName());
            tfDescription.setText(entityModel.getDescription());

            List<AttributeModel> attributeModelList = entityModel.getAttributeModelList();
            if (attributeModelList != null && attributeModelList.size() > 0) {
                attributeTable.setAttributeModelList(attributeModelList);
                attributeTable.updateUI();
            }
        });
    }

    @Override
    public String getId() {
        return id;
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
            ActionEvent e = new ActionEvent(this.btnSaveEntity, 0, "");
            actionPerformed(e);
        };
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == this.btnNewAttribute) {
            int newIndex = this.attributeTable.createAttribute();
            this.attributeTable.setRowSelectionInterval(newIndex, newIndex);
        } else if (source == this.btnDeleteAttribute) {
            int[] selectedRows = this.attributeTable.getSelectedRows();
            List<AttributeModel> attributeModelList = this.attributeTable.getAttributeModelList();

            for (int i = selectedRows.length - 1; i >= 0; i--) {
                attributeModelList.remove(selectedRows[i]);
            }

            this.attributeTable.updateUI();
        } else if (source == this.btnSaveEntity) {
            try {
                bindData();
                projectContext.saveEntity(entityModel, this);

                // 对于额外情况的处理
                if (newEntity) {
                    newEntity = false;
                }
            } catch (EntityIdentifierNotFoundException exception) {
                JOptionPane.showMessageDialog(
                        this, "实体当中没唯一标识符",
                        SwingUIApplication.APP_NAME, JOptionPane.ERROR_MESSAGE);
            } catch (DuplicatedNameException exception) {
                JOptionPane.showMessageDialog(
                        this, String.format("当前工程中已经存在名为%s的实体", exception.getName()),
                        SwingUIApplication.APP_NAME, JOptionPane.ERROR_MESSAGE);
            }
        } else if (source == this.btnEntityNameCheck) {
            EntityModel entity = projectContext.getEntityByName(tfEntityName.getText());
            if (entity != null) {
                JOptionPane.showMessageDialog(this, "这个实体名称已经存在",
                        SwingUIApplication.APP_NAME, JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "这个实体名称有效",
                        SwingUIApplication.APP_NAME, JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (source == this.btnDeleteEntity) {
            // 检查这个关系实体是否处于被维护的位置，如果是，不允许直接进行删除操作
            List<RelationshipModel> entityHandled = projectContext.getRelationshipListEntityHandled(this.entityModel);
            if (!newEntity && entityHandled != null && entityHandled.size() > 0) {
                StringBuilder builder = new StringBuilder();
                builder.append("当前实体在下面关联关系中处于被维护方：");
                builder.append(System.lineSeparator());

                for (RelationshipModel relationshipModel : entityHandled) {
                    builder.append(relationshipModel.getName());
                    builder.append(System.lineSeparator());
                }

                builder.append("当前实体不能被删除，请确认后再执行删除操作！");
                JOptionPane.showMessageDialog(this, builder.toString(),
                        SwingUIApplication.APP_NAME, JOptionPane.ERROR_MESSAGE);
                return;
            }

            int result = JOptionPane.showConfirmDialog(
                    this, "您确定要删除这个实体吗？该操作不可恢复！",
                    SwingUIApplication.APP_NAME, JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                if (newEntity) {
                    this.dirty = false;

                    JClosableTabbedPane parent = (JClosableTabbedPane) this.getParent();
                    int index = parent.indexOfComponent(this);

                    if (index != -1) {
                        parent.remove(index);
                    }
                } else {
                    bindData();
                    this.dirty = false;

                    List<RelationshipModel> entityHandles = projectContext.getRelationshipListEntityHandles(this.entityModel);

                    if (entityHandles != null && entityHandles.size() > 0) {
                        StringBuilder builder = new StringBuilder();
                        builder.append("当前实体维护着下面的关联关系：");
                        builder.append(System.lineSeparator());

                        for (RelationshipModel model : entityHandles) {
                            builder.append(model.getName());
                            builder.append(System.lineSeparator());
                        }

                        builder.append("如果删除实体，上面的关联关系也会随之删除，是否继续？");
                        result = JOptionPane.showConfirmDialog(this, builder.toString(), "REST API Generator", JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.YES_OPTION) {
                            try {
                                projectContext.deleteEntity(entityModel, this);
                            } catch (Exception exception) {
                                JOptionPane.showMessageDialog(this,
                                        "删除实体类发生错误：" + exception.getMessage(),
                                        SwingUIApplication.APP_NAME, JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        }
    }
}
