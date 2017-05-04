package cn.dyr.rest.generator.ui.web.service;

import cn.dyr.rest.generator.ui.web.dto.RelationshipDTO;
import cn.dyr.rest.generator.ui.web.entity.AttributeEntity;
import cn.dyr.rest.generator.ui.web.entity.DBInfoEntity;
import cn.dyr.rest.generator.ui.web.entity.EntityEntity;
import cn.dyr.rest.generator.ui.web.entity.ProjectEntity;
import cn.dyr.rest.generator.ui.web.entity.RelationshipEntity;
import cn.dyr.rest.generator.ui.web.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 工程相关的 Service 类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IProjectService {

    /**
     * 查找工程列表的分页数据
     *
     * @param userEntity 用户实体
     * @param pageable   分页相关
     * @return 对应内容
     */
    Page<ProjectEntity> getProjects(UserEntity userEntity, Pageable pageable);

    /**
     * 获得所有工程的列表
     *
     * @param userEntity 用户实体
     * @return 工程列表
     */
    List<ProjectEntity> getProjects(UserEntity userEntity);

    /**
     * 为指定的 User 创建一个工程项目信息
     *
     * @param projectEntity 工程项目信息
     * @param userEntity    执行工程创建的用户对象
     * @return 如果创建成功，则返回创建后的工程对象，否则返回 null
     */
    ProjectEntity createProject(ProjectEntity projectEntity, UserEntity userEntity);

    /**
     * 根据项目流水号获得项目信息
     *
     * @param projectId 项目的流水号
     * @return 如果这个项目存在，则返回项目的信息；否则返回 null
     */
    ProjectEntity getProject(String projectId);

    /**
     * 删除工程项目信息
     *
     * @param projectId 工程编号
     * @return 如果成功删除，则返回这个工程信息；否则返回 null
     */
    ProjectEntity deleteProject(String projectId);

    /**
     * 为指定的工程创建一个数据库信息
     *
     * @param projectId    工程编号
     * @param dbInfoEntity 要保存到这个工程的数据库信息
     * @return 返回原来与这个工程相关联的数据库信息；如果返回 null，则表示这是第一次保存数据库信息；
     */
    DBInfoEntity saveDBInfo(String projectId, DBInfoEntity dbInfoEntity);

    /**
     * 获得指定工程的数据库信息
     *
     * @param projectId 工程编号
     * @return 如果工程对应的数据库信息存在，则返回这个工程对应的数据库信息；否则返回 null
     */
    DBInfoEntity getDBInfo(String projectId);

    /**
     * 为指定的工程删除数据库信息
     *
     * @param projectId 工程编号
     * @return 如果成功删除这个工程的数据库信息，则返回被删除的数据库信息；否则返回 null
     */
    DBInfoEntity deleteDBInfo(String projectId);

    /**
     * 为指定的工程创建一个实体信息
     *
     * @param projectId  工程编号
     * @param entityInfo 要保存到这个工程的实体信息
     * @return 返回原来同名的实体；如果返回 null，则表示这是第一次保存实体信息
     */
    EntityEntity saveEntity(String projectId, EntityEntity entityInfo);

    /**
     * 返回一个给定工程的所有的实体信息
     *
     * @param projectId 工程编号
     * @return 这个工程存在的所有实体信息；如果这个工程不存在任何的实体信息，则返回一个空列表
     */
    List<EntityEntity> getEntities(String projectId);

    /**
     * 返回一个给定工程的实体信息
     *
     * @param projectId  工程编号
     * @param entityName 实体名称
     * @return 如果这个工程指定的实体存在，则返回这个实体
     */
    EntityEntity getEntity(String projectId, String entityName);

    /**
     * 返回一个给定实体的所有的属性的列表
     *
     * @param projectId  工程编号
     * @param entityName 实体名称
     * @return 相应的属性的列表
     */
    List<AttributeEntity> getAttributes(String projectId, String entityName);

    /**
     * 返回一个给定的属性信息
     *
     * @param projectId     工程编号
     * @param entityName    实体名称
     * @param attributeName 属性名称
     * @return 如果指定的属性信息存在，则返回这个属性信息
     */
    AttributeEntity getAttribute(String projectId, String entityName, String attributeName);

    /**
     * 为指定的实体创建一个属性字段
     *
     * @param projectId     工程编号
     * @param entityName    实体名称
     * @param attributeInfo 要保存的属性信息
     * @return 返回原来同名的属性；如果返回 null，则表示这是第一次保存属性信息
     */
    AttributeEntity saveAttribute(String projectId, String entityName, AttributeEntity attributeInfo);

    /**
     * 删除实体当中的其中一个属性
     *
     * @param projectId     工程编号
     * @param entityName    实体名称
     * @param attributeName 要删除的属性的属性名
     * @return 如果成功删除属性，则返回这个属性对象，否则返回 null
     */
    AttributeEntity deleteAttribute(String projectId, String entityName, String attributeName);

    /**
     * 删除工程当中的一个实体
     *
     * @param projectId  工程编号
     * @param entityName 要删除的实体名称
     * @return 如果成功删除实体，则返回相应实体内容；否则返回 null
     */
    EntityEntity deleteEntity(String projectId, String entityName);

    /**
     * 往工程当中保存一个关联关系信息
     *
     * @param projectId       工程编号
     * @param relationshipDTO 含有要保存关联关系的信息
     * @return 如果这个关联关系保存成功，则返回对应的关联关系对象；否则返回 null
     */
    RelationshipEntity saveRelationship(String projectId, RelationshipDTO relationshipDTO);

    /**
     * 从指定的工程当中删除一个关联关系的信息
     *
     * @param projectId        工程编号
     * @param relationshipName 关联关系的名称
     * @return 如果成功删除对应关联关系的信息，则返回删除前的对象；否则返回 null
     */
    RelationshipEntity deleteRelationship(String projectId, String relationshipName);

    /**
     * 获得指定工程中所有关联关系的信息
     *
     * @param projectId 工程编号
     * @return 这个工程对应的关联关系的信息
     */
    List<RelationshipEntity> getRelationships(String projectId);
}
