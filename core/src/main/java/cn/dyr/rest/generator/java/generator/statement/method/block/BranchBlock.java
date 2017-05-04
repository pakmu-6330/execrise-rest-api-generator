package cn.dyr.rest.generator.java.generator.statement.method.block;

import cn.dyr.rest.generator.java.generator.IndentManager;
import cn.dyr.rest.generator.java.generator.elements.ValueExpressionToken;
import cn.dyr.rest.generator.java.generator.statement.IStatement;
import cn.dyr.rest.generator.java.generator.statement.StatementFactory;
import cn.dyr.rest.generator.java.meta.flow.ChoiceFlow;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;

import java.util.List;

/**
 * 表示这是一个分支结构的语句
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class BranchBlock implements IStatement {

    private ChoiceFlow choiceFlow;

    public BranchBlock(ChoiceFlow choiceFlow) {
        this.choiceFlow = choiceFlow;
    }

    public ChoiceFlow getChoiceFlow() {
        return choiceFlow;
    }

    public BranchBlock setChoiceFlow(ChoiceFlow choiceFlow) {
        this.choiceFlow = choiceFlow;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        IndentManager indentManager = IndentManager.get();

        // 生成 if 语句块的内容
        ChoiceFlow.ChoiceFlowGroup ifGroup = choiceFlow.getIfBlock();
        if (ifGroup != null) {
            // 生成 if 条件语句
            builder.append(indentManager.getIndentString());

            builder.append("if (");
            ValueExpressionToken ifToken = new ValueExpressionToken(ifGroup.condition);
            builder.append(ifToken.toString());
            builder.append(") {");
            builder.append(System.lineSeparator());

            // 生成 if 块里面语句
            indentManager.indent();
            IStatement statement = StatementFactory.fromInstruction(ifGroup.instruction);
            builder.append(statement);

            // 生成 if 结尾的大括号
            indentManager.delIndent();
            builder.append(indentManager.getIndentString());
            builder.append("}");
        }

        // 生成 else if 语句块的内容
        List<ChoiceFlow.ChoiceFlowGroup> elseIfBlocks = choiceFlow.getElseIfBlocks();
        for (ChoiceFlow.ChoiceFlowGroup block : elseIfBlocks) {
            // 生成 else if 条件
            builder.append(" else if (");
            ValueExpressionToken elseIfConditionToken = new ValueExpressionToken(block.condition);
            builder.append(elseIfConditionToken.toString());
            builder.append(") {");
            builder.append(System.lineSeparator());

            // 生成 else if 块里面的内容
            indentManager.indent();
            IStatement statement = StatementFactory.fromInstruction(block.instruction);
            builder.append(statement);

            // 生成 else if 结尾的大括号
            indentManager.delIndent();
            builder.append(indentManager.getIndentString());
            builder.append("}");
        }

        // 生成 else 语句块的内容
        IInstruction elseBlock = choiceFlow.getElseBlock();
        if (elseBlock != null) {
            // 生成 else 头部分代码
            builder.append(" else {");
            builder.append(System.lineSeparator());

            // 生成 else 语句块的内容会
            indentManager.indent();
            IStatement elseStatement = StatementFactory.fromInstruction(elseBlock);
            builder.append(elseStatement);

            // 生成结尾的花括号
            indentManager.delIndent();
            builder.append(indentManager.getIndentString());
            builder.append("}");
        }

        // 最后的换行
        builder.append(System.lineSeparator());

        return builder.toString();
    }
}
