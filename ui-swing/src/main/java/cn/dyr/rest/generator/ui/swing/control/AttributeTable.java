package cn.dyr.rest.generator.ui.swing.control;

import cn.dyr.rest.generator.ui.swing.model.AttributeModel;
import cn.dyr.rest.generator.ui.swing.util.IdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.Component;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import static cn.dyr.rest.generator.ui.swing.constant.UIConstant.ATTRIBUTE_TYPES;

/**
 * 专门用于显示实体属性的控件
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class AttributeTable extends JTable {

    private static Logger logger;

    private static String[] COLUMNS_NAME = {
            "属性名", "描述", "类型", "实体唯一标识符", "必填项", "查询条件", "是否对外暴露"
    };

    static {
        logger = LoggerFactory.getLogger(AttributeTable.class);
    }

    private List<AttributeModel> attributeModelList;

    public AttributeTable() {
        super();

        this.attributeModelList = new ArrayList<>();

        AttributeTableModel attributeTableModel = new AttributeTableModel();
        setModel(attributeTableModel);

        TableColumn typeColumn = getColumnModel().getColumn(2);
        typeColumn.setCellEditor(new TypeComboBoxEditor(ATTRIBUTE_TYPES));
        typeColumn.setCellRenderer(new TypeComboBoxRenderer(ATTRIBUTE_TYPES));

        setRowHeight(30);
    }

    public int createAttribute() {
        int retValue = this.attributeModelList.size();

        String newName = String.format("attr_%d", IdGenerator.attribute());

        AttributeModel model = new AttributeModel();
        model.setName(newName);
        model.setDescription(newName);
        model.setType("int");
        model.setPrimaryIdentifier(false);
        model.setAsSelectionCondition(false);

        this.attributeModelList.add(model);
        EventQueue.invokeLater(this::updateUI);

        return retValue;
    }

    public List<AttributeModel> getAttributeModelList() {
        return attributeModelList;
    }

    public AttributeTable setAttributeModelList(List<AttributeModel> attributeModelList) {
        this.attributeModelList = attributeModelList;
        return this;
    }

    private static class TypeComboBoxRenderer extends JComboBox<String> implements TableCellRenderer {

        TypeComboBoxRenderer(String[] values) {
            super(values);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }

            setSelectedItem(value);
            return this;
        }

    }

    private static class TypeComboBoxEditor extends DefaultCellEditor {
        public TypeComboBoxEditor(String[] values) {
            super(new JComboBox<>(values));
        }
    }

    private class AttributeTableModel extends DefaultTableModel {

        private boolean checkRange(int row, int col) {
            return !(col < 0 || col >= COLUMNS_NAME.length) && !(row < 0 || row >= attributeModelList.size());

        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                case 1:
                case 2:
                    return String.class;
                case 3:
                case 4:
                case 5:
                case 6:
                    return Boolean.class;
                default:
                    logger.warn("unexpected col index: {}", columnIndex);
                    return null;
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return true;
        }

        @Override
        public Object getValueAt(int row, int column) {
            if (!checkRange(row, column)) {
                return null;
            }

            AttributeModel attributeModel = attributeModelList.get(row);
            switch (column) {
                case 0:
                    return attributeModel.getName();
                case 1:
                    return attributeModel.getDescription();
                case 2:
                    return attributeModel.getType();
                case 3:
                    return attributeModel.isPrimaryIdentifier();
                case 4:
                    return attributeModel.isMandatory();
                case 5:
                    return attributeModel.isAsSelectionCondition();
                case 6:
                    return attributeModel.isExpose();
                default:
                    logger.warn("unexpected col index: {}", column);
                    return null;
            }
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            if (!checkRange(row, column)) {
                return;
            }

            AttributeModel attributeModel = attributeModelList.get(row);

            try {
                switch (column) {
                    case 0:
                        attributeModel.setName((String) aValue);
                        break;
                    case 1:
                        attributeModel.setDescription((String) aValue);
                        break;
                    case 2:
                        attributeModel.setType((String) aValue);
                        break;
                    case 3:
                        attributeModel.setPrimaryIdentifier((Boolean) aValue);
                        break;
                    case 4:
                        attributeModel.setMandatory((Boolean) aValue);
                        break;
                    case 5:
                        attributeModel.setAsSelectionCondition((Boolean) aValue);
                        break;
                    case 6:
                        attributeModel.setExpose((Boolean) aValue);
                        break;
                    default:
                        logger.warn("unexpected col index: {}", column);
                        break;
                }
            } catch (Exception e) {
                logger.warn("exception occurred during setting value", e);
            }
        }

        @Override
        public int getRowCount() {
            return attributeModelList.size();
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS_NAME[column];
        }

        @Override
        public int getColumnCount() {
            return COLUMNS_NAME.length;
        }
    }

}