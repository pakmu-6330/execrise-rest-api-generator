package cn.dyr.rest.generator.ui.swing.context;

import cn.dyr.rest.generator.entity.AttributeInfo;
import cn.dyr.rest.generator.entity.AttributeType;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.entity.EntityRelationship;
import cn.dyr.rest.generator.entity.RelationshipType;
import cn.dyr.rest.generator.project.Project;
import cn.dyr.rest.generator.project.ProjectGenerationContext;
import cn.dyr.rest.generator.project.database.IJdbcConfig;
import cn.dyr.rest.generator.project.database.JdbcConfigFactory;
import cn.dyr.rest.generator.ui.swing.exception.ConverterException;
import cn.dyr.rest.generator.ui.swing.model.AttributeModel;
import cn.dyr.rest.generator.ui.swing.model.BasicInfoModel;
import cn.dyr.rest.generator.ui.swing.model.DBInfoModel;
import cn.dyr.rest.generator.ui.swing.model.EntityModel;
import cn.dyr.rest.generator.ui.swing.model.ProjectConfigModel;
import cn.dyr.rest.generator.ui.swing.model.ProjectModel;
import cn.dyr.rest.generator.ui.swing.model.RelationshipModel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 封装了工程生成的相关逻辑
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ProjectGenerator {

    private ProjectContext context;

    private Map<String, EntityInfo> entityInfoByName;

    public ProjectGenerator(ProjectContext context) {
        this.context = context;

        this.entityInfoByName = new HashMap<>();
    }

    private AttributeInfo fromAttributeEntity(AttributeModel attributeModel) {
        AttributeInfo retValue = new AttributeInfo();
        AttributeType type;

        String strType = attributeModel.getType().toLowerCase();
        switch (strType) {
            case "byte":
                type = AttributeType.BYTE;
                break;
            case "short":
                type = AttributeType.SHORT;
                break;
            case "int":
                type = AttributeType.INT;
                break;
            case "long":
                type = AttributeType.LONG;
                break;
            case "float":
                type = AttributeType.FLOAT;
                break;
            case "double":
                type = AttributeType.DOUBLE;
                break;
            case "char":
                type = AttributeType.FIXED_STRING;
                break;
            case "varchar":
                type = AttributeType.VAR_STRING;
                break;
            case "boolean":
                type = AttributeType.BOOLEAN;
                break;
            case "datetime":
                type = AttributeType.DATETIME;
                break;
            default:
                throw new ConverterException(String.format("unexpected data type: %s", strType));
        }

        retValue.setType(type);
        retValue.setName(attributeModel.getName());
        retValue.setDescription(attributeModel.getDescription());
        retValue.setMandatory(attributeModel.isPrimaryIdentifier());
        retValue.setPrimaryIdentifier(attributeModel.isPrimaryIdentifier());
        retValue.setDescription(attributeModel.getDescription());
        retValue.setAsSelectCondition(attributeModel.isAsSelectionCondition());
        retValue.setExpose(attributeModel.isExpose());

        return retValue;
    }

    private EntityInfo fromEntityModel(EntityModel entityModel) {
        EntityInfo retValue = new EntityInfo();
        List<AttributeInfo> attributes = retValue.getAttributes();

        retValue.setName(entityModel.getName());
        retValue.setDescription(entityModel.getDescription());

        List<AttributeModel> attributeModelList = entityModel.getAttributeModelList();
        for (AttributeModel attributeModel : attributeModelList) {
            AttributeInfo attributeInfo = fromAttributeEntity(attributeModel);
            attributes.add(attributeInfo);
        }

        return retValue;
    }

    private Project fromProjectModel(ProjectModel projectModel) {
        Project retValue = new Project();

        // 1. 转换基本信息
        BasicInfoModel basicInfo = projectModel.getBasicInfo();
        retValue.setProjectName(basicInfo.getProjectName());
        retValue.setVersion(basicInfo.getVersion());
        retValue.setBasePackage(basicInfo.getPackageName());
        retValue.setAuthor(basicInfo.getAuthorName());

        // 2. 转换数据库信息
        DBInfoModel dbInfo = projectModel.getDbInfo();
        String dbType = dbInfo.getDbType().toLowerCase();
        IJdbcConfig jdbcConfig = null;

        switch (dbType) {
            case "mysql":
                jdbcConfig = JdbcConfigFactory.mySql(
                        dbInfo.getDbHost(), dbInfo.getDbName(),
                        dbInfo.getDbUsername(), dbInfo.getDbPassword());
                break;
            default:
                throw new ConverterException(String.format("unsupported database type: %s", dbType));
        }

        retValue.setJdbcConfig(jdbcConfig);

        return retValue;
    }

    private EntityRelationship fromRelationshipModel(RelationshipModel relationshipModel) {
        EntityRelationship retValue = new EntityRelationship();

        // 1. 获得所需的实体类
        EntityModel entityModelA = relationshipModel.getEntityModelA();
        EntityModel entityModelB = relationshipModel.getEntityModelB();
        EntityModel entityModelHandler = relationshipModel.getEntityModelHandler();

        EntityInfo entityInfoA = this.entityInfoByName.get(entityModelA.getName());
        EntityInfo entityInfoB = this.entityInfoByName.get(entityModelB.getName());

        retValue.setEndA(entityInfoA);
        retValue.setEndB(entityInfoB);

        retValue.setEndAAttributeName(relationshipModel.getEndAAttributeName());
        retValue.setEndAAttributeDescription(relationshipModel.getEndAAttributeDescription());
        retValue.setEndBAttributeName(relationshipModel.getEndBAttributeName());
        retValue.setEndBAttributeDescription(relationshipModel.getEndBAttributeDescription());

        if (entityModelHandler == entityModelA) {
            retValue.setRelationHandler(entityInfoA);
        } else {
            retValue.setRelationHandler(entityInfoB);
        }

        // 2. 进行关联关系类型的转换
        int type = relationshipModel.getType();
        switch (type) {
            case RelationshipModel.TYPE_ONE_TO_ONE:
                retValue.setType(RelationshipType.ONE_TO_ONE);
                break;
            case RelationshipModel.TYPE_ONE_TO_MANY:
                retValue.setType(RelationshipType.ONE_TO_MANY);
                break;
            case RelationshipModel.TYPE_MANY_TO_ONE:
                retValue.setType(RelationshipType.MANY_TO_ONE);
                break;
            case RelationshipModel.TYPE_MANY_TO_MANY:
                retValue.setType(RelationshipType.MANY_TO_MANY);
                break;
            default:
                throw new ConverterException(String.format("unexpected relationship type: %d", type));
        }

        int direction = relationshipModel.getDirection();
        if ((direction & RelationshipModel.DIRECTION_A_TO_B) == RelationshipModel.DIRECTION_A_TO_B) {
            retValue.setAToB();
        }

        if ((direction & RelationshipModel.DIRECTION_B_TO_A) == RelationshipModel.DIRECTION_B_TO_A) {
            retValue.setBToA();
        }

        return retValue;
    }

    public void generate(File targetDir) throws IOException {
        ProjectGenerationContext projectGenerationContext = new ProjectGenerationContext();

        ProjectModel projectModel = this.context.getProjectModel();
        ProjectConfigModel configModel = projectModel.getConfigModel();

        projectGenerationContext.setProject(fromProjectModel(projectModel));
        projectGenerationContext.setTablePrefix(configModel.getTablePrefix());
        projectGenerationContext.setUriPrefix(configModel.getUriPrefix());

        List<EntityModel> entityList = projectModel.getEntityList();
        for (EntityModel entityModel : entityList) {
            EntityInfo entityInfo = fromEntityModel(entityModel);
            this.entityInfoByName.put(entityModel.getName(), entityInfo);

            projectGenerationContext.addEntityInfo(entityInfo);
        }

        ProjectModel.IdArrayList<RelationshipModel> list = projectModel.getRelationshipList();
        for (RelationshipModel model : list) {
            EntityRelationship relationship = fromRelationshipModel(model);

            projectGenerationContext.addEntityRelationship(relationship);
        }

        projectGenerationContext.generate(targetDir);
    }
}
