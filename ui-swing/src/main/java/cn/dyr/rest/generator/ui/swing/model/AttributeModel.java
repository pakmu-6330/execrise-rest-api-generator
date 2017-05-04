package cn.dyr.rest.generator.ui.swing.model;

import java.io.Serializable;

/**
 * 属性模型
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class AttributeModel implements Serializable {

    private String name;
    private String description;
    private String type;
    private boolean primaryIdentifier;
    private boolean asSelectionCondition;

    public String getName() {
        return name;
    }

    public AttributeModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AttributeModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getType() {
        return type;
    }

    public AttributeModel setType(String type) {
        this.type = type;
        return this;
    }

    public boolean isPrimaryIdentifier() {
        return primaryIdentifier;
    }

    public AttributeModel setPrimaryIdentifier(boolean primaryIdentifier) {
        this.primaryIdentifier = primaryIdentifier;
        return this;
    }

    public boolean isAsSelectionCondition() {
        return asSelectionCondition;
    }

    public AttributeModel setAsSelectionCondition(boolean asSelectionCondition) {
        this.asSelectionCondition = asSelectionCondition;
        return this;
    }
}
