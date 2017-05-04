package cn.dyr.rest.generator.ui.web.dao;

import cn.dyr.rest.generator.ui.web.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 用于操作用户对象的 DAO 类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IUserDAO extends PagingAndSortingRepository<UserEntity, Long> {

    /**
     * 根据电子邮箱查找用户
     *
     * @param email 电子邮箱地址
     * @return 如果这个电子邮箱地址已经存在，则返回相应的对象；否则返回 null
     */
    UserEntity findByEmail(String email);

    /**
     * 根据用户名和密码查找对应的用户实体对象
     *
     * @param username 用户名
     * @param password 密码
     * @return 如果用户名和密码作为条件指定的用户实体存在，则返回用户实体；否则返回 null
     */
    UserEntity findByUsernameAndPassword(String username, String password);

}
