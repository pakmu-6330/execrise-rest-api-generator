package cn.dyr.rest.generator.ui.web.dao;

import cn.dyr.rest.generator.ui.web.entity.AttributeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 属性相关的 DAO
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IAttributeDAO extends PagingAndSortingRepository<AttributeEntity, Long> {

    @Query("select a from ProjectEntity p inner join p.entityList e inner join e.attributes a where p.projectId=?1 and e.name=?2 and a.name=?3")
    AttributeEntity findInProjectAndEntity(String projectId, String entityName, String attributeName);

}
