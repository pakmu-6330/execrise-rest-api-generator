package cn.dyr.rest.generator.ui.web.controller;

import cn.dyr.rest.generator.ui.web.common.APIResponse;
import cn.dyr.rest.generator.ui.web.common.TokenSession;
import cn.dyr.rest.generator.ui.web.constant.ResponseConstants;
import cn.dyr.rest.generator.ui.web.dto.UserDTO;
import cn.dyr.rest.generator.ui.web.entity.UserEntity;
import cn.dyr.rest.generator.ui.web.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 这是一个用户相关操作的控制器
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping(value = "")
    public HttpEntity<APIResponse<UserDTO>> register(UserEntity userEntity) {
        APIResponse<UserDTO> serviceResponse = userService.register(userEntity);
        if (serviceResponse.getMeta().getCode() != ResponseConstants.RESPONSE_OK) {
            return new ResponseEntity<>(serviceResponse, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
    }

    @PostMapping("/verify/{code}")
    public HttpEntity<APIResponse<UserDTO>> verify(
            @PathVariable("code") String verifyCode
    ) {
        APIResponse<UserDTO> verifyResponse = userService.doVerify(verifyCode);
        if (verifyResponse.getMeta().getCode() != ResponseConstants.RESPONSE_OK) {
            return new ResponseEntity<>(verifyResponse, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(verifyResponse, HttpStatus.OK);
        }
    }

}
