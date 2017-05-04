package cn.dyr.rest.generator.java.generator;

import cn.dyr.rest.generator.java.meta.ClassInfo;

/**
 * 用于定义代码生成器的接口
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface ICodeGenerator {

    /**
     * 获得当前生成器中的各项属性
     *
     * @return 当前代码生成器的属性
     */
    CodeGeneratorConfig getConfig();

    /**
     * 将设置参数应用到代码生成器当中
     *
     * @param config 设置参数
     * @return 代码生成器本身
     */
    ICodeGenerator setConfig(CodeGeneratorConfig config);

    /**
     * 生成单个类的代码文件
     *
     * @param classInfo 用于产生代码的类信息
     * @return 与这个类信息与之对应的代码内容
     */
    String generateSingleClass(ClassInfo classInfo);
}
