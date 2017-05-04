package cn.dyr.rest.generator.java.meta.flow;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类表示一个 if-else if-else 的分支结构
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ChoiceFlow extends AbstractFlow {

    public static class ChoiceFlowGroup {
        public IValueExpression condition;
        public IInstruction instruction;
    }

    private ChoiceFlowGroup ifBlock;
    private List<ChoiceFlowGroup> elseIfBlocks;
    private IInstruction elseBlock;

    public ChoiceFlow() {
        this.elseIfBlocks = new ArrayList<>();
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        if (ifBlock != null) {
            ifBlock.instruction.fillImportOperations(context);
            ifBlock.condition.fillImportOperations(context);
        }

        if (this.elseIfBlocks != null && this.elseIfBlocks.size() > 0) {
            for (ChoiceFlowGroup group : elseIfBlocks) {
                group.instruction.fillImportOperations(context);
                group.condition.fillImportOperations(context);
            }
        }

        if (this.elseBlock != null) {
            this.elseBlock.fillImportOperations(context);
        }
    }

    @Override
    public int getFlowType() {
        return AbstractFlow.FLOW_CHOICE;
    }

    public ChoiceFlowGroup getIfBlock() {
        return ifBlock;
    }

    public ChoiceFlow setIfBlock(ChoiceFlowGroup ifBlock) {
        this.ifBlock = ifBlock;
        return this;
    }

    public List<ChoiceFlowGroup> getElseIfBlocks() {
        return elseIfBlocks;
    }

    public ChoiceFlow setElseIfBlocks(List<ChoiceFlowGroup> elseIfBlocks) {
        this.elseIfBlocks = elseIfBlocks;
        return this;
    }

    public ChoiceFlow addElseIfBlock(IValueExpression condition, IInstruction instruction) {
        if (this.elseIfBlocks == null) {
            this.elseIfBlocks = new ArrayList<>();
        }

        ChoiceFlowGroup group = new ChoiceFlowGroup();
        group.condition = condition;
        group.instruction = instruction;
        this.elseIfBlocks.add(group);

        return this;
    }

    public IInstruction getElseBlock() {
        return elseBlock;
    }

    public ChoiceFlow setElseBlock(IInstruction elseBlock) {
        this.elseBlock = elseBlock;
        return this;
    }
}
