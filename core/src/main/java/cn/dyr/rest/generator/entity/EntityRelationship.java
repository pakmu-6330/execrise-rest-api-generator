package cn.dyr.rest.generator.entity;

/**
 * 表示实体之间关系的类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class EntityRelationship {

    public static final int DIRECTION_A_TO_B = 0x1;
    public static final int DIRECTION_B_TO_A = 0x2;
    public static final int DIRECTION_BOTH = 0x3;

    private EntityInfo endA;
    private EntityInfo endB;

    private RelationshipType type;
    private EntityInfo relationHandler;
    private int direction;

    private String endAAttributeName;
    private boolean endAExpose;

    private String endBAttributeName;
    private boolean endBExpose;

    private boolean cascadeSave;
    private boolean cascadeDelete;

    public EntityRelationship() {
        this.endAExpose = true;
        this.endBExpose = true;
    }

    /**
     * 获得关联关系 A 端
     *
     * @return 关联关系 A 端的实体类
     */
    public EntityInfo getEndA() {
        return endA;
    }

    /**
     * 设置关联关系 A 端
     *
     * @param endA A 端实体类
     * @return 这个关联关系本身
     */
    public EntityRelationship setEndA(EntityInfo endA) {
        this.endA = endA;
        return this;
    }

    /**
     * 获得关联关系 B 端
     *
     * @return 关联关系 B 端
     */
    public EntityInfo getEndB() {
        return endB;
    }

    /**
     * 设置关联关系 B 端
     *
     * @param endB B 端实体类
     * @return 这个关联关系本身
     */
    public EntityRelationship setEndB(EntityInfo endB) {
        this.endB = endB;
        return this;
    }

    /**
     * 获得这个关联关系的类型
     *
     * @return 这个关联关系的类型
     */
    public RelationshipType getType() {
        return type;
    }

    /**
     * 设置这个关联关系的类型
     *
     * @param type 这个关联关系的类型
     * @return 这个关联关系的类型
     */
    public EntityRelationship setType(RelationshipType type) {
        this.type = type;
        return this;
    }

    /**
     * 获得关联关系的维护方
     *
     * @return 由那个对象维护关联关系
     */
    public EntityInfo getRelationHandler() {
        return relationHandler;
    }

    /**
     * 设置关联关系的维护方
     *
     * @param relationHandler 关联关系的维护方
     * @return 这个关联关系本身
     */
    public EntityRelationship setRelationHandler(EntityInfo relationHandler) {
        this.relationHandler = relationHandler;
        return this;
    }

    /**
     * 设置 A 到 B 的关联关系方向
     *
     * @return 这个关系值本身
     */
    public EntityRelationship setAToB() {
        this.direction |= DIRECTION_A_TO_B;
        return this;
    }

    /**
     * 设置 B 到 A 的关联关系方向
     *
     * @return 这个关系值本身
     */
    public EntityRelationship setBToA() {
        this.direction |= DIRECTION_B_TO_A;
        return this;
    }

    /**
     * 取消 A 到 B 的关联关系方向
     *
     * @return 这个关系值本身
     */
    public EntityRelationship unsetAToB() {
        this.direction &= (~DIRECTION_A_TO_B);
        return this;
    }

    /**
     * 取消 B 到 A 的关联关系方向
     *
     * @return 这个关系值本身
     */
    public EntityRelationship unsetBToA() {
        this.direction &= (~DIRECTION_B_TO_A);
        return this;
    }

    /**
     * 获得这个关联关系是否有 A 到 B 的方向
     *
     * @return 表示这个关联关系是否具有 A 到 B 方向的关系
     */
    public boolean hasAToB() {
        return (this.direction & DIRECTION_A_TO_B) == DIRECTION_A_TO_B;
    }

    /**
     * 获得这个关联关系是否有 B 到 A 的方向
     *
     * @return 表示这个关联关系是否具有 B 到 A 方向的关系
     */
    public boolean hasBToA() {
        return (this.direction & DIRECTION_B_TO_A) == DIRECTION_B_TO_A;
    }

    /**
     * 判断这个关联关系的方向是否为双向
     *
     * @return 表示这个关联关系是否为双向的布尔值
     */
    public boolean isBidirectional() {
        return this.direction == DIRECTION_BOTH;
    }

    /**
     * 获得这个关联关系 A 端的名称
     *
     * @return 这个关联关系 A 端的名称
     */
    public String getEndAAttributeName() {
        return endAAttributeName;
    }

    /**
     * 设置这个关联关系 A 端的名称
     *
     * @param endAAttributeName 关联关系 A 端的名称
     * @return 这个关联关系本身
     */
    public EntityRelationship setEndAAttributeName(String endAAttributeName) {
        this.endAAttributeName = endAAttributeName;
        return this;
    }

    /**
     * 获得这个关联关系 B 端的名称
     *
     * @return 这个关联关系本身
     */
    public String getEndBAttributeName() {
        return endBAttributeName;
    }

    /**
     * 设置这个关联关系 B 端的名称
     *
     * @param endBAttributeName 关联关系在 B 端的名称
     * @return 这个关联关系本身
     */
    public EntityRelationship setEndBAttributeName(String endBAttributeName) {
        this.endBAttributeName = endBAttributeName;
        return this;
    }

    /**
     * 表示 A-&gt;B 关联关系的属性是否对外暴露
     *
     * @return 一个表示 A 端到 B 端关联关系是否对外暴露的布尔值
     */
    public boolean isEndAExpose() {
        return endAExpose;
    }

    /**
     * 设置 A-&gt;B 关联关系的属性是否对外暴露
     *
     * @param endAExpose 表示 A 端到 B 端关联关系是否对外暴露的布尔值
     * @return 关联关系对象本身
     */
    public EntityRelationship setEndAExpose(boolean endAExpose) {
        this.endAExpose = endAExpose;
        return this;
    }

    /**
     * 表示 B-&gt;A 关联关系的属性是否对外暴露
     *
     * @return 一个表示 B 端到 A 端关联关系是否对外暴露的布尔值
     */
    public boolean isEndBExpose() {
        return endBExpose;
    }

    /**
     * 设置 B-&gt;A 关联关系的属性是否对外暴露
     *
     * @param endBExpose 一个表示 B 端到 A 端关联关系是否对外暴露的布尔值
     * @return 关联关系对象本身
     */
    public EntityRelationship setEndBExpose(boolean endBExpose) {
        this.endBExpose = endBExpose;
        return this;
    }

    /**
     * 获得这个关系是否进行级联保存
     *
     * @return 表示这个关联关系是否进行级联保存的布尔值
     */
    public boolean isCascadeSave() {
        return cascadeSave;
    }

    /**
     * 设置这个关系是否进行级联保存
     *
     * @param cascadeSave 这个关联关系是否进行级联保存
     * @return 关联关系对象本身
     */
    public EntityRelationship setCascadeSave(boolean cascadeSave) {
        this.cascadeSave = cascadeSave;
        return this;
    }

    /**
     * 获得这个关系是否进行级联删除
     *
     * @return 表示这个关联关系是否进行级联删除的布尔值
     */
    public boolean isCascadeDelete() {
        return cascadeDelete;
    }

    /**
     * 设置这个关联关系进行级联删除
     *
     * @param cascadeDelete 这个关联关系是否进行级联删除的布尔值
     * @return 关联关系对象本身
     */
    public EntityRelationship setCascadeDelete(boolean cascadeDelete) {
        this.cascadeDelete = cascadeDelete;
        return this;
    }
}
