package cn.dyr.rest.generator.xml.maven;

import cn.dyr.rest.generator.util.StringUtils;
import cn.dyr.rest.generator.xml.IXMLMapping;
import org.dom4j.Branch;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 这里的每一个对象表示一个 Maven 依赖配置信息
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class MavenDependency implements IXMLMapping {

    /**
     * 表示 compile 范围。
     */
    public static final String SCOPE_COMPILE = "compile";

    /**
     * 表示 test 范围。
     */
    public static final String SCOPE_TEST = "test";

    /**
     * 表示 provided 范围。
     */
    public static final String SCOPE_PROVIDED = "provided";

    /**
     * 表示 runtime 范围。
     */
    public static final String SCOPE_RUNTIME = "runtime";

    /**
     * 表示 system 范围。
     */
    public static final String SCOPE_SYSTEM = "system";

    private String scope;
    private MavenCoordinate coordinate;

    private List<MavenCoordinate> excludes;
    private boolean optional;

    public MavenDependency() {
        this.excludes = new ArrayList<>();
    }

    public MavenCoordinate getCoordinate() {
        return coordinate;
    }

    public MavenDependency setCoordinate(MavenCoordinate coordinate) {
        this.coordinate = coordinate;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public MavenDependency setScope(String scope) {
        this.scope = scope;
        return this;
    }

    public boolean isOptional() {
        return optional;
    }

    public MavenDependency setOptional(boolean optional) {
        this.optional = optional;
        return this;
    }

    /**
     * 往这个 maven 依赖当中添加一个排除项
     *
     * @param coordinate 要添加到当前依赖信息当中的排除项
     * @return 这个依赖信息本身
     */
    public MavenDependency addExclusion(MavenCoordinate coordinate) {
        if (coordinate == null) {
            throw new IllegalArgumentException("coordinate is null");
        }

        MavenCoordinate coordinateWithoutVersion = removeVersionInfo(coordinate);
        this.excludes.add(coordinateWithoutVersion);

        return this;
    }

    /**
     * 生成一个用于迭代排除依赖的迭代器
     *
     * @return 一个用于迭代依赖排除项坐标信息的迭代器
     */
    public Iterator<MavenCoordinate> iterateExclusions() {
        return this.excludes.iterator();
    }

    private MavenCoordinate removeVersionInfo(MavenCoordinate coordinate) {
        return new MavenCoordinate()
                .setVersion(null)
                .setPackaging(coordinate.getPackaging())
                .setGroupId(coordinate.getGroupId())
                .setArtifactId(coordinate.getArtifactId())
                .setClassifier(coordinate.getClassifier());
    }

    @Override
    public void xmlMapping(Branch parentBranch) {
        if (this.coordinate == null) {
            throw new NullPointerException("dependency coordinate is null!");
        }

        Element dependencyElement = parentBranch.addElement("dependency");
        this.coordinate.xmlMapping(dependencyElement);

        // 添加 scope 信息
        if (!StringUtils.isStringEmpty(this.scope) &&
                !SCOPE_COMPILE.equals(this.scope)) {
            dependencyElement.addElement("scope").addText(this.scope);
        }

        // 输出排除信息
        if (this.excludes != null && this.excludes.size() > 0) {
            Element exclusionsElement = dependencyElement.addElement("exclusions");

            for (MavenCoordinate coordinate : this.excludes) {
                coordinate.xmlMapping(exclusionsElement);
            }
        }
    }
}
