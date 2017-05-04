package cn.dyr.rest.generator.ui.web.service;

import cn.dyr.rest.generator.ui.web.common.APIResponse;
import cn.dyr.rest.generator.ui.web.dto.UserDTO;
import cn.dyr.rest.generator.ui.web.entity.UserEntity;

/**
 * 用户 Service 接口
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IUserService {

    /**
     * 用户注册
     *
     * @param userEntity 存放用户信息的实体
     * @return 用户注册的结果
     */
    APIResponse<UserDTO> register(UserEntity userEntity);

    /**
     * 对验证码进行相应的处理
     *
     * @param verifyToken 要进行处理的验证码
     * @return 返回值
     */
    APIResponse<UserDTO> doVerify(String verifyToken);

}
