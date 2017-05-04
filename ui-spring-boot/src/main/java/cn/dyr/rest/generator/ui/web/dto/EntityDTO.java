package cn.dyr.rest.generator.ui.web.dto;

import cn.dyr.rest.generator.ui.web.entity.AttributeEntity;
import cn.dyr.rest.generator.ui.web.entity.EntityEntity;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体对应的 DTO 对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class EntityDTO {

    private String name;
    private String description;
    private List<AttributeDTO> attributes;

    public static EntityDTO fromEntityEntity(EntityEntity entityEntity) {
        EntityDTO retValue = new EntityDTO();
        BeanUtils.copyProperties(entityEntity, retValue);

        List<AttributeDTO> dtoList = new ArrayList<>();
        List<AttributeEntity> attributes = entityEntity.getAttributes();
        if (attributes != null) {
            for (AttributeEntity attributeEntity : attributes) {
                AttributeDTO dto = AttributeDTO.fromAttributeEntity(attributeEntity);
                dtoList.add(dto);
            }
        }

        retValue.setAttributes(dtoList);

        return retValue;
    }

    public List<AttributeDTO> getAttributes() {
        return attributes;
    }

    public EntityDTO setAttributes(List<AttributeDTO> attributes) {
        this.attributes = attributes;
        return this;
    }

    public String getName() {
        return name;
    }

    public EntityDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EntityDTO setDescription(String description) {
        this.description = description;
        return this;
    }
}
