package cn.dyr.rest.generator.ui.swing.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 表示一个实体的模型信息
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class EntityModel implements Serializable, UUIDIdentifier {

    private String id;
    private String name;
    private String description;
    private List<AttributeModel> attributeModelList;

    public EntityModel() {
        this.attributeModelList = new ArrayList<>();
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public EntityModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EntityModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<AttributeModel> getAttributeModelList() {
        return attributeModelList;
    }

    public EntityModel setAttributeModelList(List<AttributeModel> attributeModelList) {
        this.attributeModelList = attributeModelList;
        return this;
    }

    public EntityModel addAttributeModel(AttributeModel attributeModel) {
        if (this.attributeModelList == null) {
            this.attributeModelList = new ArrayList<>();
        }

        this.attributeModelList.add(attributeModel);

        return this;
    }
}
