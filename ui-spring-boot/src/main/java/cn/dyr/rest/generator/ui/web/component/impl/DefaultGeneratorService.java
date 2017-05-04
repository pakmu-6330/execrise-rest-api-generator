package cn.dyr.rest.generator.ui.web.component.impl;

import cn.dyr.rest.generator.entity.AttributeInfo;
import cn.dyr.rest.generator.entity.AttributeType;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.entity.EntityRelationship;
import cn.dyr.rest.generator.entity.RelationshipType;
import cn.dyr.rest.generator.project.Project;
import cn.dyr.rest.generator.project.ProjectGenerationContext;
import cn.dyr.rest.generator.project.database.IJdbcConfig;
import cn.dyr.rest.generator.project.database.JdbcConfigFactory;
import cn.dyr.rest.generator.ui.web.component.IGeneratorService;
import cn.dyr.rest.generator.ui.web.constant.EntityConstants;
import cn.dyr.rest.generator.ui.web.dao.IJobDAO;
import cn.dyr.rest.generator.ui.web.entity.AttributeEntity;
import cn.dyr.rest.generator.ui.web.entity.DBInfoEntity;
import cn.dyr.rest.generator.ui.web.entity.EntityEntity;
import cn.dyr.rest.generator.ui.web.entity.JobEntity;
import cn.dyr.rest.generator.ui.web.entity.ProjectEntity;
import cn.dyr.rest.generator.ui.web.entity.RelationshipEntity;
import cn.dyr.rest.generator.ui.web.util.StringUtils;
import cn.dyr.rest.generator.ui.web.util.ZipCompressor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cn.dyr.rest.generator.ui.web.constant.EntityConstants.RelationshipConstant.DIRECTION_A_TO_B;
import static cn.dyr.rest.generator.ui.web.constant.EntityConstants.RelationshipConstant.DIRECTION_B_TO_A;
import static cn.dyr.rest.generator.ui.web.constant.EntityConstants.RelationshipConstant.TYPE_MANY_TO_MANY;
import static cn.dyr.rest.generator.ui.web.constant.EntityConstants.RelationshipConstant.TYPE_MANY_TO_ONE;
import static cn.dyr.rest.generator.ui.web.constant.EntityConstants.RelationshipConstant.TYPE_ONE_TO_MANY;
import static cn.dyr.rest.generator.ui.web.constant.EntityConstants.RelationshipConstant.TYPE_ONE_TO_ONE;

