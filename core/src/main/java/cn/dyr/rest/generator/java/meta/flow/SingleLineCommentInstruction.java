package cn.dyr.rest.generator.java.meta.flow;

/**
 * 这个类表示这是一条单行的注释内容
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SingleLineCommentInstruction extends AbstractCommentInstruction {

    private String comment;

    public SingleLineCommentInstruction(String comment) {
        this.comment = comment;
    }

    /**
     * 获得注释的具体内容
     *
     * @return 注释的具体内容
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置注释的具体内容
     *
     * @param comment 注释的具体内容
     * @return 这个注释对象本身
     */
    public SingleLineCommentInstruction setComment(String comment) {
        this.comment = comment;
        return this;
    }
}
