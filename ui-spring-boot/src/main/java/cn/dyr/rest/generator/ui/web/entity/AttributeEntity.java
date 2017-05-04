package cn.dyr.rest.generator.ui.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 属性实体
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@Entity
@Table(name = "ATTRIBUTE")
public class AttributeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private int type;

    @Column
    private boolean mandatory;

    @Column
    private boolean primaryIdentifier;

    @Column
    private int length;

    @Column
    private boolean expose;

    @Column
    private String description;

    @Column
    private boolean asSelectCondition;

    public AttributeEntity() {
        this.description = "";
        this.name = "";
        this.length = 255;
    }

    public long getId() {
        return id;
    }

    public AttributeEntity setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AttributeEntity setName(String name) {
        this.name = name;
        return this;
    }

    public int getType() {
        return type;
    }

    public AttributeEntity setType(int type) {
        this.type = type;
        return this;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public AttributeEntity setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public boolean isPrimaryIdentifier() {
        return primaryIdentifier;
    }

    public AttributeEntity setPrimaryIdentifier(boolean primaryIdentifier) {
        this.primaryIdentifier = primaryIdentifier;
        return this;
    }

    public int getLength() {
        return length;
    }

    public AttributeEntity setLength(int length) {
        this.length = length;
        return this;
    }

    public boolean isExpose() {
        return expose;
    }

    public AttributeEntity setExpose(boolean expose) {
        this.expose = expose;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AttributeEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isAsSelectCondition() {
        return asSelectCondition;
    }

    public AttributeEntity setAsSelectCondition(boolean asSelectCondition) {
        this.asSelectCondition = asSelectCondition;
        return this;
    }
}
