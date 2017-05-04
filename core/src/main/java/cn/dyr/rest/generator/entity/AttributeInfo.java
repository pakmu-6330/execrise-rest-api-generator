package cn.dyr.rest.generator.entity;

/**
 * 表示实体当中的一个属性
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class AttributeInfo {

    private AttributeType type;
    private String name;
    private boolean mandatory;
    private boolean primaryIdentifier;
    private int length;
    private boolean expose;
    private String description;
    private boolean asSelectCondition;

    public AttributeInfo() {
        this.expose = true;
    }

    /**
     * 获得这个属性的类型
     *
     * @return 这个属性的类型
     */
    public AttributeType getType() {
        return type;
    }

    /**
     * 设置这个属性的类型信息
     *
     * @param type 这个属性的类型
     * @return 属性对象本身
     */
    public AttributeInfo setType(AttributeType type) {
        this.type = type;
        return this;
    }

    /**
     * 获得这个属性的名称
     *
     * @return 这个属性的名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置这个属性的名称
     *
     * @param name 属性的名称
     * @return 这个对象本身
     */
    public AttributeInfo setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 这个属性是否为必须的属性
     *
     * @return 表示这个属性是否必须属性的布尔值
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * 设置这个属性是否为必须
     *
     * @param mandatory 表示这个属性是否必须的布尔值
     * @return 这个对象本身
     */
    public AttributeInfo setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    /**
     * 这个属性是否为这个实体的唯一标识符
     *
     * @return 表示这个属性是否为这个实体唯一标识符的布尔值
     */
    public boolean isPrimaryIdentifier() {
        return primaryIdentifier;
    }

    /**
     * 设置这个属性是否为这个实体的唯一标识符
     *
     * @param primaryIdentifier 表示这个属性是否为唯一标识符的布尔值
     * @return 这个属性本身
     */
    public AttributeInfo setPrimaryIdentifier(boolean primaryIdentifier) {
        this.primaryIdentifier = primaryIdentifier;
        return this;
    }

    /**
     * 这个属性的最大长度
     *
     * @return 这个属性的最大长度
     */
    public int getLength() {
        return length;
    }

    /**
     * 设置这个属性的最大长度
     *
     * @param length 这个属性的最大程度值
     * @return 这个属性本身
     */
    public AttributeInfo setLength(int length) {
        this.length = length;
        return this;
    }

    /**
     * 这个属性是否会在 API 的响应当中暴露
     *
     * @return 表示这个属性是否会在 API 响应中暴露的布尔值
     */
    public boolean isExpose() {
        return expose;
    }

    /**
     * 设置属性是否会在最终的 API 响应当中暴露
     *
     * @param expose 表示这个属性是否会在 API 响应当中暴露的布尔值
     */
    public void setExpose(boolean expose) {
        this.expose = expose;
    }

    /**
     * 获得这个属性的描述字符串
     *
     * @return 用于描述这个属性的字符串
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述这个属性的字符串
     *
     * @param description 描述这个属性的字符串
     * @return 这个属性本身对象
     */
    public AttributeInfo setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * 表示这个属性值是否会作为数据库查询的条件
     *
     * @return 表示这个属性值是否会作为数据库查询条件的布尔值
     */
    public boolean isAsSelectCondition() {
        return asSelectCondition;
    }

    /**
     * 设置这个条件是否会被作为数据库查询条件
     *
     * @param asSelectCondition 表示这个属性值是否会作为数据库查询条件的布尔值
     * @return 这个属性信息本身
     */
    public AttributeInfo setAsSelectCondition(boolean asSelectCondition) {
        this.asSelectCondition = asSelectCondition;
        return this;
    }
}
