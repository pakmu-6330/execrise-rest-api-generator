package cn.dyr.rest.generator.xml.maven.factory;

import cn.dyr.rest.generator.xml.maven.MavenCoordinate;
import cn.dyr.rest.generator.xml.maven.MavenPlugin;
import cn.dyr.rest.generator.xml.maven.MavenPluginConfiguration;
import cn.dyr.rest.generator.xml.maven.MavenPluginConfigurationKVImpl;

/**
 * 用于创建 Maven 插件信息的工厂类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class MavenPluginFactory {

    /**
     * maven 编译器插件的 groupId
     */
    public static final String GROUP_ID_COMPILER_PLUGIN = "org.apache.maven.plugins";

    /**
     * maven 编译器插件的 artifactId
     */
    public static final String ARTIFACT_ID_COMPILER_PLUGIN = "maven-compiler-plugin";

    /**
     * 用于 maven 编译器插件表示 jdk 1.5 版本的字符串
     */
    public static final String CONF_VERSION_JDK15 = "1.5";

    /**
     * 用于 maven 编译器插件表示 jdk 1.6 版本的字符串
     */
    public static final String CONF_VERSION_JDK16 = "1.6";

    /**
     * 用于 maven 编译器插件表示 jdk 1.7 版本的字符串
     */
    public static final String CONF_VERSION_JDK17 = "1.7";

    /**
     * 用于 maven 编译器插件表示 jdk 1.8 版本的字符串
     */
    public static final String CONF_VERISON_JDK18 = "1.8";

    /**
     * 创建一个编译器的插件信息
     *
     * @param sourceVersion source 值
     * @param targetVersion target 值
     * @return 相应的插件信息
     */
    public static MavenPlugin compilerPlugin(String sourceVersion, String targetVersion) {
        MavenCoordinate coordinate = new MavenCoordinate()
                .setGroupId(GROUP_ID_COMPILER_PLUGIN)
                .setArtifactId(ARTIFACT_ID_COMPILER_PLUGIN);
        MavenPluginConfiguration configuration = new MavenPluginConfigurationKVImpl()
                .putConfiguration("source", sourceVersion)
                .putConfiguration("target", targetVersion);

        return new MavenPlugin()
                .setCoordinate(coordinate)
                .setConfiguration(configuration);
    }
}
