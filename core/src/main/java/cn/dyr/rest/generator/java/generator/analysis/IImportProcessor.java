package cn.dyr.rest.generator.java.generator.analysis;

import java.util.Set;

/**
 * 这个接口可以获得这个元信息当中所使用到的类的信息，便于在生成 import 语句时对外部的依赖进行发现
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IImportProcessor {

    /**
     * 将本单元需要引用的第三方类添加到列表当中
     *
     * @param context 用于存放第三方类引入信息的集合类
     */
    void fillImportOperations(ImportContext context);

}
