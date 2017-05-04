package cn.dyr.rest.generator.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 表示一个实体类的信息
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class EntityInfo {

    private String name;
    private List<AttributeInfo> attributes;

    private String description;
    private String tableName;

    public EntityInfo() {
        this.attributes = new ArrayList<>();
    }

    /**
     * 获得这个实体的名称
     *
     * @return 这个实体的名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置这个实体的名称
     *
     * @param name 实体的名称
     * @return 这个实体本身
     */
    public EntityInfo setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 获得这个实体当中的属性列表
     *
     * @return 这个实体当中的属性列表
     */
    public List<AttributeInfo> getAttributes() {
        return attributes;
    }

    /**
     * 往这个实体当中的添加一个属性
     *
     * @param attribute 要添加到实体当中的属性
     * @return 这个实体本身
     */
    public EntityInfo addAttribute(AttributeInfo attribute) {
        if (this.attributes == null) {
            this.attributes = new ArrayList<>();
        }

        this.attributes.add(attribute);

        return this;
    }

    /**
     * 设置这个实体当中的属性列表
     *
     * @param attributes 要设置到实体当中的属性列表
     * @return 这个实体本身
     */
    public EntityInfo setAttributes(List<AttributeInfo> attributes) {
        this.attributes = attributes;
        return this;
    }

    /**
     * 获得这个实体的描述字符串
     *
     * @return 这个实体的描述字符串
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置这个实体的描述字符串
     *
     * @param description 这个实体的描述字符串
     * @return 这个实体本身
     */
    public EntityInfo setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * 获得这个实体对应的表名
     *
     * @return 实体对应的表名
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 设置这个实体对应的表名
     *
     * @param tableName 表名
     * @return 这个实体本身
     */
    public EntityInfo setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }
}
