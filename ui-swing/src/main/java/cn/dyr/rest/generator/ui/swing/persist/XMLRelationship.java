package cn.dyr.rest.generator.ui.swing.persist;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.UUID;

/**
 * XML 关联关系
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@XmlRootElement
@XmlType(name = "relationship", propOrder = {
        "id", "name", "entityA", "entityB", "handler", "type", "direction", "endAAttributeName", "endBAttributeName"
})
public class XMLRelationship {

    private String id;

    private String name;
    private String entityA;
    private String entityB;
    private String handler;
    private int type;
    private int direction;
    private String endAAttributeName;
    private String endBAttributeName;

    public XMLRelationship() {
        this.id = UUID.randomUUID().toString();
    }

    @XmlElement
    public String getId() {
        return id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public XMLRelationship setName(String name) {
        this.name = name;
        return this;
    }

    @XmlElement
    public String getEntityA() {
        return entityA;
    }

    public XMLRelationship setEntityA(String entityA) {
        this.entityA = entityA;
        return this;
    }

    @XmlElement
    public String getEntityB() {
        return entityB;
    }

    public XMLRelationship setEntityB(String entityB) {
        this.entityB = entityB;
        return this;
    }

    @XmlElement
    public String getHandler() {
        return handler;
    }

    public XMLRelationship setHandler(String handler) {
        this.handler = handler;
        return this;
    }

    @XmlElement
    public int getType() {
        return type;
    }

    public XMLRelationship setType(int type) {
        this.type = type;
        return this;
    }

    @XmlElement
    public int getDirection() {
        return direction;
    }

    public XMLRelationship setDirection(int direction) {
        this.direction = direction;
        return this;
    }

    @XmlElement
    public String getEndAAttributeName() {
        return endAAttributeName;
    }

    public XMLRelationship setEndAAttributeName(String endAAttributeName) {
        this.endAAttributeName = endAAttributeName;
        return this;
    }

    @XmlElement
    public String getEndBAttributeName() {
        return endBAttributeName;
    }

    public XMLRelationship setEndBAttributeName(String endBAttributeName) {
        this.endBAttributeName = endBAttributeName;
        return this;
    }
}
