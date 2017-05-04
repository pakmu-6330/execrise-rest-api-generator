package cn.dyr.rest.generator.ui.web.dto;

import cn.dyr.rest.generator.ui.web.entity.AttributeEntity;
import org.springframework.beans.BeanUtils;

/**
 * 实体属性对应的 DTO 对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class AttributeDTO {

    private String name;
    private int type;
    private boolean mandatory;
    private boolean primaryIdentifier;
    private int length;
    private boolean expose;
    private String description;
    private boolean asSelectionCondition;

    public static AttributeDTO fromAttributeEntity(AttributeEntity attribute) {
        AttributeDTO retValue = new AttributeDTO();
        BeanUtils.copyProperties(attribute, retValue);

        return retValue;
    }

    public String getName() {
        return name;
    }

    public AttributeDTO setName(String name) {
        this.name = name;
        return this;
    }

    public int getType() {
        return type;
    }

    public AttributeDTO setType(int type) {
        this.type = type;
        return this;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public AttributeDTO setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public boolean isPrimaryIdentifier() {
        return primaryIdentifier;
    }

    public AttributeDTO setPrimaryIdentifier(boolean primaryIdentifier) {
        this.primaryIdentifier = primaryIdentifier;
        return this;
    }

    public int getLength() {
        return length;
    }

    public AttributeDTO setLength(int length) {
        this.length = length;
        return this;
    }

    public boolean isExpose() {
        return expose;
    }

    public AttributeDTO setExpose(boolean expose) {
        this.expose = expose;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AttributeDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isAsSelectionCondition() {
        return asSelectionCondition;
    }

    public AttributeDTO setAsSelectionCondition(boolean asSelectionCondition) {
        this.asSelectionCondition = asSelectionCondition;
        return this;
    }
}
