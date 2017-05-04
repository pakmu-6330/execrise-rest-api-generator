package cn.dyr.rest.generator.java.meta.factory;

import cn.dyr.rest.generator.java.meta.flow.ChoiceFlow;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 用于生成分支结构的 Builder
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ChoiceFlowBuilder {

    private IValueExpression ifCondition;
    private IInstruction ifBlock;

    private List<IValueExpression> elseIfConditions;
    private List<IInstruction> elseIfInstructions;

    private IInstruction elseBlock;

    public ChoiceFlowBuilder() {
        this.elseIfConditions = new ArrayList<>();
        this.elseIfInstructions = new ArrayList<>();
    }

    /**
     * 设置 if 条件和语句块内容
     *
     * @param condition   if 条件表达式
     * @param instruction if 语句块内容
     * @return 这个 builder 本身
     */
    public ChoiceFlowBuilder setIfBlock(IValueExpression condition, IInstruction instruction) {
        this.ifCondition = condition;
        this.ifBlock = instruction;

        return this;
    }

    /**
     * 添加一个 else if 条件和语句块的内容
     *
     * @param condition   else if 条件
     * @param instruction else if 语句块
     * @return 这个 builder 本身
     */
    public ChoiceFlowBuilder addElseIf(IValueExpression condition, IInstruction instruction) {
        this.elseIfConditions.add(condition);
        this.elseIfInstructions.add(instruction);

        return this;
    }

    /**
     * 添加一个 else 语句块的内容
     *
     * @param elseBlock else 语句块
     * @return 这个 builder 本身
     */
    public ChoiceFlowBuilder setElse(IInstruction elseBlock) {
        this.elseBlock = elseBlock;

        return this;
    }

    /**
     * 根据 builder 中的信息构建对应的分支结构指令
     *
     * @return 分支结构指令
     */
    public IInstruction build() {
        Objects.requireNonNull(this.ifCondition, "if condition is null");
        Objects.requireNonNull(this.ifBlock, "if block is null");

        // 组装 if 条件相关内容
        ChoiceFlow flow = new ChoiceFlow();
        ChoiceFlow.ChoiceFlowGroup ifGroup = new ChoiceFlow.ChoiceFlowGroup();
        ifGroup.condition = ifCondition;
        ifGroup.instruction = ifBlock;
        flow.setIfBlock(ifGroup);

        // 组装 else if 相关的内容
        if (this.elseIfConditions != null && this.elseIfInstructions != null) {
            if (this.elseIfInstructions.size() != this.elseIfInstructions.size()) {
                throw new IllegalStateException("else if condition and instruction number not matched!");
            }

            for (int i = 0; i < this.elseIfConditions.size(); i++) {
                IValueExpression condition = this.elseIfConditions.get(i);
                IInstruction instruction = this.elseIfInstructions.get(i);

                flow.addElseIfBlock(condition, instruction);
            }
        }

        // 组装 else 部分的内容
        flow.setElseBlock(elseBlock);

        return flow;
    }
}
