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
@XmlType(name = "config", propOrder = {"tablePrefix", "uriPrefix",
        "pageSize", "port"})
public class XMLConfig {

    private String tablePrefix;
    private String uriPrefix;

    private int pageSize;
    private int port;

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

    public int getPageSize() {
        return pageSize;
    }

    public XMLConfig setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int getPort() {
        return port;
    }

    public XMLConfig setPort(int port) {
        this.port = port;
        return this;
    }
}
