package cn.dyr.rest.generator.ui.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * 表示一个实体的实体信息
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@Entity
@Table(name = "ENTITY")
public class EntityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private String description;

    @OneToMany
    @JoinColumn(name = "ref_entity")
    private List<AttributeEntity> attributes;

    public EntityEntity() {
        this.name = "";
        this.description = "";
    }

    public long getId() {
        return id;
    }

    public EntityEntity setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public EntityEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EntityEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<AttributeEntity> getAttributes() {
        return attributes;
    }

    public EntityEntity setAttributes(List<AttributeEntity> attributes) {
        this.attributes = attributes;
        return this;
    }
}
