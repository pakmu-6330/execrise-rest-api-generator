package cn.dyr.rest.generator.io.structure;

import java.io.File;
import java.io.IOException;

/**
 * 封装用于 maven 项目文件 IO 操作相关逻辑
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class MavenProjectStructure extends BasicJavaProjectStructure {

    private File parent;
    private File javaDir;
    private File javaSrcDir;
    private File javaResourceDir;
    private File testDir;
    private File testSrcDir;
    private File testResourceDir;

    @Override
    public void createStructure(File targetDir) throws IOException {
        // 如果目标目录不存在，则创建目标目录
        if (!targetDir.exists()) {
            boolean mkdirs = targetDir.mkdirs();
            if (!mkdirs) {
                throw new IOException(String.format("%s not exists and failed to created it.", targetDir.getAbsolutePath()));
            }
        }

        // 创建 maven 项目结构
        File sourceDir = new File(targetDir, "src");
        throwExceptionWhenFailedToCreateDir(sourceDir);

        this.javaDir = new File(sourceDir, "main");
        throwExceptionWhenFailedToCreateDir(this.javaDir);

        this.javaSrcDir = new File(this.javaDir, "java");
        throwExceptionWhenFailedToCreateDir(this.javaSrcDir);

        this.javaResourceDir = new File(this.javaDir, "resources");
        throwExceptionWhenFailedToCreateDir(this.javaResourceDir);

        this.parent = targetDir;
    }

    @Override
    public File getRootDir() throws IOException {
        return this.parent;
    }

    @Override
    public File getSourcesDir() {
        checkTargetDir();

        return this.javaSrcDir;
    }

    @Override
    public File getResourcesDir() {
        checkTargetDir();

        return this.javaResourceDir;
    }

    public File getPOMFile() {
        return new File(this.parent, "pom.xml");
    }

    private void checkTargetDir() {
        if (parent == null) {
            throw new RuntimeException("target dir is not specified.");
        }
    }

    private void throwExceptionWhenFailedToCreateDir(File dir) throws IOException {
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException(String.format("%s not exists and failed to created it.", dir.getAbsolutePath()));
        }
    }
}
