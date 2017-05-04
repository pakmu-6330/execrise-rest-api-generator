package cn.dyr.rest.generator.ui.web.util;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import java.io.File;

/**
 * zip 压缩相关的工具类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ZipCompressor {

    private File zipFile;

    /**
     * 压缩文件构造函数
     *
     * @param finalFile 最终压缩生成的压缩文件：目录+压缩文件名.zip
     */
    public ZipCompressor(File finalFile) {
        zipFile = finalFile;
    }

    /**
     * 执行压缩操作
     *
     * @param srcPathName 需要被压缩的文件/文件夹
     */
    public void compressExe(String srcPathName) {
        File srcDir = new File(srcPathName);
        if (!srcDir.exists()) {
            throw new RuntimeException(srcPathName + "不存在！");
        }

        Project prj = new Project();
        Zip zip = new Zip();
        zip.setProject(prj);
        zip.setDestFile(zipFile);
        FileSet fileSet = new FileSet();
        fileSet.setProject(prj);
        fileSet.setDir(srcDir);
        zip.addFileset(fileSet);
        zip.execute();
    }

}
