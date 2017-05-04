package cn.dyr.rest.generator.ui.web.util;

import cn.dyr.rest.generator.ui.web.entity.AttributeEntity;
import cn.dyr.rest.generator.ui.web.entity.DBInfoEntity;
import cn.dyr.rest.generator.ui.web.entity.EntityEntity;
import cn.dyr.rest.generator.ui.web.entity.JobEntity;
import cn.dyr.rest.generator.ui.web.entity.ProjectEntity;
import cn.dyr.rest.generator.ui.web.entity.RelationshipEntity;
import cn.dyr.rest.generator.ui.web.entity.UserEntity;
import org.w3c.dom.Attr;

import java.util.List;

/**
 * 处理懒加载手动触发
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class LazyLoadProcessor {

    public static void processEntity(EntityEntity entityEntity) {
        List<AttributeEntity> attributes = entityEntity.getAttributes();
        for (AttributeEntity attributeEntity : attributes) {
            String uselessName = attributeEntity.getName();
        }
    }

    public static void processRelationship(RelationshipEntity relationshipEntity) {
        EntityEntity endA = relationshipEntity.getEndA();
        String useLessName = endA.getName();

        EntityEntity endB = relationshipEntity.getEndB();
        String name = endB.getName();
    }

    public static void processProjectEntity(ProjectEntity projectEntity) {
        DBInfoEntity dbInfoEntity = projectEntity.getDbInfoEntity();
        int uselessType = dbInfoEntity.getType();

        List<EntityEntity> entityList = projectEntity.getEntityList();
        for (EntityEntity entityEntity : entityList) {
            String uselessName = entityEntity.getName();

            processEntity(entityEntity);
        }

        List<RelationshipEntity> relationshipList = projectEntity.getRelationshipList();
        for (RelationshipEntity relationshipEntity : relationshipList) {
            processRelationship(relationshipEntity);
        }
    }

    public static void processJobEntity(JobEntity jobEntity) {
        UserEntity submitter = jobEntity.getSubmitter();
        String uselessUserEntity = submitter.getUsername();

        processProjectEntity(jobEntity.getProject());
    }

}
