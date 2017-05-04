package cn.dyr.rest.generator.xml.maven;

import cn.dyr.rest.generator.xml.IXMLMapping;
import org.dom4j.Branch;
import org.dom4j.Element;

/**
 * 表示一个 maven 的插件
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class MavenPlugin implements IXMLMapping {

    private MavenCoordinate coordinate;
    private MavenPluginConfiguration configuration;

    public MavenPlugin() {
    }

    public MavenCoordinate getCoordinate() {
        return coordinate;
    }

    public MavenPlugin setCoordinate(MavenCoordinate coordinate) {
        this.coordinate = coordinate;
        return this;
    }

    public MavenPluginConfiguration getConfiguration() {
        return configuration;
    }

    public MavenPlugin setConfiguration(MavenPluginConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    @Override
    public void xmlMapping(Branch parentBranch) {
        if (this.coordinate == null) {
            throw new NullPointerException("coordinate is null!");
        }

        Element pluginElement = parentBranch.addElement("plugin");
        this.coordinate.xmlMapping(pluginElement);

        if (this.configuration != null) {
            Element configurationElement = pluginElement.addElement("configuration");
            this.configuration.xmlMapping(configurationElement);
        }
    }
}
