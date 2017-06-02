package cn.dyr.rest.generator.ui.swing.model;

import java.util.UUID;

/**
 * 用于存放项目生成数据的一些配置信息
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ProjectConfigModel implements UUIDIdentifier {
    private String id;

    private String uriPrefix;
    private String tablePrefix;

    public ProjectConfigModel() {
        this.id = UUID.randomUUID().toString();
        this.uriPrefix = "";
        this.tablePrefix = "";
    }

    @Override
    public String getId() {
        return this.id;
    }

    public String getUriPrefix() {
        return uriPrefix;
    }

    public ProjectConfigModel setUriPrefix(String uriPrefix) {
        this.uriPrefix = uriPrefix;
        return this;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public ProjectConfigModel setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
        return this;
    }
}
