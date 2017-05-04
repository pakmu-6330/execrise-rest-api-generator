package cn.dyr.rest.generator.java.generator.statement.method;

import cn.dyr.rest.generator.java.generator.IndentManager;
import cn.dyr.rest.generator.java.generator.elements.ElementsConstant;
import cn.dyr.rest.generator.java.generator.elements.ValueExpressionToken;
import cn.dyr.rest.generator.java.generator.statement.IStatement;
import cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory;
import cn.dyr.rest.generator.java.meta.flow.instruction.ReturnInstruction;

/**
 * 表示一条函数返回的语句
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ReturnStatement implements IStatement {

    private ReturnInstruction instruction;

    public ReturnStatement(ReturnInstruction instruction) {
        this.instruction = instruction;
    }

    public ReturnInstruction getInstruction() {
        return instruction;
    }

    public ReturnStatement setInstruction(ReturnInstruction instruction) {
        this.instruction = instruction;
        return this;
    }

    @Override
    public String toString() {
        IndentManager indentManager = IndentManager.get();

        if (instruction.getReturnValue() == ValueExpressionFactory.voidExpression()) {
            return String.format("%s%s ;%s", indentManager.getIndentString(), "return", ElementsConstant.LINE_SEPARATOR);
        }

        ValueExpressionToken returnExpression = new ValueExpressionToken(instruction.getReturnValue());
        return String.format("%s%s %s;%s", indentManager.getIndentString(), "return", returnExpression.toString(), ElementsConstant.LINE_SEPARATOR);
    }
}
