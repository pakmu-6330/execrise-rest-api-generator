package cn.dyr.rest.generator.java.generator.statement.method;

import cn.dyr.rest.generator.java.generator.IndentManager;
import cn.dyr.rest.generator.java.generator.elements.ValueExpressionToken;
import cn.dyr.rest.generator.java.generator.statement.IStatement;
import cn.dyr.rest.generator.java.meta.flow.instruction.ExceptionThrowInstruction;

import java.util.Objects;

/**
 * 表示一条抛出异常的语句
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ExceptionThrowStatement implements IStatement {

    private ExceptionThrowInstruction instruction;

    public ExceptionThrowStatement(ExceptionThrowInstruction instruction) {
        Objects.requireNonNull(instruction, "instruction null");

        this.instruction = instruction;
    }

    public ExceptionThrowInstruction getInstruction() {
        return instruction;
    }

    public ExceptionThrowStatement setInstruction(ExceptionThrowInstruction instruction) {
        this.instruction = instruction;
        return this;
    }

    @Override
    public String toString() {
        IndentManager indentManager = IndentManager.get();

        ValueExpressionToken token = new ValueExpressionToken(this.instruction.getValueExpression());

        return String.format("%sthrow %s;%s",
                indentManager.getIndentString(),
                token.toString(),
                System.lineSeparator());
    }
}
