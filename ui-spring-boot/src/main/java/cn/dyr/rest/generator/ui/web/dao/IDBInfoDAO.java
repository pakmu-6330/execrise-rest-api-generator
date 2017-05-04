package cn.dyr.rest.generator.ui.web.dao;

import cn.dyr.rest.generator.ui.web.entity.DBInfoEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 工程数据库信息相关的 DAO 类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IDBInfoDAO extends PagingAndSortingRepository<DBInfoEntity, Long> {



}
