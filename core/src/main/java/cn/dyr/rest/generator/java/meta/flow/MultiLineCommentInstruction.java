package cn.dyr.rest.generator.java.meta.flow;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类表示的是一条多行的注释信息
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class MultiLineCommentInstruction extends AbstractCommentInstruction {

    private List<String> comments;

    public MultiLineCommentInstruction(List<String> comments) {
        this.comments = comments;
    }

    /**
     * 获得注释的内容
     *
     * @return 注释的内容
     */
    public List<String> getComments() {
        return comments;
    }

    /**
     * 设置注释的内容
     *
     * @param comments 注释的内容
     * @return 注释的本身
     */
    public MultiLineCommentInstruction setComments(List<String> comments) {
        this.comments = comments;
        return this;
    }

    /**
     * 给注释添加一行内容
     *
     * @param line 要添加到注释当中的内容
     * @return 这个注释指令本身
     */
    public MultiLineCommentInstruction addCommentLine(String line) {
        if (this.comments == null) {
            this.comments = new ArrayList<>();
        }

        this.comments.add(line);
        return this;
    }
}
