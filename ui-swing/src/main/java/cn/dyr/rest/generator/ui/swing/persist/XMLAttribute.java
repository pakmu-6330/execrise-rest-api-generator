package cn.dyr.rest.generator.ui.swing.persist;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.UUID;

/**
 * 用于保存属性信息的对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@XmlRootElement
@XmlType(name = "attribute", propOrder = {
        "id", "name", "description", "type", "primaryIdentifier", "asSelectionCondition", "expose"
})
public class XMLAttribute {

    private String id;
    private String name;
    private String description;
    private String type;
    private boolean primaryIdentifier;
    private boolean asSelectionCondition;
    private boolean expose;

    public XMLAttribute() {
        this.id = UUID.randomUUID().toString();
        this.expose = true;
    }

    @XmlElement
    public String getId() {
        return id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public XMLAttribute setName(String name) {
        this.name = name;
        return this;
    }

    @XmlElement
    public String getType() {
        return type;
    }

    public XMLAttribute setType(String type) {
        this.type = type;
        return this;
    }

    @XmlElement
    public String getDescription() {
        return description;
    }

    public XMLAttribute setDescription(String description) {
        this.description = description;
        return this;
    }

    @XmlElement
    public boolean isPrimaryIdentifier() {
        return primaryIdentifier;
    }

    public XMLAttribute setPrimaryIdentifier(boolean primaryIdentifier) {
        this.primaryIdentifier = primaryIdentifier;
        return this;
    }

    @XmlElement
    public boolean isAsSelectionCondition() {
        return asSelectionCondition;
    }

    public XMLAttribute setAsSelectionCondition(boolean asSelectionCondition) {
        this.asSelectionCondition = asSelectionCondition;
        return this;
    }

    @XmlElement
    public boolean isExpose() {
        return expose;
    }

    public void setExpose(boolean expose) {
        this.expose = expose;
    }
}
