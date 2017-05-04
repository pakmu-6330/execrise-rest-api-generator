package cn.dyr.rest.generator.ui.web.dto;

import cn.dyr.rest.generator.ui.web.entity.UserEntity;

/**
 * 去除用户的安全等信息的安全暴露窗口
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class UserDTO {

    private long id;
    private String username;
    private int status;

    /**
     * 从一个用户的实体对象获得一个 DTO 对象
     *
     * @param userEntity 用户实体对象
     * @return 这个实体对象对应的 DTO 对象
     */
    public static UserDTO fromUserEntity(UserEntity userEntity) {
        UserDTO retValue = new UserDTO();
        retValue.setId(userEntity.getId());
        retValue.setUsername(userEntity.getUsername());
        retValue.setStatus(userEntity.getStatus());

        return retValue;
    }

    public long getId() {
        return id;
    }

    public UserDTO setId(long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public UserDTO setStatus(int status) {
        this.status = status;
        return this;
    }
}
