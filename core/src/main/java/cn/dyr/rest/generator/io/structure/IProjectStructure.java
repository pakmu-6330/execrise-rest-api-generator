package cn.dyr.rest.generator.io.structure;

import cn.dyr.rest.generator.java.meta.ClassInfo;

import java.io.File;
import java.io.IOException;

/**
 * 对程序项目结构操作的抽象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IProjectStructure {

    /**
     * 在给定的目录下创建一个项目结构
     *
     * @param targetDir 要创建项目结构目录
     * @throws IOException 如果在执行 IO 过程中出现错误，则会抛出这个异常
     */
    void createStructure(File targetDir) throws IOException;

    /**
     * 获得这个项目的根目录
     *
     * @return 这个项目的根目录
     * @throws IOException 如果在执行 IO 过程中出现错误，则会抛出这个异常
     */
    File getRootDir() throws IOException;

    /**
     * 获得用于存放源码文件的目录
     *
     * @return 存放源码的目录
     * @throws IOException 如果在执行 IO 过程中出现错误，则会抛出这个异常
     */
    File getSourcesDir() throws IOException;

    /**
     * 获得用于存放资源文件的目录
     *
     * @return 存放资源文件的目录
     * @throws IOException 如果在执行 IO 过程中出现错误，则会抛出这个异常
     */
    File getResourcesDir() throws IOException;

    /**
     * 获得某个类对应的代码文件
     *
     * @param classInfo 类元信息
     * @return 这个类对应的源码文件路径
     * @throws IOException 如果在执行 IO 过程中出现错误，则会抛出这个异常
     */
    File getSourceFile(ClassInfo classInfo) throws IOException;
}
