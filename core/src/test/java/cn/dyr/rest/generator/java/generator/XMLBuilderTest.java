package cn.dyr.rest.generator.java.generator;

import cn.dyr.rest.generator.xml.XMLBuilder;
import cn.dyr.rest.generator.xml.maven.MavenCoordinate;
import cn.dyr.rest.generator.xml.maven.MavenDependency;
import cn.dyr.rest.generator.xml.maven.MavenParent;
import cn.dyr.rest.generator.xml.maven.MavenPlugin;
import cn.dyr.rest.generator.xml.maven.MavenPluginConfigurationKVImpl;
import cn.dyr.rest.generator.xml.maven.MavenProject;
import org.junit.Test;

/**
 * XML 生成工具的测试
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class XMLBuilderTest {

    @Test
    public void testBasic0() {
        XMLBuilder builder = new XMLBuilder();

        // 父 pom 文件的一些信息
        MavenCoordinate parentCoordinate = new MavenCoordinate()
                .setGroupId("org.springframework.boot")
                .setArtifactId("spring-boot-starter-parent")
                .setVersion("1.5.1.RELEASE");
        MavenParent parent = new MavenParent()
                .setCoordinate(parentCoordinate)
                .setRelativePath(null);

        // tomcat starter
        MavenCoordinate tomcatStarter = new MavenCoordinate()
                .setGroupId("org.springframework.boot")
                .setArtifactId("spring-boot-starter-tomcat");
        MavenDependency tomcatStarterDependency = new MavenDependency()
                .setCoordinate(tomcatStarter);

        // jdbc starter
        MavenCoordinate jdbcStarter = new MavenCoordinate()
                .setGroupId("org.springframework.boot")
                .setArtifactId("spring-boot-starter-jdbc");
        MavenDependency jdbcStarterDependency = new MavenDependency()
                .setCoordinate(jdbcStarter);

        // web starter
        MavenCoordinate webStarter = new MavenCoordinate()
                .setGroupId("org.springframework.boot")
                .setArtifactId("spring-boot-starter-web");
        MavenDependency webStarterDependency = new MavenDependency()
                .setCoordinate(webStarter);

        // slf4j
        MavenCoordinate slf4j = new MavenCoordinate()
                .setGroupId("org.slf4j")
                .setArtifactId("slf4j-api");
        MavenDependency slf4jDependency = new MavenDependency()
                .setCoordinate(slf4j);

        // mysql jdbc 驱动
        MavenCoordinate mysqlJdbc = new MavenCoordinate()
                .setGroupId("mysql")
                .setArtifactId("mysql-connector-java");
        MavenDependency mysqlJdbcDependency = new MavenDependency()
                .setCoordinate(mysqlJdbc)
                .setScope(MavenDependency.SCOPE_RUNTIME);

        // test
        MavenCoordinate testStarter = new MavenCoordinate()
                .setGroupId("org.springframework.boot")
                .setArtifactId("spring-boot-starter-test");
        MavenDependency testStarterDependency = new MavenDependency()
                .setCoordinate(testStarter)
                .setScope(MavenDependency.SCOPE_TEST);

        // spring-boot 插件
        MavenCoordinate springBootPluginCoordinate = new MavenCoordinate()
                .setArtifactId("spring-boot-maven-plugin")
                .setGroupId("org.springframework.boot");
        MavenPlugin springBootPlugin = new MavenPlugin()
                .setCoordinate(springBootPluginCoordinate);

        // maven war 插件
        MavenCoordinate warMavenPluginCoordinate = new MavenCoordinate()
                .setArtifactId("maven-war-plugin")
                .setGroupId("org.apache.maven.plugins");
        MavenPluginConfigurationKVImpl warPluginConfiguration = new MavenPluginConfigurationKVImpl()
                .putConfiguration("warName", "dnsreport");
        MavenPlugin warMavenPlugin = new MavenPlugin()
                .setCoordinate(warMavenPluginCoordinate)
                .setConfiguration(warPluginConfiguration);

        // maven 配置本身的一些信息
        MavenCoordinate coordinate = new MavenCoordinate()
                .setGroupId("cn.dyr")
                .setArtifactId("dns-log-report")
                .setVersion("0.1.0005")
                .setPackaging(MavenCoordinate.PACKAGE_WAR);
        MavenProject project = new MavenProject()
                .setCoordinate(coordinate)
                .setName("dns-log-report")
                .setDescription("Report Webapp For DNS Log analysis")
                .setParent(parent)
                .putProperty("project.build.sourceEncoding", "UTF-8")
                .putProperty("project.reporting.outputEncoding", "UTF-8")
                .putProperty("java.version", "1.8")
                .addDependency(tomcatStarterDependency)
                .addDependency(jdbcStarterDependency)
                .addDependency(webStarterDependency)
                .addDependency(slf4jDependency)
                .addDependency(mysqlJdbcDependency)
                .addDependency(testStarterDependency)
                .addPlugin(springBootPlugin)
                .addPlugin(warMavenPlugin);

        String xml = builder.buildMavenPOM(project);
        System.out.println(xml);
    }

}
