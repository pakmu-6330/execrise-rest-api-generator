package cn.dyr.rest.generator.ui.web.dto;

import cn.dyr.rest.generator.ui.web.entity.EntityEntity;
import cn.dyr.rest.generator.ui.web.entity.ProjectEntity;
import cn.dyr.rest.generator.ui.web.entity.UserEntity;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ProjectEntity 对应的 DTO 对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ProjectDTO {

    private String projectId;
    private String displayName;
    private String developerName;
    private String projectName;
    private String version;
    private String packageName;
    private int status;
    private UserDTO user;
    private List<EntityDTO> entities;

    /**
     * 根据一个项目的实体信息转换成 DTO 对象
     *
     * @param projectEntity 要进行转换的项目实体
     * @return 这个项目实体对应的 DTO 对象
     */
    public static ProjectDTO fromProjectEntity(ProjectEntity projectEntity) {
        ProjectDTO retValue = new ProjectDTO();
        BeanUtils.copyProperties(projectEntity, retValue);

        UserEntity user = projectEntity.getUser();
        UserDTO userDTO = UserDTO.fromUserEntity(user);
        retValue.setUser(userDTO);

        List<EntityDTO> entityList = new ArrayList<>();
        List<EntityEntity> rawEntityList = projectEntity.getEntityList();
        if (rawEntityList != null) {
            for (EntityEntity entityEntity : rawEntityList) {
                EntityDTO dto = EntityDTO.fromEntityEntity(entityEntity);
                entityList.add(dto);
            }
        }

        retValue.setEntities(entityList);

        return retValue;
    }

    public List<EntityDTO> getEntities() {
        return entities;
    }

    public ProjectDTO setEntities(List<EntityDTO> entities) {
        this.entities = entities;
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public ProjectDTO setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ProjectDTO setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public ProjectDTO setDeveloperName(String developerName) {
        this.developerName = developerName;
        return this;
    }

    public String getProjectName() {
        return projectName;
    }

    public ProjectDTO setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public ProjectDTO setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getPackageName() {
        return packageName;
    }

    public ProjectDTO setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public ProjectDTO setStatus(int status) {
        this.status = status;
        return this;
    }

    public UserDTO getUser() {
        return user;
    }

    public ProjectDTO setUser(UserDTO user) {
        this.user = user;
        return this;
    }
}
