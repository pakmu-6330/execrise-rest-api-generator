package cn.dyr.rest.generator.io.structure;

import cn.dyr.rest.generator.java.meta.ClassInfo;

import java.io.File;
import java.io.IOException;

/**
 * Java 语言项目，封装从包名和类名转换到对应类源码的逻辑
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public abstract class BasicJavaProjectStructure implements IProjectStructure {

    @Override
    public File getSourceFile(ClassInfo classInfo) throws IOException {
        File srcDir = this.getSourcesDir();
        String srcDirPath = srcDir.getAbsolutePath();
        if (!srcDirPath.endsWith("/")) {
            srcDirPath = srcDirPath + "/";
        }

        String classFullName = classInfo.getFullName();
        String suffix = classFullName.replace(".", "/");

        return new File(srcDirPath + suffix + ".java");
    }
}
