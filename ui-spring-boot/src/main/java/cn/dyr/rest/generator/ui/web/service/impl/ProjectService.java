package cn.dyr.rest.generator.ui.web.service.impl;

import cn.dyr.rest.generator.ui.web.constant.EntityConstants;
import cn.dyr.rest.generator.ui.web.dao.IAttributeDAO;
import cn.dyr.rest.generator.ui.web.dao.IDBInfoDAO;
import cn.dyr.rest.generator.ui.web.dao.IEntityDAO;
import cn.dyr.rest.generator.ui.web.dao.IProjectDAO;
import cn.dyr.rest.generator.ui.web.dao.IRelationshipDAO;
import cn.dyr.rest.generator.ui.web.dao.IUserDAO;
import cn.dyr.rest.generator.ui.web.dto.RelationshipDTO;
import cn.dyr.rest.generator.ui.web.entity.AttributeEntity;
import cn.dyr.rest.generator.ui.web.entity.DBInfoEntity;
import cn.dyr.rest.generator.ui.web.entity.EntityEntity;
import cn.dyr.rest.generator.ui.web.entity.ProjectEntity;
import cn.dyr.rest.generator.ui.web.entity.RelationshipEntity;
import cn.dyr.rest.generator.ui.web.entity.UserEntity;
import cn.dyr.rest.generator.ui.web.exception.BadParameterError;
import cn.dyr.rest.generator.ui.web.exception.DuplicatedConstraintException;
import cn.dyr.rest.generator.ui.web.exception.ResourceNotFoundException;
import cn.dyr.rest.generator.ui.web.service.IProjectService;
import cn.dyr.rest.generator.ui.web.util.IdGenerator;
import cn.dyr.rest.generator.ui.web.util.StringUtils;
import cn.dyr.rest.generator.ui.web.validate.ServiceValidateToolKit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import static cn.dyr.rest.generator.ui.web.constant.EntityConstants.RelationshipConstant.DIRECTION_A_TO_B;
import static cn.dyr.rest.generator.ui.web.constant.EntityConstants.RelationshipConstant.DIRECTION_B_TO_A;
import static cn.dyr.rest.generator.ui.web.constant.EntityConstants.RelationshipConstant.TYPE_MANY_TO_ONE;
import static cn.dyr.rest.generator.ui.web.constant.EntityConstants.RelationshipConstant.TYPE_ONE_TO_MANY;

