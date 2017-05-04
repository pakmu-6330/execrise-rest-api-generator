package cn.dyr.rest.generator.ui.swing.persist;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;
import java.util.UUID;

/**
 * 用于保存实体数据的对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@XmlRootElement
@XmlType(name = "entity", propOrder = {
        "id", "name", "description", "attributes"
})
public class XMLEntity {

    private String id;

    private String name;
    private String description;
    private List<XMLAttribute> attributes;

    public XMLEntity() {
        this.id = UUID.randomUUID().toString();
    }

    @XmlElement
    public String getId() {
        return id;
    }

    public XMLEntity setId(String id) {
        this.id = id;
        return this;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public XMLEntity setName(String name) {
        this.name = name;
        return this;
    }

    @XmlElement
    public String getDescription() {
        return description;
    }

    public XMLEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    @XmlElement(name = "attribute")
    @XmlElementWrapper(name = "attributes")
    public List<XMLAttribute> getAttributes() {
        return attributes;
    }

    public XMLEntity setAttributes(List<XMLAttribute> attributes) {
        this.attributes = attributes;
        return this;
    }
}
