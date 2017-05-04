package cn.dyr.rest.generator.xml.maven;

import cn.dyr.rest.generator.util.StringUtils;
import cn.dyr.rest.generator.xml.IXMLMapping;
import org.dom4j.Branch;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 这个类表示一个 Maven 项目信息，这些信息用于生成用于 Maven 构建的 pom 文件
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class MavenProject implements IXMLMapping {

    private String name;
    private String description;

    private MavenCoordinate coordinate;
    private List<MavenDependency> dependencies;
    private Map<String, String> properties;

    private MavenParent parent;
    private List<MavenPlugin> plugins;

    public MavenProject() {
        this.dependencies = new ArrayList<>();
        this.properties = new HashMap<>();
        this.plugins = new ArrayList<>();
    }

    public MavenCoordinate getCoordinate() {
        return coordinate;
    }

    public MavenProject setCoordinate(MavenCoordinate coordinate) {
        this.coordinate = coordinate;
        return this;
    }

    public List<MavenDependency> getDependencies() {
        return dependencies;
    }

    /**
     * 往当前的 Maven 项目当中添加一个依赖信息
     *
     * @param dependency 要添加到 Maven 的依赖信息
     * @return 这个 Maven 项目本身
     */
    public MavenProject addDependency(MavenDependency dependency) {
        this.dependencies.add(dependency);
        return this;
    }

    /**
     * 往当前的 Maven 项目当中添加一个插件信息
     *
     * @param plugin 要添加到 Maven
     * @return 这个 Maven 项目本身
     */
    public MavenProject addPlugin(MavenPlugin plugin) {
        this.plugins.add(plugin);
        return this;
    }

    public MavenProject setDependencies(List<MavenDependency> dependencies) {
        this.dependencies = dependencies;
        return this;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public MavenProject setProperties(Map<String, String> properties) {
        this.properties = properties;
        return this;
    }

    /**
     * 向 Maven 的配置文件当中添加一项属性
     *
     * @param key   要添加的属性的 key
     * @param value 这个 key 对应的 value
     * @return 这个 Maven 工程本身
     */
    public MavenProject putProperty(String key, String value) {
        this.properties.put(key, value);
        return this;
    }

    /**
     * 为这个配置项创建一个占位符
     *
     * @param key 配置项
     * @return 相应的配置项
     */
    public String placeHolderForProperty(String key) {
        return String.format("${%s}", key);
    }

    public String getName() {
        return name;
    }

    public MavenProject setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public MavenProject setDescription(String description) {
        this.description = description;
        return this;
    }

    public MavenParent getParent() {
        return parent;
    }

    public MavenProject setParent(MavenParent parent) {
        this.parent = parent;
        return this;
    }

    @Override
    public void xmlMapping(Branch parentBranch) {
        Element projectNode = parentBranch.addElement("project", "http://maven.apache.org/POM/4.0.0");
        projectNode.addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        projectNode.addAttribute("xsi:schemaLocation", "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd");

        // 基本信息
        projectNode.addElement("modelVersion").addText("4.0.0");

        // 本项目的坐标
        if (this.coordinate == null) {
            throw new NullPointerException("project coordinate is null!");
        }

        this.coordinate.xmlMapping(projectNode);

        // packaging 属性，坐标本身不会添加到 dom 之中
        projectNode.addElement("package").addText(this.coordinate.getPackaging());

        // 添加 name 和 description 属性
        if (!StringUtils.isStringEmpty(this.name)) {
            projectNode.addElement("name").addText(this.name);
        }

        if (!StringUtils.isStringEmpty(this.description)) {
            projectNode.addElement("description").addText(this.description);
        }

        // 进行 project 节点的生成
        if (this.parent != null) {
            this.parent.xmlMapping(projectNode);
        }

        // 对 pom 文件的属性生成
        if (this.properties != null && this.properties.size() > 0) {
            Element propertiesElement = projectNode.addElement("properties");

            Set<String> keySet = this.properties.keySet();
            for (String key : keySet) {
                propertiesElement.addElement(key).addText(this.properties.get(key));
            }
        }

        // 对 maven 的依赖项进行管理
        if (this.dependencies != null && this.dependencies.size() > 0) {
            Element dependenciesElement = projectNode.addElement("dependencies");

            for (MavenDependency dependency : this.dependencies) {
                dependency.xmlMapping(dependenciesElement);
            }
        }

        // 添加插件信息
        Element buildElement = projectNode.addElement("build");
        Element plugins = buildElement.addElement("plugins");
        for (MavenPlugin plugin : this.plugins) {
            plugin.xmlMapping(plugins);
        }
    }
}
