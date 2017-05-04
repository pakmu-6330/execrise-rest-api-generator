package cn.dyr.rest.generator.xml.maven;

import org.dom4j.Branch;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 基于键值对方式实现的 Maven 插件配置信息
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class MavenPluginConfigurationKVImpl implements MavenPluginConfiguration {

    private Map<String, String> configurations;

    public MavenPluginConfigurationKVImpl() {
        this.configurations = new HashMap<>();
    }

    /**
     * 往这个配置信息当中放置一个键值对信息
     *
     * @param key   键
     * @param value 值
     * @return 这个配置信息对象本身
     */
    public MavenPluginConfigurationKVImpl putConfiguration(String key, String value) {
        this.configurations.put(key, value);
        return this;
    }

    @Override
    public void xmlMapping(Branch parentBranch) {
        if (this.configurations != null && this.configurations.size() > 0) {
            Set<String> keySet = this.configurations.keySet();

            for (String key : keySet) {
                parentBranch.addElement(key).addText(this.configurations.get(key));
            }
        }
    }
}
