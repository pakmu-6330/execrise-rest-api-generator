package cn.dyr.rest.generator.project;

import cn.dyr.rest.generator.converter.ConverterConfig;
import cn.dyr.rest.generator.converter.ConverterContext;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.entity.EntityRelationship;
import cn.dyr.rest.generator.framework.spring.boot.SpringBootMavenFactory;
import cn.dyr.rest.generator.framework.spring.boot.SpringBootVersion;
import cn.dyr.rest.generator.framework.swagger.SwaggerMavenFactory;
import cn.dyr.rest.generator.io.SourceWriter;
import cn.dyr.rest.generator.java.generator.DefaultCodeGenerator;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.project.database.IJdbcConfig;
import cn.dyr.rest.generator.xml.XMLBuilder;
import cn.dyr.rest.generator.xml.maven.MavenCoordinate;
import cn.dyr.rest.generator.xml.maven.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import static cn.dyr.rest.generator.framework.spring.boot.SpringBootProperties.DATA_SOURCE_DRIVER_CLASS_NAME;
import static cn.dyr.rest.generator.framework.spring.boot.SpringBootProperties.DATA_SOURCE_PASSWORD;
import static cn.dyr.rest.generator.framework.spring.boot.SpringBootProperties.DATA_SOURCE_URL;
import static cn.dyr.rest.generator.framework.spring.boot.SpringBootProperties.DATA_SOURCE_USERNAME;
import static cn.dyr.rest.generator.framework.spring.boot.SpringBootProperties.JPA_HIBERNATE_DDL_AUTO;
import static cn.dyr.rest.generator.framework.spring.boot.SpringBootProperties.JPA_HIBERNATE_DDL_AUTO_VALUE_UPDATE;
import static cn.dyr.rest.generator.framework.spring.boot.SpringBootProperties.JPA_SHOW_SQL;

/**
 * 项目生成上下文
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ProjectGenerationContext {

    private static Logger logger;

    static {
        logger = LoggerFactory.getLogger(ProjectGenerationContext.class);
    }

    private Project project;
    private List<EntityInfo> entityInfoList;
    private List<EntityRelationship> relationshipList;

    private String tablePrefix;
    private String uriPrefix;

    public ProjectGenerationContext() {
        this.entityInfoList = new ArrayList<>();
        this.relationshipList = new ArrayList<>();
    }

    public Project getProject() {
        return project;
    }

    public ProjectGenerationContext setProject(Project project) {
        this.project = project;
        return this;
    }

    /**
     * 往当前的项目生成环境当中添加一个实体类信息
     *
     * @param entityInfo 要添加的实体类信息
     * @return 这个项目生成环境本身
     */
    public ProjectGenerationContext addEntityInfo(EntityInfo entityInfo) {
        this.entityInfoList.add(entityInfo);
        return this;
    }

    /**
     * 往当前的项目生成环境当中添加一个实体关系信息
     *
     * @param relationship 要添加的关系信息
     * @return 这个项目生成环境本身
     */
    public ProjectGenerationContext addEntityRelationship(EntityRelationship relationship) {
        this.relationshipList.add(relationship);
        return this;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public ProjectGenerationContext setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
        return this;
    }

    public String getUriPrefix() {
        return uriPrefix;
    }

    public ProjectGenerationContext setUriPrefix(String uriPrefix) {
        this.uriPrefix = uriPrefix;
        return this;
    }

    private void saveApplicationProperties(SourceWriter writer) throws IOException {
        IJdbcConfig config = project.getJdbcConfig();

        Properties properties = new Properties();
        properties.setProperty(DATA_SOURCE_DRIVER_CLASS_NAME, config.getDriverClassName());
        properties.setProperty(DATA_SOURCE_URL, config.getJdbcUrl());
        properties.setProperty(DATA_SOURCE_PASSWORD, config.getPassword());
        properties.setProperty(DATA_SOURCE_USERNAME, config.getUsername());

        properties.setProperty(JPA_HIBERNATE_DDL_AUTO, JPA_HIBERNATE_DDL_AUTO_VALUE_UPDATE);
        properties.setProperty(JPA_SHOW_SQL, "true");

        writer.writePropertiesToResourceDir("application.properties", properties);
    }

    /**
     * 根据项目信息生成相应的 Maven 项目信息
     *
     * @param project 项目信息
     * @return 对应的 Maven 项目信息
     */
    private MavenProject fromProject(Project project) {
        MavenProject retValue = new MavenProject();
        MavenCoordinate thisProject = new MavenCoordinate();

        // 创建本项目的信息
        thisProject.setGroupId(project.getAuthor());
        thisProject.setArtifactId(project.getProjectName());
        thisProject.setVersion(project.getVersion());
        thisProject.setPackaging(MavenCoordinate.PACKAGE_JAR);

        // 将本项目的 POM 文件继承 spring boot
        retValue.setCoordinate(thisProject);
        retValue.setName(project.getProjectName());
        retValue.setParent(SpringBootMavenFactory.parentPOM(SpringBootVersion.VER_1_5_2));

        // 往项目中添加一些需要的 starter 依赖
        retValue.addDependency(SpringBootMavenFactory.tomcatStarter());
        retValue.addDependency(SpringBootMavenFactory.jpaStarter());
        retValue.addDependency(SpringBootMavenFactory.hateoasStarter());
        retValue.addDependency(SpringBootMavenFactory.mysqlStarter());
        retValue.addDependency(SpringBootMavenFactory.testStarter());

        // Swagger 相关依赖
        retValue.addDependency(SwaggerMavenFactory.swagger());
        retValue.addDependency(SwaggerMavenFactory.swaggerUi());

        // 添加相应的插件
        retValue.addPlugin(SpringBootMavenFactory.mavenPlugin());

        return retValue;
    }

    /**
     * 在指定的路径中生成项目代码
     *
     * @param targetDir 目标代码文件存放的路径
     * @throws IOException 如果在写文件过程中发生错误，则会抛出这个异常
     */
    public void generate(File targetDir) throws IOException {
        SourceWriter writer = new SourceWriter();
        writer.init(targetDir);

        // 0. 检查一些项目信息是否有效
        if (this.project == null) {
            throw new IllegalArgumentException("project info is null");
        }

        // 1. 生成 pom 文件
        XMLBuilder xmlBuilder = new XMLBuilder();
        String pomContent = xmlBuilder.buildMavenPOM(fromProject(this.project));
        writer.writePOM(pomContent);

        // 2. 生成配置文件
        saveApplicationProperties(writer);

        // 3. 生成代码文件
        DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator();
        ConverterContext converterContext = new ConverterContext(this.project);

        // 3.0. 将代码生成配置信息应用到生成器当中
        ConverterConfig config = converterContext.getConverterConfig();
        config.setTablePrefix(tablePrefix);
        config.setUriPrefix(uriPrefix);

        // 3.1. 实体信息 -> 类元信息 -> 实体类对应的代码
        for (EntityInfo entityInfo : this.entityInfoList) {
            converterContext.addEntityInfo(entityInfo);
        }

        for (EntityRelationship relationship : this.relationshipList) {
            converterContext.addEntityRelationship(relationship);
        }

        converterContext.generate();
        Iterator<ClassInfo> classInfoIterator = converterContext.iterateClass();
        while (classInfoIterator.hasNext()) {
            ClassInfo classInfo = classInfoIterator.next();
            writer.writeSource(classInfo, codeGenerator.generateSingleClass(classInfo));
        }

    }
}
