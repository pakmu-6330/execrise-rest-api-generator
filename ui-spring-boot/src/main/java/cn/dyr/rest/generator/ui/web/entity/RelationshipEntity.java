package cn.dyr.rest.generator.ui.web.entity;

import org.springframework.util.DigestUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.UUID;

/**
 * 表示一个关联关系的实体对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@Entity
@Table(name = "RELATIONSHIP")
public class RelationshipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String relationshipId;

    @Column
    private String name;

    @OneToOne
    private EntityEntity endA;

    @OneToOne
    private EntityEntity endB;

    @Column
    private int direction;

    @Column
    private String endAAttributeName;

    @Column
    private boolean endAExpose;

    @Column
    private String endBAttributeName;

    @Column
    private boolean endBExpose;

    @Column
    private String handlerEntity;

    @Column
    private int type;

    @Column
    private boolean cascadeSave;

    @Column
    private boolean cascadeDelete;

    public RelationshipEntity() {
        this.relationshipId = DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes());
        this.name = "";
        this.endAAttributeName = "";
        this.endBAttributeName = "";
        this.endA = null;
        this.endB = null;
    }

    public long getId() {
        return id;
    }

    public RelationshipEntity setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public RelationshipEntity setName(String name) {
        this.name = name;
        return this;
    }

    public EntityEntity getEndA() {
        return endA;
    }

    public RelationshipEntity setEndA(EntityEntity endA) {
        this.endA = endA;
        return this;
    }

    public EntityEntity getEndB() {
        return endB;
    }

    public RelationshipEntity setEndB(EntityEntity endB) {
        this.endB = endB;
        return this;
    }

    public int getDirection() {
        return direction;
    }

    public RelationshipEntity setDirection(int direction) {
        this.direction = direction;
        return this;
    }

    public String getEndAAttributeName() {
        return endAAttributeName;
    }

    public RelationshipEntity setEndAAttributeName(String endAAttributeName) {
        this.endAAttributeName = endAAttributeName;
        return this;
    }

    public boolean isEndAExpose() {
        return endAExpose;
    }

    public RelationshipEntity setEndAExpose(boolean endAExpose) {
        this.endAExpose = endAExpose;
        return this;
    }

    public String getEndBAttributeName() {
        return endBAttributeName;
    }

    public RelationshipEntity setEndBAttributeName(String endBAttributeName) {
        this.endBAttributeName = endBAttributeName;
        return this;
    }

    public boolean isEndBExpose() {
        return endBExpose;
    }

    public RelationshipEntity setEndBExpose(boolean endBExpose) {
        this.endBExpose = endBExpose;
        return this;
    }

    public boolean isCascadeSave() {
        return cascadeSave;
    }

    public RelationshipEntity setCascadeSave(boolean cascadeSave) {
        this.cascadeSave = cascadeSave;
        return this;
    }

    public boolean isCascadeDelete() {
        return cascadeDelete;
    }

    public RelationshipEntity setCascadeDelete(boolean cascadeDelete) {
        this.cascadeDelete = cascadeDelete;
        return this;
    }

    public String getHandlerEntity() {
        return handlerEntity;
    }

    public RelationshipEntity setHandlerEntity(String handlerEntity) {
        this.handlerEntity = handlerEntity;
        return this;
    }

    public String getRelationshipId() {
        return relationshipId;
    }

    public RelationshipEntity setRelationshipId(String relationshipId) {
        this.relationshipId = relationshipId;
        return this;
    }

    public int getType() {
        return type;
    }

    public RelationshipEntity setType(int type) {
        this.type = type;
        return this;
    }
}
