package cn.dyr.rest.generator.java.meta.javadoc;

import java.util.ArrayList;
import java.util.List;

/**
 * 表示符合 javadoc 规范的文档信息
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class JavaDocInfo {

    private String content;
    private List<IJavaDocInfoItem> items;

    public JavaDocInfo(String content) {
        this.content = content;
        this.items = new ArrayList<>();
    }

    /**
     * 获得 javadoc 内容
     *
     * @return javadoc 的内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置 javadoc 的内容
     *
     * @param content javadoc 的内容
     * @return 这个对象本身
     */
    public JavaDocInfo setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * 获得 JavaDoc 当中标记的列表
     *
     * @return 这个 JavaDoc 标记当中拥有的标记的列表
     */
    public List<IJavaDocInfoItem> getItems() {
        return items;
    }

    /**
     * 设置 JavaDoc 当中标记的列表
     *
     * @param items javadoc 标记的列表
     * @return 这个 JavaDoc 本身
     */
    public JavaDocInfo setItems(List<IJavaDocInfoItem> items) {
        this.items = items;
        return this;
    }

    /**
     * 往 JavaDoc 当中添加一个标记信息
     *
     * @param item 添加到 JavaDoc 对象当中的一个标记信息
     * @return 这个 JavaDoc 本身
     */
    public JavaDocInfo addItem(IJavaDocInfoItem item) {
        return this;
    }
}
