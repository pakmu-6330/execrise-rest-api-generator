package cn.dyr.rest.generator.ui.swing.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * 关联关系相关的模型
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class RelationshipModel implements Serializable, UUIDIdentifier {

    public static final int TYPE_ONE_TO_ONE = 1;
    public static final int TYPE_ONE_TO_MANY = 2;
    public static final int TYPE_MANY_TO_ONE = 3;
    public static final int TYPE_MANY_TO_MANY = 4;

    public static final int DIRECTION_A_TO_B = 1;
    public static final int DIRECTION_B_TO_A = 2;
    public static final int DIRECTION_BOTH = 3;

    private String id;
    private String name;
    private EntityModel entityModelA;
    private EntityModel entityModelB;
    private EntityModel entityModelHandler;
    private int type;
    private int direction;
    private String endAAttributeName;
    private String endBAttributeName;

    public RelationshipModel() {
        this.id = UUID.randomUUID().toString();
        this.type = TYPE_ONE_TO_ONE;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public RelationshipModel setName(String name) {
        this.name = name;
        return this;
    }

    public EntityModel getEntityModelA() {
        return entityModelA;
    }

    public RelationshipModel setEntityModelA(EntityModel entityModelA) {
        this.entityModelA = entityModelA;
        return this;
    }

    public EntityModel getEntityModelB() {
        return entityModelB;
    }

    public RelationshipModel setEntityModelB(EntityModel entityModelB) {
        this.entityModelB = entityModelB;
        return this;
    }

    public EntityModel getEntityModelHandler() {
        return entityModelHandler;
    }

    public RelationshipModel setEntityModelHandler(EntityModel entityModelHandler) {
        this.entityModelHandler = entityModelHandler;
        return this;
    }

    public int getType() {
        return type;
    }

    public RelationshipModel setType(int type) {
        this.type = type;
        return this;
    }

    public int getDirection() {
        return direction;
    }

    public RelationshipModel setDirection(int direction) {
        this.direction = direction;
        return this;
    }

    public String getEndAAttributeName() {
        return endAAttributeName;
    }

    public RelationshipModel setEndAAttributeName(String endAAttributeName) {
        this.endAAttributeName = endAAttributeName;
        return this;
    }

    public String getEndBAttributeName() {
        return endBAttributeName;
    }

    public RelationshipModel setEndBAttributeName(String endBAttributeName) {
        this.endBAttributeName = endBAttributeName;
        return this;
    }
}
