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

    private int port;
    private int pageSize;

    public ProjectConfigModel() {
        this.id = UUID.randomUUID().toString();

        this.uriPrefix = "";
        this.tablePrefix = "";
        this.port = 8080;
        this.pageSize = 5;
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

    public int getPort() {
        return port;
    }

    public ProjectConfigModel setPort(int port) {
        this.port = port;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public ProjectConfigModel setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }
}
