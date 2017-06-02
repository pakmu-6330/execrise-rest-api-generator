package cn.dyr.rest.generator.ui.swing.persist;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 用于进行项目配置信息持久化的类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@XmlRootElement
@XmlType(name = "config", propOrder = {"tablePrefix", "uriPrefix"})
public class XMLConfig {

    private String tablePrefix;
    private String uriPrefix;

    public String getTablePrefix() {
        return tablePrefix;
    }

    public XMLConfig setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
        return this;
    }

    public String getUriPrefix() {
        return uriPrefix;
    }

    public XMLConfig setUriPrefix(String uriPrefix) {
        this.uriPrefix = uriPrefix;
        return this;
    }
}
