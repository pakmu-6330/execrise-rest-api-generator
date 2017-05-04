package cn.dyr.rest.generator.ui.swing.persist;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;
import java.util.UUID;

/**
 * 用于保存工程的项目对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@XmlRootElement
@XmlType(name = "project", propOrder = {
        "id", "projectName", "authorName", "version", "packageName",
        "dbType", "dbName", "dbHost", "dbUsername", "dbPassword",
        "entities", "relationships"})
public class XMLProject {

    private String id;

    private String projectName;
    private String authorName;
    private String version;
    private String packageName;

    private String dbType;
    private String dbName;
    private String dbHost;
    private String dbUsername;
    private String dbPassword;

    private List<XMLEntity> entities;
    private List<XMLRelationship> relationships;

    public XMLProject() {
        this.id = UUID.randomUUID().toString();
    }

    @XmlElement
    public String getId() {
        return id;
    }

    @XmlElement
    public String getProjectName() {
        return projectName;
    }

    public XMLProject setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    @XmlElement
    public String getAuthorName() {
        return authorName;
    }

    public XMLProject setAuthorName(String authorName) {
        this.authorName = authorName;
        return this;
    }

    @XmlElement
    public String getVersion() {
        return version;
    }

    public XMLProject setVersion(String version) {
        this.version = version;
        return this;
    }

    @XmlElement
    public String getPackageName() {
        return packageName;
    }

    public XMLProject setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    @XmlElement
    public String getDbType() {
        return dbType;
    }

    public XMLProject setDbType(String dbType) {
        this.dbType = dbType;
        return this;
    }

    @XmlElement
    public String getDbName() {
        return dbName;
    }

    public XMLProject setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    @XmlElement
    public String getDbHost() {
        return dbHost;
    }

    public XMLProject setDbHost(String dbHost) {
        this.dbHost = dbHost;
        return this;
    }

    @XmlElement
    public String getDbUsername() {
        return dbUsername;
    }

    public XMLProject setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
        return this;
    }

    @XmlElement
    public String getDbPassword() {
        return dbPassword;
    }

    public XMLProject setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
        return this;
    }

    @XmlElement(name = "entity")
    @XmlElementWrapper(name = "entities")
    public List<XMLEntity> getEntities() {
        return entities;
    }

    public XMLProject setEntities(List<XMLEntity> entities) {
        this.entities = entities;
        return this;
    }

    @XmlElement(name = "relationship")
    @XmlElementWrapper(name = "relationships")
    public List<XMLRelationship> getRelationships() {
        return relationships;
    }

    public XMLProject setRelationships(List<XMLRelationship> relationships) {
        this.relationships = relationships;
        return this;
    }
}
