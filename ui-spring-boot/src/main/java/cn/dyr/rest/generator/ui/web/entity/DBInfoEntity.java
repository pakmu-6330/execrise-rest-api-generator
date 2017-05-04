package cn.dyr.rest.generator.ui.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 这个实体用于保存对应数据库配置信息的实体对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@Entity
@Table(name = "DBINFO")
public class DBInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private int type;

    @Column
    private String host;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String dbName;

    public long getId() {
        return id;
    }

    public DBInfoEntity setId(long id) {
        this.id = id;
        return this;
    }

    public int getType() {
        return type;
    }

    public DBInfoEntity setType(int type) {
        this.type = type;
        return this;
    }

    public String getHost() {
        return host;
    }

    public DBInfoEntity setHost(String host) {
        this.host = host;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public DBInfoEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DBInfoEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getDbName() {
        return dbName;
    }

    public DBInfoEntity setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }
}
