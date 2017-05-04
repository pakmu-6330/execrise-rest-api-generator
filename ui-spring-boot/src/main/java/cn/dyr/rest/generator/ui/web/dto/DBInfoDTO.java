package cn.dyr.rest.generator.ui.web.dto;

import cn.dyr.rest.generator.ui.web.entity.DBInfoEntity;
import cn.dyr.rest.generator.ui.web.entity.ProjectEntity;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

/**
 * 数据库信息对应的 DTO
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DBInfoDTO {

    private String projectId;
    private String host;
    private String username;
    private String password;
    private int type;

    /**
     * 提取项目信息得到相应的 DTO 类
     *
     * @param projectEntity 项目实体对象
     * @return 对应的 DTO
     */
    public static DBInfoDTO fromProject(ProjectEntity projectEntity) {
        Objects.requireNonNull(projectEntity, "project entity is null");

        DBInfoDTO retValue = new DBInfoDTO();
        DBInfoEntity dbInfoEntity = projectEntity.getDbInfoEntity();

        BeanUtils.copyProperties(dbInfoEntity, dbInfoEntity);
        retValue.setProjectId(projectEntity.getProjectId());

        return retValue;
    }

    /**
     * 由数据库实体类得到相应的 DTO 对象
     *
     * @param entity 实体类对象
     * @return 对应的 DTO 对象
     */
    public static DBInfoDTO fromDBInfoEntity(DBInfoEntity entity) {
        Objects.requireNonNull(entity, "entity is null");

        DBInfoDTO retValue = new DBInfoDTO();
        BeanUtils.copyProperties(entity, retValue);

        return retValue;
    }

    public String getProjectId() {
        return projectId;
    }

    public DBInfoDTO setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getHost() {
        return host;
    }

    public DBInfoDTO setHost(String host) {
        this.host = host;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public DBInfoDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DBInfoDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    public int getType() {
        return type;
    }

    public DBInfoDTO setType(int type) {
        this.type = type;
        return this;
    }
}
