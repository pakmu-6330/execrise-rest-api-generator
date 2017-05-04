package cn.dyr.rest.generator.ui.web.dao;

import cn.dyr.rest.generator.ui.web.entity.RelationshipEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 关联关系相关的 DAO 接口
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IRelationshipDAO extends PagingAndSortingRepository<RelationshipEntity, Long> {

    @Query("select r from ProjectEntity p inner join p.relationshipList r where p.projectId=?1 and r.relationshipId=?2")
    RelationshipEntity findByRelationshipIdAndProjectId(String projectId, String relationshipId);

    @Query("select r from ProjectEntity p inner join p.relationshipList r where p.projectId=?1 and r.endA.name=?2 and r.endAAttributeName=?3")
    RelationshipEntity findAInProject(String projectId, String entityName, String attributeName);

    @Query("select r from ProjectEntity p inner join p.relationshipList r where p.projectId=?1 and r.endB.name=?2 and r.endBAttributeName=?3")
    RelationshipEntity findBInProject(String projectId, String entityName, String attributeName);

}
