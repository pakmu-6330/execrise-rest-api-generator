package cn.dyr.rest.generator.java.meta.javadoc;

/**
 * 表示带 key 的 JavaDoc 项目
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class KeyJavaDocItem implements IJavaDocInfoItem {

    private JavaDocInfoItemType type;
    private String content;

    public KeyJavaDocItem(JavaDocInfoItemType type, String content) {
        this.type = type;
        this.content = content;
    }

    public KeyJavaDocItem setType(JavaDocInfoItemType type) {
        this.type = type;
        return this;
    }

    public KeyJavaDocItem setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public JavaDocInfoItemType getType() {
        return this.type;
    }

    @Override
    public String getContent() {
        return this.content;
    }
}
