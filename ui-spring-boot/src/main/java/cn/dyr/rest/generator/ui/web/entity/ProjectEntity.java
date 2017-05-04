package cn.dyr.rest.generator.ui.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * 表示一个工程的实体信息
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@Entity
@Table(name = "PROJECT")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String projectId;

    @Column
    private String displayName;

    @Column
    private String developerName;

    @Column
    private String projectName;

    @Column
    private String version;

    @Column
    private String packageName;

    @Column
    private int status;

    @OneToMany
    @JoinColumn(name = "ref_project")
    private List<EntityEntity> entityList;

    @OneToMany
    @JoinColumn(name = "ref_project")
    private List<RelationshipEntity> relationshipList;

    @OneToOne
    private DBInfoEntity dbInfoEntity;

    @ManyToOne
    private UserEntity user;

    @OneToOne
    private JobEntity job;

    public ProjectEntity() {
        this.entityList = new ArrayList<>();
        this.relationshipList = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public ProjectEntity setId(long id) {
        this.id = id;
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public ProjectEntity setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ProjectEntity setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public ProjectEntity setDeveloperName(String developerName) {
        this.developerName = developerName;
        return this;
    }

    public String getProjectName() {
        return projectName;
    }

    public ProjectEntity setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public ProjectEntity setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getPackageName() {
        return packageName;
    }

    public ProjectEntity setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public List<EntityEntity> getEntityList() {
        return entityList;
    }

    public ProjectEntity setEntityList(List<EntityEntity> entityList) {
        this.entityList = entityList;
        return this;
    }

    public List<RelationshipEntity> getRelationshipList() {
        return relationshipList;
    }

    public int getStatus() {
        return status;
    }

    public ProjectEntity setStatus(int status) {
        this.status = status;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public ProjectEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public ProjectEntity setRelationshipList(List<RelationshipEntity> relationshipList) {
        this.relationshipList = relationshipList;
        return this;
    }

    public DBInfoEntity getDbInfoEntity() {
        return dbInfoEntity;
    }

    public ProjectEntity setDbInfoEntity(DBInfoEntity dbInfoEntity) {
        this.dbInfoEntity = dbInfoEntity;
        return this;
    }

    public JobEntity getJob() {
        return job;
    }

    public ProjectEntity setJob(JobEntity job) {
        this.job = job;
        return this;
    }
}