/**
 * 工程 Service 类的实现类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@Service
public class ProjectService implements IProjectService {

    private static final Random random = new Random();

    @Autowired
    private IProjectDAO projectDAO;

    @Autowired
    private IUserDAO userDAO;

    @Autowired
    private IDBInfoDAO dbInfoDAO;

    @Autowired
    private IAttributeDAO attributeDAO;

    @Autowired
    private IEntityDAO entityDAO;

    @Autowired
    private IRelationshipDAO relationshipDAO;

    /**
     * 根据关联关系的信息返回这个关联关系的名称
     *
     * @param dto 储存有关联关系的信息
     * @return 这个关联关系对应的名称
     */
    private String getRelationshipName(RelationshipDTO dto) {
        Objects.requireNonNull(dto, "relationship info is null");
        return String.format("rel_%s", DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes()).substring(3, 13));
    }

    @Override
    public Page<ProjectEntity> getProjects(UserEntity userEntity, Pageable pageable) {
        return this.projectDAO.findByUser(userEntity.getId(), pageable);
    }

    @Override
    public List<ProjectEntity> getProjects(UserEntity userEntity) {
        return this.projectDAO.findByUser(userEntity.getId());
    }

    @Transactional
    @Override
    public ProjectEntity createProject(ProjectEntity projectEntity, UserEntity userEntity) {
        Objects.requireNonNull(projectEntity, "project entity is null");
        Objects.requireNonNull(userEntity, "user entity is null");

        // 1. 检查相应的参数
        ServiceValidateToolKit.requireStringNotEmpty(
                projectEntity.getDeveloperName(), "developer name is empty");
        ServiceValidateToolKit.requireStringNotEmpty(
                projectEntity.getPackageName(), "package name is empty");
        ServiceValidateToolKit.requireStringNotEmpty(
                projectEntity.getProjectName(), "project name is null");

        // 2. 给没有完整的信息补齐
        String displayName = projectEntity.getDisplayName();
        if (StringUtils.isStringEmpty(displayName)) {
            displayName = String.format("%s-%04d", projectEntity.getProjectName(), random.nextInt(10000));
            projectEntity.setDisplayName(displayName);
        }

        String version = projectEntity.getVersion();
        if (StringUtils.isStringEmpty(version)) {
            projectEntity.setVersion("1.0-SNAPSHOT");
        }

        // 3. 生成工程流水号
        String projectId = IdGenerator.projectId(userEntity.getId());
        projectEntity.setProjectId(projectId);

        // 4. 补充其他的必要信息
        projectEntity.setStatus(EntityConstants.ProjectEntityConstant.TYPE_SAVED);
        projectEntity.setId(0L);

        projectEntity.setEntityList(Collections.<EntityEntity>emptyList());
        projectEntity.setRelationshipList(Collections.<RelationshipEntity>emptyList());
        projectEntity.setUser(null);
        projectEntity.setJob(null);
        projectEntity.setDbInfoEntity(null);

        // 5. 调用 DAO 保存信息
        ProjectEntity saved = this.projectDAO.save(projectEntity);

        // 6. 保存工程与用户之间的关系
        saved.setUser(userEntity);
        this.projectDAO.save(saved);

        return saved;
    }

    @Override
    public ProjectEntity getProject(String projectId) {
        return projectGetOrThrow(projectId);
    }

    @Transactional
    @Override
    public ProjectEntity deleteProject(String projectId) {
        // 1. 寻找项目编号对应的是否存在
        ProjectEntity projectEntity = projectGetOrThrow(projectId);

        // 2. 调用 DAO 删除
        this.projectDAO.delete(projectEntity);

        return projectEntity;
    }

    @Transactional
    @Override
    public DBInfoEntity saveDBInfo(String projectId, DBInfoEntity dbInfoEntity) {
        // 1. 进行参数校验
        Objects.requireNonNull(projectId, "project is null");
        Objects.requireNonNull(dbInfoEntity, "db info is null");

        int dbType = dbInfoEntity.getType();
        ServiceValidateToolKit.requireInRange(
                EntityConstants.DatabaseInfoEntityConstant.TYPE_MIN,
                EntityConstants.DatabaseInfoEntityConstant.TYPE_MAX,
                dbType, "invalid database type");

        ServiceValidateToolKit.requireStringNotEmpty(dbInfoEntity.getHost(), "host is empty");
        ServiceValidateToolKit.requireStringNotEmpty(dbInfoEntity.getUsername(), "username is empty");
        ServiceValidateToolKit.requireStringNotEmpty(dbInfoEntity.getPassword(), "password is empty");
        ServiceValidateToolKit.requireStringNotEmpty(dbInfoEntity.getDbName(), "dbName is empty");

        // 2. 检查工程编号是否有效
        ProjectEntity projectEntity = this.projectDAO.findByProjectId(projectId);
        if (projectEntity == null) {
            throw new BadParameterError("invalid project id");
        }

        DBInfoEntity oldInfo = projectEntity.getDbInfoEntity();

        // 3. 保存数据库信息
        DBInfoEntity savedDBInfoEntity = this.dbInfoDAO.save(dbInfoEntity);

        // 4. 将数据库信息和工程信息进行关联
        projectEntity.setDbInfoEntity(savedDBInfoEntity);
        projectDAO.save(projectEntity);

        return oldInfo;
    }

    @Override
    public DBInfoEntity getDBInfo(String projectId) {
        ProjectEntity projectEntity = this.projectGetOrThrow(projectId);
        DBInfoEntity dbInfoEntity = projectEntity.getDbInfoEntity();

        if (dbInfoEntity == null) {
            throw new ResourceNotFoundException("db info not exists!");
        }

        return dbInfoEntity;
    }

    @Transactional
    @Override
    public DBInfoEntity deleteDBInfo(String projectId) {
        // 1. 检查工程编号是否有效
        ProjectEntity projectEntity = this.projectGetOrThrow(projectId);

        // 2. 解除工程与数据库信息之间的关联关系
        DBInfoEntity dbInfoEntity = projectEntity.getDbInfoEntity();
        if (dbInfoEntity == null) {
            return null;
        }

        projectEntity.setDbInfoEntity(null);
        this.projectDAO.save(projectEntity);

        // 3. 调用 DAO 方法删除数据库信息
        this.dbInfoDAO.delete(dbInfoEntity);

        return dbInfoEntity;
    }

    @Transactional
    @Override
    public EntityEntity saveEntity(String projectId, EntityEntity entityInfo) {
        // 1. 进行参数校验
        Objects.requireNonNull(projectId, "project id is null");
        Objects.requireNonNull(entityInfo, "entity info is null");

        ServiceValidateToolKit.requireStringNotEmpty(entityInfo.getName(), "entity name is empty");

        // 2. 补充和计算可选值
        String description = entityInfo.getDescription();
        if (StringUtils.isStringEmpty(description)) {
            entityInfo.setDescription(entityInfo.getName());
        }

        // 3. 检查工程编号是否有效
        ProjectEntity projectEntity = projectGetOrThrow(projectId);

        // 4. 原有的同名实体，先进行提取
        EntityEntity sameNameEntity = null;

        List<EntityEntity> projectEntityList = projectEntity.getEntityList();
        if (projectEntityList != null) {
            for (EntityEntity entityEntity : projectEntityList) {
                if (entityEntity.getName().equalsIgnoreCase(entityInfo.getName())) {
                    sameNameEntity = entityEntity;
                    break;
                }
            }
        }

        // 5. 如果这里的属性不为空，则先对属性进行保存
        // 5.1. 如果是实体原来已经存在，则不会对原来的属性进行任何的处理
        if (sameNameEntity == null) {
            List<AttributeEntity> attributes = entityInfo.getAttributes();
            if (attributes != null && attributes.size() > 0) {
                for (AttributeEntity attributeEntity : attributes) {
                    attributeEntity.setId(0L);
                    this.attributeDAO.save(attributeEntity);
                }
            }
        }

        // 6. 将实体信息保存到数据库当中
        // 6.1. 如果这个实体对象是一个新的实体对象，则保存到数据库里面
        // 6.2. 如果这个实体对象是一个存在的实体对象，则对其进行修改
        if (sameNameEntity == null) {
            this.entityDAO.save(entityInfo);
        } else {
            BeanUtils.copyProperties(entityInfo, sameNameEntity, "id", "attributes");
            this.entityDAO.save(sameNameEntity);
        }

        // 7. 将实体信息与工程信息进行关联
        // 7.1. 如果这是一个新的实体信息，则直接保存
        // 7.2. 如果这是一个已经存在的实体信息，则不需要对原有的关联做出任何处理
        if (sameNameEntity == null) {
            List<EntityEntity> entityList = projectEntityList;
            if (entityList == null) {
                entityList = new ArrayList<>();
                projectEntity.setEntityList(entityList);
            }

            entityList.add(entityInfo);
            this.entityDAO.save(entityInfo);
        }

        return sameNameEntity;
    }

    @NotNull
    private ProjectEntity projectGetOrThrow(String projectId) {
        ProjectEntity ret = this.projectDAO.findByProjectId(projectId);
        if (ret == null) {
            throw new ResourceNotFoundException("project not found: " + projectId);
        }

        return ret;
    }

    @NotNull
    private EntityEntity entityGetOrThrow(String projectId, String entityName) {
        EntityEntity ret = this.entityDAO.findInProject(projectId, entityName);
        if (ret == null) {
            throw new ResourceNotFoundException(String.format("entity %s not found in project %s", entityName, projectId));
        }

        return ret;
    }

    private AttributeEntity attributeGetOrThrow(String projectId, String entityName, String attributeName) {
        AttributeEntity attribute = this.attributeDAO.findInProjectAndEntity(projectId, entityName, attributeName);
        if (attribute == null) {
            throw new ResourceNotFoundException(String.format("attribute %s not found in %s->%s", attributeName, projectId, entityName));
        }

        return attribute;
    }

    @Override
    public List<EntityEntity> getEntities(String projectId) {
        ProjectEntity projectEntity = projectGetOrThrow(projectId);

        List<EntityEntity> entityList = projectEntity.getEntityList();
        if (entityList == null) {
            entityList = new ArrayList<>();
        }

        return entityList;
    }

    @Override
    public EntityEntity getEntity(String projectId, String entityName) {
        return entityGetOrThrow(projectId, entityName);
    }

    @Override
    public List<AttributeEntity> getAttributes(String projectId, String entityName) {
        EntityEntity entityEntity = entityGetOrThrow(projectId, entityName);
        List<AttributeEntity> attributes = entityEntity.getAttributes();
        if (attributes == null) {
            attributes = new ArrayList<>();
        }

        return attributes;
    }

    @Override
    public AttributeEntity getAttribute(String projectId, String entityName, String attributeName) {
        AttributeEntity attribute = this.attributeDAO.findInProjectAndEntity(projectId, entityName, attributeName);
        if (attribute == null) {
            throw new ResourceNotFoundException("attribute " + attributeName + " cannot found in " + projectId + ", " + entityName);
        }

        return attribute;
    }

    @Transactional
    @Override
    public AttributeEntity saveAttribute(String projectId, String entityName, AttributeEntity attributeInfo) {
        // 1. 进行参数校验
        Objects.requireNonNull(attributeInfo, "attribute info is null");

        ServiceValidateToolKit.requireStringNotEmpty(projectId, "project id is empty");
        ServiceValidateToolKit.requireStringNotEmpty(entityName, "entity name is empty");

        // 2. 检查项目编号和实体名称是否有效
        EntityEntity entity = entityGetOrThrow(projectId, entityName);

        // 3. 寻找实体当中同名属性，如果存在则覆盖
        AttributeEntity sameNameAttribute = null;
        List<AttributeEntity> entityAttributes = entity.getAttributes();

        if (entityAttributes != null) {
            for (AttributeEntity attributeEntity : entityAttributes) {
                if (attributeEntity.getName().equalsIgnoreCase(attributeInfo.getName())) {
                    sameNameAttribute = attributeEntity;
                    break;
                }
            }
        }

        // 4. 将属性信息保存到数据库当中
        this.attributeDAO.save(attributeInfo);

        // 5. 将实体信息与属性信息进行关联
        if (sameNameAttribute != null) {
            entityAttributes.remove(sameNameAttribute);
        }

        if (entityAttributes == null) {
            entityAttributes = new ArrayList<>();
            entity.setAttributes(entityAttributes);
        }

        entityAttributes.add(attributeInfo);
        this.entityDAO.save(entity);

        return sameNameAttribute;
    }

    @Transactional
    @Override
    public AttributeEntity deleteAttribute(String projectId, String entityName, String attributeName) {
        // 1. 查找要删除的属性，如果这个属性不存在，则直接抛出异常
        AttributeEntity toBeDeleted = this.attributeGetOrThrow(projectId, entityName, attributeName);
        this.attributeDAO.delete(toBeDeleted);

        return toBeDeleted;
    }

    @Transactional
    @Override
    public EntityEntity deleteEntity(String projectId, String entityName) {
        // 1. 查找到要删除的实体，如果这个实体不存在，则抛出异常
        EntityEntity entityEntity = this.entityGetOrThrow(projectId, entityName);

        // 2. 判断这个关系是否存在相应的关联关系

        // 3. 进行删除实体的操作
        this.entityDAO.delete(entityEntity);

        return entityEntity;
    }

    @Transactional
    @Override
    public RelationshipEntity saveRelationship(String projectId, RelationshipDTO relationshipDTO) {
        // 1. 对信息进行校验
        Objects.requireNonNull(projectId, "id is null");
        Objects.requireNonNull(relationshipDTO, "dto is null");

        ProjectEntity projectEntity = this.projectGetOrThrow(projectId);

        // 2. 判断是否为新的关联关系
        RelationshipEntity sameRelationship = null;
        if (!StringUtils.isStringEmpty(relationshipDTO.getRelationshipId())) {
            sameRelationship = this.relationshipDAO.findByRelationshipIdAndProjectId(projectId, relationshipDTO.getRelationshipId());
        }

        // 3. 判断关联关系的双方是否有效
        String entityAName = relationshipDTO.getEntityA();
        EntityEntity entityA = this.entityGetOrThrow(projectId, entityAName);
        String entityAAttribute = relationshipDTO.getEntityAAttribute();

        String entityBName = relationshipDTO.getEntityB();
        EntityEntity entityB = this.entityGetOrThrow(projectId, entityBName);
        String entityBAttribute = relationshipDTO.getEntityBAttribute();

        String handlerEntity = relationshipDTO.getHandlerEntity();
        if (!handlerEntity.equalsIgnoreCase(entityAName) &&
                !handlerEntity.equalsIgnoreCase(entityBName)) {
            throw new BadParameterError("handler entity must be entity A or entity B");
        }

        // 4. 根据主从方等信息对数据进行重新的组装
        RelationshipEntity target;
        if (sameRelationship != null) {
            target = sameRelationship;
        } else {
            target = new RelationshipEntity();
        }

        boolean handledByEntityA = (entityAName.equalsIgnoreCase(handlerEntity));
        if (handledByEntityA) {
            if (StringUtils.isStringEmpty(entityAAttribute)) {
                throw new BadParameterError("handler attribute is null");
            }

            if (relationshipDTO.getDirection() == 3 &&
                    StringUtils.isStringEmpty(entityBAttribute)) {
                throw new BadParameterError("handled attribute is null");
            }

            target.setEndA(entityA);
            target.setEndAAttributeName(entityAAttribute);
            target.setEndB(entityB);
            target.setEndBAttributeName(entityBAttribute);
            target.setHandlerEntity(entityAName);
            target.setDirection(relationshipDTO.getDirection());
            target.setType(relationshipDTO.getType());
        } else {
            if (StringUtils.isStringEmpty(entityBAttribute)) {
                throw new BadParameterError("handler attribute is null");
            }

            if (relationshipDTO.getDirection() == 3 &&
                    StringUtils.isStringEmpty(entityAAttribute)) {
                throw new BadParameterError("handled attribute is null");
            }

            target.setEndA(entityB);
            target.setEndAAttributeName(entityBAttribute);
            target.setEndB(entityA);
            target.setEndBAttributeName(entityBAttribute);
            target.setHandlerEntity(entityBName);
            target.setDirection(oppositeDirection(relationshipDTO.getDirection()));
            target.setType(oppositeType(relationshipDTO.getType()));
        }

        // 5. 根据关联关系的主控信息查询是否存在
        RelationshipEntity exists = this.relationshipDAO.findAInProject(projectId, entityAName, entityAAttribute);
        if (exists != null) {
            throw new DuplicatedConstraintException(String.format("%s %s handled another relationship",
                    target.getEndA().getName(), target.getEndAAttributeName()));
        }

        // 6. 调用 dao 接口进行保存
        this.relationshipDAO.save(target);

        // 7. 将关联关系与工程进行关联
        List<RelationshipEntity> relationshipList = projectEntity.getRelationshipList();
        if (relationshipList == null) {
            relationshipList = new ArrayList<>();
            projectEntity.setRelationshipList(relationshipList);
        }

        relationshipList.add(target);
        this.projectDAO.save(projectEntity);

        return target;
    }

    private int oppositeDirection(int direction) {
        switch (direction) {
            case DIRECTION_A_TO_B:
                return DIRECTION_B_TO_A;
            case DIRECTION_B_TO_A:
                return DIRECTION_A_TO_B;
            default:
                return direction;
        }
    }

    private int oppositeType(int type) {
        switch (type) {
            case TYPE_ONE_TO_MANY:
                return TYPE_MANY_TO_ONE;
            case TYPE_MANY_TO_ONE:
                return TYPE_ONE_TO_MANY;
            default:
                return type;
        }
    }

    @Transactional
    @Override
    public RelationshipEntity deleteRelationship(String projectId, String relationshipName) {
        return null;
    }

    @Override
    public List<RelationshipEntity> getRelationships(String projectId) {
        return null;
    }
}
