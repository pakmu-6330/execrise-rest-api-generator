package cn.dyr.rest.generator.ui.swing.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * 与界面对应的工程基本信息
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class BasicInfoModel implements Serializable, UUIDIdentifier {

    private String id;

    private String projectName;
    private String authorName;
    private String version;
    private String packageName;

    public BasicInfoModel() {
        this.projectName = "project";
        this.authorName = "com.example";
        this.version = "1.0-SNAPSHOT";
        this.packageName = "com.example.project";

        this.id = UUID.randomUUID().toString();
    }

    @Override
    public String getId() {
        return id;
    }

    public String getProjectName() {
        return projectName;
    }

    public BasicInfoModel setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public String getAuthorName() {
        return authorName;
    }

    public BasicInfoModel setAuthorName(String authorName) {
        this.authorName = authorName;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public BasicInfoModel setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getPackageName() {
        return packageName;
    }

    public BasicInfoModel setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }
}
