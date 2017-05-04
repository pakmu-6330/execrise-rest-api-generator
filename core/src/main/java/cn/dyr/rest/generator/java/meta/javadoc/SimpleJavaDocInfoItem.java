package cn.dyr.rest.generator.java.meta.javadoc;

/**
 * 简单的 JavaDoc 标记项目
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SimpleJavaDocInfoItem implements IJavaDocInfoItem {

    private JavaDocInfoItemType type;
    private String content;

    public SimpleJavaDocInfoItem(JavaDocInfoItemType type, String content) {
        this.type = type;
        this.content = content;
    }

    /**
     * 设置这个 JavaDoc 标记的类型
     *
     * @param type 这个 JavaDoc 标记的类型
     * @return 这个标记项目本身
     */
    public SimpleJavaDocInfoItem setType(JavaDocInfoItemType type) {
        this.type = type;
        return this;
    }

    @Override
    public JavaDocInfoItemType getType() {
        return this.type;
    }

    /**
     * 设置这个标记项目的内容
     *
     * @param content 要设置的内容
     * @return 这个标记项目本身
     */
    public SimpleJavaDocInfoItem setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public String getContent() {
        return this.content;
    }
}
