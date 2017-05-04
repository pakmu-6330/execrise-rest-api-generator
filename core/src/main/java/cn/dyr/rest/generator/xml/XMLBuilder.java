package cn.dyr.rest.generator.xml;

import cn.dyr.rest.generator.xml.maven.MavenProject;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringWriter;

/**
 * 用于创建各类的 XML 文档的类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class XMLBuilder {

    private Document buildDocument(IXMLMapping targetObject) {
        if (targetObject == null) {
            throw new NullPointerException("target Object is null!");
        }

        Document document = DocumentHelper.createDocument();
        targetObject.xmlMapping(document);

        return document;
    }

    /**
     * 创建一个 Maven 的 pom 文件
     *
     * @param projectInfo 储存有 maven 项目信息的类
     * @return pom 文件的内容
     */
    public String buildMavenPOM(MavenProject projectInfo) {
        Document document = this.buildDocument(projectInfo);

        // 对 XML 文档进行格式化输出
        OutputFormat xmlFormat = OutputFormat.createPrettyPrint();
        xmlFormat.setNewlines(true);
        xmlFormat.setIndent(true);
        xmlFormat.setIndent("    ");
        xmlFormat.setEncoding("UTF-8");
        StringWriter writer = new StringWriter();

        try {
            XMLWriter xmlWriter = new XMLWriter(writer, xmlFormat);
            xmlWriter.write(document);
            xmlWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

}
