package cn.dyr.rest.generator.java.generator.statement.method.block;

import cn.dyr.rest.generator.java.generator.statement.IStatement;
import cn.dyr.rest.generator.java.generator.statement.StatementFactory;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.flow.SequenceFlow;

import java.util.Iterator;

/**
 * 表示一个顺序结构的代码块
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SequenceBlock implements IStatement {

    private SequenceFlow sequenceFlow;

    public SequenceBlock(SequenceFlow sequenceFlow) {
        this.sequenceFlow = sequenceFlow;
    }

    public SequenceFlow getSequenceFlow() {
        return sequenceFlow;
    }

    public SequenceBlock setSequenceFlow(SequenceFlow sequenceFlow) {
        this.sequenceFlow = sequenceFlow;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        Iterator<IInstruction> instructions = this.sequenceFlow.iterateInstructions();
        while (instructions.hasNext()) {
            IInstruction instruction = instructions.next();

            IStatement statement = StatementFactory.fromInstruction(instruction);
            builder.append(statement.toString());
        }

        return builder.toString();
    }
}
