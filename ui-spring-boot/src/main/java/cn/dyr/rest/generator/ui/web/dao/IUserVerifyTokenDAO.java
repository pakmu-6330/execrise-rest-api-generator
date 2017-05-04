package cn.dyr.rest.generator.ui.web.dao;

import cn.dyr.rest.generator.ui.web.entity.UserVerifyToken;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 用于操作用户操作验证码的 DAO
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IUserVerifyTokenDAO extends PagingAndSortingRepository<UserVerifyToken, Long> {

    /**
     * 根据验证码寻找相应验证操作信息
     *
     * @param token 验证码
     * @return 如果这个验证码存在且有效，则返回这个信息；否则返回 null
     */
    UserVerifyToken findByToken(String token);

}
