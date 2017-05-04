package cn.dyr.rest.generator.java.generator.statement;

import cn.dyr.rest.generator.java.generator.IndentManager;
import cn.dyr.rest.generator.java.meta.flow.SingleLineCommentInstruction;

/**
 * 表示单行注释的语句
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SingleLineCommentStatement implements IStatement {

    private SingleLineCommentInstruction commentInstruction;

    public SingleLineCommentStatement(SingleLineCommentInstruction commentInstruction) {
        this.commentInstruction = commentInstruction;
    }

    /**
     * 获得这个单行注释对应的单行注释指令
     *
     * @return 单行注释指令
     */
    public SingleLineCommentInstruction getCommentInstruction() {
        return commentInstruction;
    }

    /**
     * 设置单行注释指令
     *
     * @param commentInstruction 单行注释指令
     * @return 单行注释指令
     */
    public SingleLineCommentStatement setCommentInstruction(SingleLineCommentInstruction commentInstruction) {
        this.commentInstruction = commentInstruction;
        return this;
    }

    @Override
    public String toString() {
        IndentManager indentManager = IndentManager.get();

        return indentManager.getIndentString() + "// " + this.commentInstruction.getComment() + System.lineSeparator();
    }
}
