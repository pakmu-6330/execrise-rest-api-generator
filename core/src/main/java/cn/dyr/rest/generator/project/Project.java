package cn.dyr.rest.generator.project;

import cn.dyr.rest.generator.project.database.IJdbcConfig;

/**
 * 用于封装项目信息的类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class Project {

    private String author;
    private String projectName;
    private String version;
    private IJdbcConfig jdbcConfig;
    private String basePackage;

    public String getAuthor() {
        return author;
    }

    public Project setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getProjectName() {
        return projectName;
    }

    public Project setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public Project setVersion(String version) {
        this.version = version;
        return this;
    }

    public IJdbcConfig getJdbcConfig() {
        return jdbcConfig;
    }

    public Project setJdbcConfig(IJdbcConfig jdbcConfig) {
        this.jdbcConfig = jdbcConfig;
        return this;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public Project setBasePackage(String basePackage) {
        this.basePackage = basePackage;
        return this;
    }
}
