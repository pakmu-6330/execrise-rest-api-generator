package cn.dyr.rest.generator.framework.spring.boot;

import cn.dyr.rest.generator.xml.maven.MavenCoordinate;
import cn.dyr.rest.generator.xml.maven.MavenDependency;
import cn.dyr.rest.generator.xml.maven.MavenParent;
import cn.dyr.rest.generator.xml.maven.MavenPlugin;

/**
 * 这个类用于创建 Spring Boot 相关的 Maven 信息类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SpringBootMavenFactory {

    private static final String SPRING_BOOT_GROUP_ID = "org.springframework.boot";
    private static final String MYSQL_CONNECTOR_GROUP_ID = "mysql";

    private static final String SPRING_BOOT_STARTER_JPA = "spring-boot-starter-data-jpa";
    private static final String SPRING_BOOT_STARTER_WEB = "spring-boot-starter-web";
    private static final String SPRING_BOOT_STARTER_HATEOAS = "spring-boot-starter-hateoas";
    private static final String SPRING_BOOT_STARTER_TEST = "spring-boot-starter-test";
    private static final String SPRING_BOOT_STARTER_TOMCAT = "spring-boot-starter-tomcat";

    private static final String SPRING_BOOT_STARTER_MYSQL_CONNECTOR = "mysql-connector-java";

    private static final String PLUGIN_BOOT_PLUGIN = "spring-boot-maven-plugin";

    /**
     * 获得 Spring Boot 父 pom 的信息
     *
     * @param version Spring Boot 的版本
     * @return 指定版本的 Spring Boot 的父 pom 文件信息
     */
    public static MavenParent parentPOM(String version) {
        MavenCoordinate coordinate = new MavenCoordinate()
                .setGroupId(SPRING_BOOT_GROUP_ID)
                .setArtifactId("spring-boot-starter-parent")
                .setVersion(version);
        return new MavenParent()
                .setCoordinate(coordinate)
                .setRelativePath(null);
    }

    /**
     * 创建一个 spring boot starter 的 maven 依赖项
     *
     * @param starterId starter 的 artifactId
     * @return 对应的 Maven 依赖项
     */
    private static MavenDependency starter(String starterId) {
        MavenCoordinate coordinate = new MavenCoordinate()
                .setGroupId(SPRING_BOOT_GROUP_ID)
                .setArtifactId(starterId);
        return new MavenDependency().setCoordinate(coordinate);
    }

    /**
     * 创建一个 spring 以外的 starter maven 依赖项
     *
     * @param groupId    groupId
     * @param artifactId artifactId
     * @return 对应的 Maven 依赖项
     */
    private static MavenDependency dependency(String groupId, String artifactId) {
        MavenCoordinate coordinate = new MavenCoordinate()
                .setGroupId(groupId)
                .setArtifactId(artifactId);

        return new MavenDependency().setCoordinate(coordinate);
    }

    /**
     * 创建一个 spring data jpa starter 的 maven 依赖项
     *
     * @return 对应的 Maven 依赖项
     */
    public static MavenDependency jpaStarter() {
        return starter(SPRING_BOOT_STARTER_JPA);
    }

    /**
     * 创建一个 web starter 的 maven 依赖项
     *
     * @return 对应的 Maven 依赖项
     */
    public static MavenDependency webStarter() {
        return starter(SPRING_BOOT_STARTER_WEB);
    }

    /**
     * 创建一个 hateoas starter 的 maven 依赖项
     *
     * @return 对应的 Maven 依赖项
     */
    public static MavenDependency hateoasStarter() {
        return starter(SPRING_BOOT_STARTER_HATEOAS);
    }

    /**
     * 创建一个 mysql 依赖的 maven 依赖项
     *
     * @return 对应的 Maven 依赖项
     */
    public static MavenDependency mysqlStarter() {
        return dependency(MYSQL_CONNECTOR_GROUP_ID, SPRING_BOOT_STARTER_MYSQL_CONNECTOR)
                .setScope(MavenDependency.SCOPE_RUNTIME);
    }

    /**
     * 创建一个 tomcat starter 的 maven 依赖项
     *
     * @return 对应的 Maven 依赖项
     */
    public static MavenDependency tomcatStarter() {
        return starter(SPRING_BOOT_STARTER_TOMCAT);
    }

    /**
     * 创建一个 test starter 的 maven 依赖项
     *
     * @return 对应的 Maven 依赖项
     */
    public static MavenDependency testStarter() {
        return starter(SPRING_BOOT_STARTER_TEST).setScope(MavenDependency.SCOPE_TEST);
    }

    /**
     * 创建 Spring Boot Maven 插件信息
     *
     * @return 插件信息
     */
    public static MavenPlugin mavenPlugin() {
        MavenCoordinate coordinate = new MavenCoordinate()
                .setGroupId(SPRING_BOOT_GROUP_ID)
                .setArtifactId(PLUGIN_BOOT_PLUGIN);
        return new MavenPlugin().setCoordinate(coordinate);
    }
}
