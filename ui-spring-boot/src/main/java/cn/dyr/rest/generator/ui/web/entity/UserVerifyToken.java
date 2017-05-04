package cn.dyr.rest.generator.ui.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 存放用于验证的验证码
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@Entity
@Table(name = "USER_VERIFY_TOKEN")
public class UserVerifyToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private int type;

    @Column
    private String token;

    @Column
    private long expired;

    @ManyToOne
    private UserEntity user;

    public UserVerifyToken() {
        this.expired = System.currentTimeMillis() + 7200000;
    }

    public long getId() {
        return id;
    }

    public UserVerifyToken setId(long id) {
        this.id = id;
        return this;
    }

    public int getType() {
        return type;
    }

    public UserVerifyToken setType(int type) {
        this.type = type;
        return this;
    }

    public String getToken() {
        return token;
    }

    public UserVerifyToken setToken(String token) {
        this.token = token;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public UserVerifyToken setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public long getExpired() {
        return expired;
    }

    public UserVerifyToken setExpired(long expired) {
        this.expired = expired;
        return this;
    }
}
