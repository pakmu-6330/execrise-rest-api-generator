package cn.dyr.rest.generator.ui.swing.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * 与界面相对应的数据库基本信息
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DBInfoModel implements Serializable, UUIDIdentifier {

    private String id;

    private String dbType;
    private String dbName;
    private String dbHost;
    private String dbUsername;
    private String dbPassword;

    public DBInfoModel() {
        this.dbType = "MySQL";
        this.dbHost = "localhost";
        this.dbName = "spring";
        this.dbUsername = "root";
        this.dbPassword = "popkart.alex9498";

        this.id = UUID.randomUUID().toString();
    }

    @Override
    public String getId() {
        return id;
    }

    public String getDbType() {
        return dbType;
    }

    public DBInfoModel setDbType(String dbType) {
        this.dbType = dbType;
        return this;
    }

    public String getDbName() {
        return dbName;
    }

    public DBInfoModel setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public String getDbHost() {
        return dbHost;
    }

    public DBInfoModel setDbHost(String dbHost) {
        this.dbHost = dbHost;
        return this;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public DBInfoModel setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
        return this;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public DBInfoModel setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
        return this;
    }
}
