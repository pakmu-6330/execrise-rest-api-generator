package cn.dyr.rest.generator.ui.swing.persist;

import cn.dyr.rest.generator.ui.swing.context.ProjectContext;
import cn.dyr.rest.generator.ui.swing.model.AttributeModel;
import cn.dyr.rest.generator.ui.swing.model.BasicInfoModel;
import cn.dyr.rest.generator.ui.swing.model.DBInfoModel;
import cn.dyr.rest.generator.ui.swing.model.EntityModel;
import cn.dyr.rest.generator.ui.swing.model.ProjectConfigModel;
import cn.dyr.rest.generator.ui.swing.model.ProjectModel;
import cn.dyr.rest.generator.ui.swing.model.RelationshipModel;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这个类用于内部模型类和 XML 模型类之间的相互转换
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class XMLAdapter {

    /**
     * 从 XML 模型转换成内部模型
     *
     * @param xmlProject XML 模型
     * @return 内部模型
     */
    public static ProjectContext fromXml(XMLProject xmlProject) {
        ProjectModel projectModel = new ProjectModel();
        ProjectContext projectContext = new ProjectContext(projectModel);
        Map<String, EntityModel> entityModelMapByXmlId = new HashMap<>();

        // 基本信息
        BasicInfoModel basicInfoModel = new BasicInfoModel();
        projectModel.setBasicInfo(basicInfoModel);

        BeanUtils.copyProperties(xmlProject, basicInfoModel, "id");

        // 数据库信息
        DBInfoModel dbInfoModel = new DBInfoModel();
        projectModel.setDbInfo(dbInfoModel);

        BeanUtils.copyProperties(xmlProject, dbInfoModel, "id");

        // 配置信息
        ProjectConfigModel configModel = new ProjectConfigModel();
        projectModel.setConfigModel(configModel);

        BeanUtils.copyProperties(xmlProject.getConfig(), configModel, "id");

        // 实体对象转换
        List<XMLEntity> xmlEntities = xmlProject.getEntities();
        for (XMLEntity xmlEntity : xmlEntities) {
            List<AttributeModel> attributes = new ArrayList<>();

            // 先对属性进行转换
            List<XMLAttribute> xmlAttributes = xmlEntity.getAttributes();
            for (XMLAttribute xmlAttribute : xmlAttributes) {
                AttributeModel attributeModel = new AttributeModel();
                BeanUtils.copyProperties(xmlAttribute, attributeModel, "id");

                attributes.add(attributeModel);
            }

            // 对实体信息进行转换
            EntityModel entityModel = new EntityModel();
            BeanUtils.copyProperties(xmlEntity, entityModel, "id", "attributes");
            entityModel.setAttributeModelList(attributes);

            entityModelMapByXmlId.put(xmlEntity.getId(), entityModel);
            projectContext.saveEntity(entityModel, null);
        }


        // 关联关系转换
        List<XMLRelationship> xmlRelationships = xmlProject.getRelationships();

        for (XMLRelationship relationship : xmlRelationships) {
            RelationshipModel relationshipModel = new RelationshipModel();

            relationshipModel.setName(relationship.getName());
            relationshipModel.setType(relationship.getType());
            relationshipModel.setDirection(relationship.getDirection());
            relationshipModel.setEndAAttributeName(relationship.getEndAAttributeName());
            relationshipModel.setEndAAttributeDescription(relationship.getEndAAttributeDescription());
            relationshipModel.setEndBAttributeName(relationship.getEndBAttributeName());
            relationshipModel.setEndBAttributeDescription(relationship.getEndBAttributeDescription());

            // 相应关系的转换
            EntityModel entityA = entityModelMapByXmlId.get(relationship.getEntityA());
            EntityModel entityB = entityModelMapByXmlId.get(relationship.getEntityB());
            EntityModel handler = entityModelMapByXmlId.get(relationship.getHandler());

            relationshipModel.setEntityModelA(entityA);
            relationshipModel.setEntityModelB(entityB);
            relationshipModel.setEntityModelHandler(handler);

            projectContext.saveRelationship(relationshipModel, null);
        }

        return projectContext;
    }

    /**
     * 从内部模型转换成 XML 模型
     *
     * @return 内部模型对应的 XML 模型
     */
    public static XMLProject fromModel(ProjectModel projectModel) {
        XMLProject retValue = new XMLProject();
        Map<String, String> entityModelIdToFileId = new HashMap<>();

        // 基本信息
        BasicInfoModel basicInfo = projectModel.getBasicInfo();
        BeanUtils.copyProperties(basicInfo, retValue, "id");

        // 数据库信息
        DBInfoModel dbInfo = projectModel.getDbInfo();
        BeanUtils.copyProperties(dbInfo, retValue, "id");

        // 代码生成配置信息
        ProjectConfigModel configModel = projectModel.getConfigModel();
        XMLConfig xmlConfig = new XMLConfig();
        retValue.setConfig(xmlConfig);
        BeanUtils.copyProperties(configModel, xmlConfig, "id");

        // 实体对象
        List<XMLEntity> entities = new ArrayList<>();

        ProjectModel.IdArrayList<EntityModel> entityList = projectModel.getEntityList();
        for (EntityModel entityModel : entityList) {
            List<XMLAttribute> attributes = new ArrayList<>();

            // 对属性进行转换
            List<AttributeModel> attributeModelList = entityModel.getAttributeModelList();
            for (AttributeModel attributeModel : attributeModelList) {
                XMLAttribute attribute = new XMLAttribute();
                BeanUtils.copyProperties(attributeModel, attribute, "id");
                attributes.add(attribute);
            }

            XMLEntity entity = new XMLEntity();
            BeanUtils.copyProperties(entityModel, entity, "id", "attributes");
            entity.setAttributes(attributes);

            entities.add(entity);

            // 保存两种实体 id 之间的关联关系
            entityModelIdToFileId.put(entityModel.getId(), entity.getId());
        }

        retValue.setEntities(entities);

        // 进行关联关系的转换
        List<XMLRelationship> relationships = new ArrayList<>();

        ProjectModel.IdArrayList<RelationshipModel> relationshipList = projectModel.getRelationshipList();
        for (RelationshipModel relationshipModel : relationshipList) {
            XMLRelationship relationship = new XMLRelationship();

            relationship.setName(relationshipModel.getName());
            relationship.setType(relationshipModel.getType());
            relationship.setDirection(relationshipModel.getDirection());
            relationship.setEndAAttributeName(relationshipModel.getEndAAttributeName());
            relationship.setEndBAttributeName(relationshipModel.getEndBAttributeName());
            relationship.setEndAAttributeDescription(relationshipModel.getEndAAttributeDescription());
            relationship.setEndBAttributeDescription(relationshipModel.getEndBAttributeDescription());

            // 寻找关联双方的 id
            String aId = entityModelIdToFileId.get(relationshipModel.getEntityModelA().getId());
            String bId = entityModelIdToFileId.get(relationshipModel.getEntityModelB().getId());
            String handler = entityModelIdToFileId.get(relationshipModel.getEntityModelHandler().getId());

            relationship.setEntityA(aId);
            relationship.setEntityB(bId);
            relationship.setHandler(handler);

            relationships.add(relationship);
        }

        retValue.setRelationships(relationships);

        return retValue;
    }

}
