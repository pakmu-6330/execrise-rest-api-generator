package cn.dyr.rest.generator.java.meta.flow;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 表示代码当中的顺序结构
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SequenceFlow extends AbstractFlow {

    private List<IInstruction> instructions;

    public SequenceFlow() {
        this.instructions = new ArrayList<>();
    }

    /**
     * 往这个顺序结构当中添加一条抽象指令
     *
     * @param instruction 要添加到程序当中的抽象指令
     * @return 这个顺序结构本身
     */
    public SequenceFlow addInstruction(IInstruction instruction) {
        this.instructions.add(instruction);
        return this;
    }

    /**
     * 产生一个用对这个顺序结构指令当中子指令进行迭代的迭代器
     *
     * @return 用于对顺序结构当中的子指令进行迭代的迭代器
     */
    public Iterator<IInstruction> iterateInstructions() {
        return this.instructions.iterator();
    }

    @Override
    public int getFlowType() {
        return IFlow.FLOW_SEQUENCE;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        for (IInstruction instruction : this.instructions) {
            instruction.fillImportOperations(context);
        }
    }
}
