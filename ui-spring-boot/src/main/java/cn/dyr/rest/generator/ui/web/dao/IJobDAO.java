package cn.dyr.rest.generator.ui.web.dao;

import cn.dyr.rest.generator.ui.web.entity.JobEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 代码生成作业相关的 DAO 接口
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IJobDAO extends PagingAndSortingRepository<JobEntity, Long> {

}
