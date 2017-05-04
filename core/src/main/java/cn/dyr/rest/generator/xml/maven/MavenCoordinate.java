package cn.dyr.rest.generator.xml.maven;

import cn.dyr.rest.generator.util.StringUtils;
import cn.dyr.rest.generator.xml.IXMLMapping;
import org.dom4j.Branch;

/**
 * 这个类用于 Maven 当中对第三方库的定位等功能
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class MavenCoordinate implements IXMLMapping {

    /**
     * 表示这个坐标表示的项目使用 jar 进行打包
     */
    public static final String PACKAGE_JAR = "jar";

    /**
     * 表示这个坐标表示的项目使用 war 进行打包
     */
    public static final String PACKAGE_WAR = "war";

    private String groupId;
    private String artifactId;
    private String version;
    private String packaging;
    private String classifier;

    public MavenCoordinate() {
        this.packaging = PACKAGE_JAR;
    }

    public String getGroupId() {
        return groupId;
    }

    public MavenCoordinate setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public MavenCoordinate setArtifactId(String artifactId) {
        this.artifactId = artifactId;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public MavenCoordinate setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getPackaging() {
        return packaging;
    }

    public MavenCoordinate setPackaging(String packaging) {
        this.packaging = packaging;
        return this;
    }

    public String getClassifier() {
        return classifier;
    }

    public MavenCoordinate setClassifier(String classifier) {
        this.classifier = classifier;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MavenCoordinate that = (MavenCoordinate) o;

        if (groupId != null ? !groupId.equals(that.groupId) : that.groupId != null) return false;
        if (artifactId != null ? !artifactId.equals(that.artifactId) : that.artifactId != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;
        if (packaging != null ? !packaging.equals(that.packaging) : that.packaging != null) return false;
        return classifier != null ? classifier.equals(that.classifier) : that.classifier == null;
    }

    @Override
    public int hashCode() {
        int result = groupId != null ? groupId.hashCode() : 0;
        result = 31 * result + (artifactId != null ? artifactId.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (packaging != null ? packaging.hashCode() : 0);
        result = 31 * result + (classifier != null ? classifier.hashCode() : 0);
        return result;
    }

    @Override
    public void xmlMapping(Branch parentBranch) {
        parentBranch.addElement("groupId").addText(this.groupId);
        parentBranch.addElement("artifactId").addText(this.artifactId);

        if (!StringUtils.isStringEmpty(this.version)) {
            parentBranch.addElement("version").addText(this.version);
        }
    }
}