/**
 * 默认的代码生成内核实现类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@Component
public class DefaultGeneratorService implements IGeneratorService {

    private static final Logger logger;
    private static final String TARGET_OUTPUT_DIR = "F:\\output";
    private static final String TARGET_OUTPUT_TMP_DIR = "F:\\output_tmp";
    private static final int THREAD_NUMBER_FOR_GENERATING = 1;

    static {
        logger = LoggerFactory.getLogger(DefaultGeneratorService.class);
    }

    private ExecutorService executorService;

    @Autowired
    private IJobDAO jobDAO;

    public DefaultGeneratorService() {
        executorService = Executors.newFixedThreadPool(THREAD_NUMBER_FOR_GENERATING);
    }

    @Override
    public void submitJob(JobEntity jobEntity) {
        Objects.requireNonNull(jobEntity, "job is null");

        CodeGenerationExecuteThread executeThread = new CodeGenerationExecuteThread(jobEntity);
        this.executorService.execute(executeThread);
    }

    private class CodeGenerationExecuteThread implements Runnable {

        private JobEntity jobEntity;
        private Map<String, EntityInfo> entityByName;

        CodeGenerationExecuteThread(JobEntity jobEntity) {
            this.jobEntity = jobEntity;
            this.entityByName = new HashMap<>();
        }

        /**
         * 这个方法完整从本模块项目信息到生成内核信息之间的转换
         *
         * @param entity 本模块的项目信息
         * @return 生成内核对应的项目信息
         */
        private Project fromProjectEntity(ProjectEntity entity) {
            Project project = new Project();

            // 转换基本信息
            project.setAuthor(entity.getDeveloperName());
            project.setProjectName(entity.getProjectName());
            project.setVersion(entity.getVersion());
            project.setBasePackage(entity.getPackageName());

            // 转换数据库信息
            DBInfoEntity dbInfoEntity = entity.getDbInfoEntity();
            if (dbInfoEntity != null) {
                int dbType = dbInfoEntity.getType();

                switch (dbType) {
                    case EntityConstants.DatabaseInfoEntityConstant.TYPE_MYSQL:
                        IJdbcConfig mysqlJdbc = JdbcConfigFactory.mySql(
                                dbInfoEntity.getHost(), dbInfoEntity.getDbName(),
                                dbInfoEntity.getUsername(), dbInfoEntity.getPassword());
                        project.setJdbcConfig(mysqlJdbc);
                        break;
                    default:
                        logger.warn("failed to handle database type: {}", dbType);
                        break;
                }
            }

            return project;
        }

        /**
         * 这个方法的作用是对属性进行转换
         *
         * @param attributeEntity 要进行转换的属性
         * @return 转换后的属性
         */
        private AttributeInfo fromAttributeEntity(AttributeEntity attributeEntity) {
            AttributeInfo retValue = new AttributeInfo();
            BeanUtils.copyProperties(attributeEntity, retValue, "type");

            // 对类型进行转换
            AttributeType type = null;
            int rawType = attributeEntity.getType();
            switch (rawType) {
                case EntityConstants.AttributeEntityConstant.TYPE_BYTE:
                    type = AttributeType.BYTE;
                    break;
                case EntityConstants.AttributeEntityConstant.TYPE_SHORT:
                    type = AttributeType.SHORT;
                    break;
                case EntityConstants.AttributeEntityConstant.TYPE_INT:
                    type = AttributeType.INT;
                    break;
                case EntityConstants.AttributeEntityConstant.TYPE_LONG:
                    type = AttributeType.LONG;
                    break;
                case EntityConstants.AttributeEntityConstant.TYPE_FLOAT:
                    type = AttributeType.FLOAT;
                    break;
                case EntityConstants.AttributeEntityConstant.TYPE_DOUBLE:
                    type = AttributeType.DOUBLE;
                    break;
                case EntityConstants.AttributeEntityConstant.TYPE_FIXED_STRING:
                    type = AttributeType.FIXED_STRING;
                    break;
                case EntityConstants.AttributeEntityConstant.TYPE_VAR_STRING:
                    type = AttributeType.VAR_STRING;
                    break;
                case EntityConstants.AttributeEntityConstant.TYPE_BOOLEAN:
                    type = AttributeType.BOOLEAN;
                    break;
                case EntityConstants.AttributeEntityConstant.TYPE_DATETIME:
                    type = AttributeType.DATETIME;
                    break;
                default:
                    logger.warn("failed to handle type {}", rawType);
                    break;
            }

            retValue.setType(type);

            return retValue;
        }

        /**
         * 对实体进行转换
         *
         * @param entityEntity 数据库中原始的数据库信息
         * @return 转换后的实体信息
         */
        private EntityInfo fromEntityEntity(EntityEntity entityEntity) {
            EntityInfo retValue = new EntityInfo();

            retValue.setName(entityEntity.getName());
            retValue.setDescription(entityEntity.getDescription());

            // 对实体当中的属性进行逐个转换
            List<AttributeInfo> targetAttributes = new ArrayList<>();
            List<AttributeEntity> attributes = entityEntity.getAttributes();
            for (AttributeEntity entity : attributes) {
                AttributeInfo attributeInfo = this.fromAttributeEntity(entity);
                targetAttributes.add(attributeInfo);
            }

            retValue.setAttributes(targetAttributes);

            return retValue;
        }

        /**
         * 对来自数据库的关联关系数据进行转换
         *
         * @param relationshipEntity 来自于数据库的关联关系对象
         * @return 经过转换以后，适用于代码生成模块的关联关系信息对象
         */
        private EntityRelationship fromRelationshipEntity(RelationshipEntity relationshipEntity) {
            EntityEntity endA = relationshipEntity.getEndA();
            EntityEntity endB = relationshipEntity.getEndB();

            EntityInfo entityInfoA = this.entityByName.get(endA.getName());
            EntityInfo entityInfoB = this.entityByName.get(endB.getName());
            EntityInfo handlerEntityInfo = this.entityByName.get(relationshipEntity.getHandlerEntity());

            RelationshipType relationshipType = null;

            switch (relationshipEntity.getType()) {
                case TYPE_ONE_TO_ONE:
                    relationshipType = RelationshipType.ONE_TO_ONE;
                    break;
                case TYPE_ONE_TO_MANY:
                    relationshipType = RelationshipType.ONE_TO_MANY;
                    break;
                case TYPE_MANY_TO_ONE:
                    relationshipType = RelationshipType.MANY_TO_ONE;
                    break;
                case TYPE_MANY_TO_MANY:
                    relationshipType = RelationshipType.MANY_TO_MANY;
                    break;
            }

            EntityRelationship retValue = new EntityRelationship()
                    .setEndA(entityInfoA)
                    .setEndB(entityInfoB)
                    .setRelationHandler(handlerEntityInfo)
                    .setEndAAttributeName(StringUtils.stringOrEmpty(relationshipEntity.getEndAAttributeName()))
                    .setEndBAttributeName(StringUtils.stringOrEmpty(relationshipEntity.getEndBAttributeName()))
                    .setType(relationshipType);

            int direction = relationshipEntity.getDirection();
            if ((direction & DIRECTION_A_TO_B) == DIRECTION_A_TO_B) {
                retValue.setAToB();
            }

            if ((direction & DIRECTION_B_TO_A) == DIRECTION_B_TO_A) {
                retValue.setBToA();
            }

            return retValue;
        }

        @Override
        public void run() {
            ProjectEntity projectEntity = this.jobEntity.getProject();

            // 0. 将正在执行的状态写入数据库
            jobEntity.setJobStatus(EntityConstants.JobEntityConstant.JOB_STATUS_EXECUTING);
            jobDAO.save(jobEntity);

            // 1. 转换基本的项目信息
            Project project = this.fromProjectEntity(projectEntity);

            // 2. 转换所有的实体信息
            List<EntityEntity> entityList = projectEntity.getEntityList();

            for (EntityEntity entityEntity : entityList) {
                EntityInfo entityInfo = this.fromEntityEntity(entityEntity);
                entityByName.put(entityInfo.getName(), entityInfo);
            }

            // 3. 对关联关系进行转换
            List<RelationshipEntity> relationshipList = projectEntity.getRelationshipList();
            List<EntityRelationship> afterRelationshipList = new ArrayList<>();

            for (RelationshipEntity entity : relationshipList) {
                EntityRelationship relationship = this.fromRelationshipEntity(entity);
                afterRelationshipList.add(relationship);
            }

            try {
                // X-1. 生成前文件路径的准备
                File tmpFile = new File(TARGET_OUTPUT_TMP_DIR, projectEntity.getProjectId());
                File targetFile = new File(TARGET_OUTPUT_DIR, projectEntity.getProjectId() + ".zip");

                boolean tmpMkdirs = tmpFile.mkdirs();
                if (!tmpMkdirs) {
                    logger.error("failed to created tmp dir: {}", tmpFile);
                    return;
                }

                // X. 调用进行生成
                ProjectGenerationContext generationContext = new ProjectGenerationContext();
                generationContext.setProject(project);

                for (EntityInfo entityInfo : entityByName.values()) {
                    generationContext.addEntityInfo(entityInfo);
                }

                for (EntityRelationship relationship : afterRelationshipList) {
                    generationContext.addEntityRelationship(relationship);
                }

                try {
                    generationContext.generate(tmpFile);
                } catch (IOException e) {
                    logger.error("exception during generating project {}", projectEntity.getProjectId(), e);
                }

                // X+1. 压缩
                ZipCompressor zipCompressor = new ZipCompressor(targetFile);
                zipCompressor.compressExe(tmpFile.getAbsolutePath());

                // X+2. 将生成以后的路径等数据写入到数据库当中
                jobEntity.setFinishTime(new Date());
                jobEntity.setJobStatus(EntityConstants.JobEntityConstant.JOB_STATUS_FINISH);
                jobDAO.save(jobEntity);

                logger.info("project {} generated successfully", projectEntity.getProjectId());

            } catch (Exception e) {
                logger.error("failed to generating project {}", projectEntity.getProjectId(), e);

                jobEntity.setFinishTime(new Date());
                jobEntity.setJobStatus(EntityConstants.JobEntityConstant.JOB_STATUS_FAILED);
                jobDAO.save(jobEntity);
            }
        }
    }
}
