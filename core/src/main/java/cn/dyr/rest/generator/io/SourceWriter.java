package cn.dyr.rest.generator.io;

import cn.dyr.rest.generator.io.structure.IProjectStructure;
import cn.dyr.rest.generator.io.structure.MavenProjectStructure;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.xml.maven.MavenProject;
import org.apache.commons.io.IOUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * 这个类封装了将代码写入指定文件当中。执行产生项目结构的逻辑代码等
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SourceWriter {

    private IProjectStructure projectStructure;

    public SourceWriter() {
        this.projectStructure = new MavenProjectStructure();
    }

    /**
     * 检查代码文件所在包的文件夹是否存在
     *
     * @param sourceFile 源码文件
     */
    private void checkPackage(File sourceFile) throws IOException {
        File packageFile = sourceFile.getParentFile();

        // 检查包是否存在，如果不存在则创建；创建失败则抛出异常
        if (!packageFile.exists()) {
            if (!packageFile.mkdirs()) {
                throw new IOException(
                        String.format("%s is not exists and failed to created it.",
                                packageFile.getAbsolutePath()));
            }
        } else if (packageFile.exists() && !packageFile.isDirectory()) {
            throw new IOException(
                    String.format("%s exists but not a directory", packageFile.getAbsolutePath()));
        }
    }

    /**
     * 初始化这个代码输出类
     *
     * @param targetDir 基目录
     * @throws IOException 如果进行 IO 操作的过程中发生错误，则会抛出这个异常
     */
    public void init(File targetDir) throws IOException {
        this.projectStructure.createStructure(targetDir);
    }

    /**
     * 将这个类文件写入到文件当中
     *
     * @param classInfo 类信息
     * @param source    源码
     * @throws IOException 如果 IO 操作发生错误，则会抛出这个异常
     */
    public void writeSource(ClassInfo classInfo, String source) throws IOException {
        File sourceFile = this.projectStructure.getSourceFile(classInfo);
        checkPackage(sourceFile);

        FileOutputStream fileOutputStream = new FileOutputStream(sourceFile);
        BufferedOutputStream outputStream = new BufferedOutputStream(fileOutputStream);

        IOUtils.write(source, outputStream, "UTF-8");

        outputStream.close();
    }

    /**
     * 将 Properties 写入到资源目录的指定文件当中
     *
     * @param filename   文件名
     * @param properties 要写入到文件的 Properties 信息
     */
    public void writePropertiesToResourceDir(String filename, Properties properties) throws IOException {
        Objects.requireNonNull(properties, "properties is null");

        File resourcesDir = this.projectStructure.getResourcesDir();
        File targetPropertiesFile = new File(resourcesDir, filename);

        FileOutputStream outputStream = new FileOutputStream(targetPropertiesFile);
        properties.store(outputStream, "");
        outputStream.close();
    }

    /**
     * 将 Maven 项目信息写入到 POM 文件当中
     *
     * @param xmlContent 要写入到文件的 POM 文件信息
     * @throws IOException 如果在进行 IO 操作的过程中发生错误，则会抛出这个异常
     */
    public void writePOM(String xmlContent) throws IOException {
        if (this.projectStructure instanceof MavenProjectStructure) {
            MavenProjectStructure mavenProjectStructure = (MavenProjectStructure) this.projectStructure;
            File pomFile = mavenProjectStructure.getPOMFile();

            FileOutputStream fileOutputStream = new FileOutputStream(pomFile);
            BufferedOutputStream outputStream = new BufferedOutputStream(fileOutputStream);

            IOUtils.write(xmlContent, outputStream, "UTF-8");

            outputStream.close();
            return;
        }

        throw new IllegalStateException("pom access is only available in maven project");
    }
}
