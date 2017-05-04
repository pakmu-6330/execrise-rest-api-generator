package cn.dyr.rest.generator.ui.web.service.impl;

import cn.dyr.rest.generator.ui.web.common.APIResponse;
import cn.dyr.rest.generator.ui.web.common.ResponseMeta;
import cn.dyr.rest.generator.ui.web.common.factory.ResponseMetaFactory;
import cn.dyr.rest.generator.ui.web.component.IMailComponent;
import cn.dyr.rest.generator.ui.web.constant.EntityConstants;
import cn.dyr.rest.generator.ui.web.dao.IUserDAO;
import cn.dyr.rest.generator.ui.web.dao.IUserVerifyTokenDAO;
import cn.dyr.rest.generator.ui.web.dto.UserDTO;
import cn.dyr.rest.generator.ui.web.entity.UserEntity;
import cn.dyr.rest.generator.ui.web.entity.UserVerifyToken;
import cn.dyr.rest.generator.ui.web.service.IUserService;
import cn.dyr.rest.generator.ui.web.util.TokenGenerator;
import cn.dyr.rest.generator.ui.web.validate.ServiceValidateToolKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User Service 实现类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@Service
public class UserService implements IUserService {

    @Autowired
    private IUserDAO userDAO;

    @Autowired
    private IUserVerifyTokenDAO userVerifyTokenDAO;

    @Autowired
    private IMailComponent mailComponent;

    @Override
    @Transactional
    public APIResponse<UserDTO> register(UserEntity userEntity) {
        // 0. 检查传入参数是否有误
        String email = userEntity.getEmail();
        ServiceValidateToolKit.requireStringNotEmpty(email, "email is null");

        String username = userEntity.getUsername();
        ServiceValidateToolKit.requireStringNotEmpty(username, "username is null");

        String password = userEntity.getPassword();
        ServiceValidateToolKit.requireStringNotEmpty(password, "password is null");

        // 1. 检查邮箱地址是否已经存在
        UserEntity exists = this.userDAO.findByEmail(email);
        if (exists != null) {
            ResponseMeta meta = ResponseMetaFactory.duplicateMeta("email is used");
            return new APIResponse<>(meta, new UserDTO());
        }

        // 2. 保存信息
        userEntity.setStatus(EntityConstants.UserEntityConstant.USER_STATUS_TO_BE_VERIFIED);
        this.userDAO.save(userEntity);

        // 3. 生成相应的验证码并保存到数据当中
        String token = TokenGenerator.tokenGenerate();

        UserVerifyToken tokenObject = new UserVerifyToken();
        tokenObject.setToken(token);
        tokenObject.setType(EntityConstants.UserVerifyTokenConstant.VERIFY_CODE_TYPE_REGISTER);
        tokenObject.setUser(userEntity);
        this.userVerifyTokenDAO.save(tokenObject);

        // 4. 发送邮件
        this.mailComponent.sendVerifyEmail(email, username, token);

        return new APIResponse<>(ResponseMetaFactory.okMeta(), UserDTO.fromUserEntity(userEntity));
    }

    @Transactional
    @Override
    public APIResponse<UserDTO> doVerify(String verifyToken) {
        // 1. 寻找验证码
        UserVerifyToken token = this.userVerifyTokenDAO.findByToken(verifyToken);
        if (token == null) {
            ResponseMeta meta = ResponseMetaFactory.expiredMeta();
            return new APIResponse<>(meta, new UserDTO());
        }

        // 2. 根据不同的类型做出不同的处理
        int type = token.getType();
        UserEntity user = token.getUser();

        switch (type) {
            case EntityConstants.UserVerifyTokenConstant.VERIFY_CODE_TYPE_REGISTER:
                if (user != null) {
                    user.setStatus(EntityConstants.UserEntityConstant.USER_STATUS_VERIFIED);
                    this.userDAO.save(user);
                } else {
                    ResponseMeta meta = ResponseMetaFactory.expiredMeta();
                    return new APIResponse<>(meta, new UserDTO());
                }
                break;
        }

        // 3. 从数据库中删除这个 token
        this.userVerifyTokenDAO.delete(token);

        return new APIResponse<>(ResponseMetaFactory.okMeta(), UserDTO.fromUserEntity(user));
    }
}
