package cn.dyr.rest.generator.java.generator.statement;

import cn.dyr.rest.generator.java.generator.IndentManager;
import cn.dyr.rest.generator.java.meta.flow.MultiLineCommentInstruction;

import java.util.List;

/**
 * 表示一条多行注释的语句
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class MultiLineCommentStatement implements IStatement {

    private MultiLineCommentInstruction instruction;

    public MultiLineCommentStatement(MultiLineCommentInstruction instruction) {
        this.instruction = instruction;
    }

    public MultiLineCommentInstruction getInstruction() {
        return instruction;
    }

    public MultiLineCommentStatement setInstruction(MultiLineCommentInstruction instruction) {
        this.instruction = instruction;
        return this;
    }

    @Override
    public String toString() {
        IndentManager indentManager = IndentManager.get();
        StringBuilder builder = new StringBuilder();

        builder.append(indentManager.getIndentString());
        builder.append("/*");
        builder.append(System.lineSeparator());

        List<String> comments = instruction.getComments();
        for (String comment : comments) {
            builder.append(indentManager.getIndentString());
            builder.append(" ");
            builder.append(comment);
            builder.append(System.lineSeparator());
        }

        builder.append(indentManager.getIndentString());
        builder.append("*/");
        builder.append(System.lineSeparator());

        return builder.toString();
    }
}
