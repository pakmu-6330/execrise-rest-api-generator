package cn.dyr.rest.generator.ui.web.dao;

import cn.dyr.rest.generator.ui.web.entity.EntityEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 实体相关的 DAO
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IEntityDAO extends PagingAndSortingRepository<EntityEntity, Long> {

    @Query("select e from ProjectEntity p inner join p.entityList e where p.projectId=?1 AND e.name=?2")
    EntityEntity findInProject(String projectId, String entityName);

}
