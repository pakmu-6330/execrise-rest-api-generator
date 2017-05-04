package cn.dyr.rest.generator.ui.web.dao;

import cn.dyr.rest.generator.ui.web.entity.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * 项目信息 DAO
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IProjectDAO extends PagingAndSortingRepository<ProjectEntity, Long> {

    /**
     * 根据工程的流水号查找对应的工程信息
     *
     * @param projectId 工程流水号
     * @return 对应的工程信息
     */
    ProjectEntity findByProjectId(String projectId);

    /**
     * 根据用户编号寻找工程信息
     *
     * @param userId 用户编号
     * @return 这个用户对应的所有的工程信息
     */
    @Query("select p from UserEntity u inner join u.projects p where u.id=?1")
    List<ProjectEntity> findByUser(long userId);

    /**
     * 根据用户编号寻找工程信息
     *
     * @param userId   用户编号
     * @param pageable 分页信息
     * @return 这个用户对应的工程信息
     */
    @Query("select p from UserEntity u inner join u.projects p where u.id=?1")
    Page<ProjectEntity> findByUser(long userId, Pageable pageable);

}
