package cn.dyr.rest.generator.java.meta.javadoc;

/**
 * JavaDoc 文档当中的一个内容项
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IJavaDocInfoItem {

    /**
     * 获得 JavaDoc 内容项的类型
     *
     * @return 表示 JavaDoc 内容项的类型
     */
    JavaDocInfoItemType getType();

    /**
     * 获得这个 JavaDoc 内容项的类型
     *
     * @return 表示 JavaDoc 内容项的内容
     */
    String getContent();

}
