package cn.dyr.rest.generator.xml.maven;

import cn.dyr.rest.generator.util.StringUtils;
import cn.dyr.rest.generator.xml.IXMLMapping;
import org.dom4j.Branch;
import org.dom4j.Element;

/**
 * 这个表示的是继承项目时的父项目
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class MavenParent implements IXMLMapping {

    public static final String DEFAULT_PATH_RELATIVE_PATH = "../pom.xml";

    private MavenCoordinate coordinate;
    private String relativePath;

    public MavenParent() {
        this.relativePath = DEFAULT_PATH_RELATIVE_PATH;
    }

    public MavenCoordinate getCoordinate() {
        return coordinate;
    }

    public MavenParent setCoordinate(MavenCoordinate coordinate) {
        this.coordinate = coordinate;
        return this;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public MavenParent setRelativePath(String relativePath) {
        this.relativePath = relativePath;
        return this;
    }

    /**
     * 用于将 parent 节点转换方法
     *
     * @param parentBranch parent 节点的上一级结点，即 pom 文件当中的 project 节点
     */
    @Override
    public void xmlMapping(Branch parentBranch) {
        if (this.coordinate == null) {
            throw new NullPointerException("parent coordinate is null!");
        }

        Element parentElement = parentBranch.addElement("parent");
        this.coordinate.xmlMapping(parentElement);

        if (this.relativePath == null) {
            parentElement.addElement("relativePath");
        } else {
            if (!DEFAULT_PATH_RELATIVE_PATH.equals(this.relativePath)) {
                parentElement.addElement("relativePath").addText(this.relativePath);
            }
        }
    }
}
