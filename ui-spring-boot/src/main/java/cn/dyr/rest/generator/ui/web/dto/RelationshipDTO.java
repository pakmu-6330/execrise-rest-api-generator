package cn.dyr.rest.generator.ui.web.dto;

import cn.dyr.rest.generator.ui.web.entity.RelationshipEntity;

/**
 * 这个对象用于接收用户表单相关数据的输入和用左输出的对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class RelationshipDTO {

    private String relationshipId;
    private String entityA;
    private String entityB;
    private String entityAAttribute;
    private String entityBAttribute;
    private String handlerEntity;
    private int direction;
    private int type;

    public static RelationshipDTO fromEntity(RelationshipEntity entity) {
        RelationshipDTO retValue = new RelationshipDTO();

        retValue.setRelationshipId(entity.getRelationshipId());
        retValue.setEntityA(entity.getEndA().getName());
        retValue.setEntityB(entity.getEndB().getName());
        retValue.setEntityAAttribute(entity.getEndAAttributeName());
        retValue.setEntityBAttribute(entity.getEndBAttributeName());
        retValue.setHandlerEntity(entity.getHandlerEntity());
        retValue.setDirection(entity.getDirection());
        retValue.setType(entity.getType());

        return retValue;
    }

    public String getRelationshipId() {
        return relationshipId;
    }

    public RelationshipDTO setRelationshipId(String relationshipId) {
        this.relationshipId = relationshipId;
        return this;
    }

    public String getEntityA() {
        return entityA;
    }

    public RelationshipDTO setEntityA(String entityA) {
        this.entityA = entityA;
        return this;
    }

    public String getEntityB() {
        return entityB;
    }

    public RelationshipDTO setEntityB(String entityB) {
        this.entityB = entityB;
        return this;
    }

    public String getEntityAAttribute() {
        return entityAAttribute;
    }

    public RelationshipDTO setEntityAAttribute(String entityAAttribute) {
        this.entityAAttribute = entityAAttribute;
        return this;
    }

    public String getEntityBAttribute() {
        return entityBAttribute;
    }

    public RelationshipDTO setEntityBAttribute(String entityBAttribute) {
        this.entityBAttribute = entityBAttribute;
        return this;
    }

    public String getHandlerEntity() {
        return handlerEntity;
    }

    public RelationshipDTO setHandlerEntity(String handlerEntity) {
        this.handlerEntity = handlerEntity;
        return this;
    }

    public int getDirection() {
        return direction;
    }

    public RelationshipDTO setDirection(int direction) {
        this.direction = direction;
        return this;
    }

    public int getType() {
        return type;
    }

    public RelationshipDTO setType(int type) {
        this.type = type;
        return this;
    }
}
