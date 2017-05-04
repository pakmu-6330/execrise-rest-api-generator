package cn.dyr.rest.generator.java.generator.statement.method;

import cn.dyr.rest.generator.java.generator.IndentManager;
import cn.dyr.rest.generator.java.generator.elements.ElementsConstant;
import cn.dyr.rest.generator.java.generator.elements.ValueExpressionToken;
import cn.dyr.rest.generator.java.generator.statement.IStatement;
import cn.dyr.rest.generator.java.meta.flow.instruction.AssignmentInstruction;

/**
 * 表示一个赋值语句
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class AssignmentStatement implements IStatement {

    private AssignmentInstruction assignmentInstruction;

    public AssignmentStatement(AssignmentInstruction assignmentInstruction) {
        if (assignmentInstruction == null) {
            throw new NullPointerException("instruction cannot be null");
        }

        this.assignmentInstruction = assignmentInstruction;
    }

    public AssignmentInstruction getAssignmentInstruction() {
        return assignmentInstruction;
    }

    public AssignmentStatement setAssignmentInstruction(AssignmentInstruction assignmentInstruction) {
        if (assignmentInstruction == null) {
            throw new NullPointerException("instruction cannot be null");
        }

        this.assignmentInstruction = assignmentInstruction;
        return this;
    }

    @Override
    public String toString() {
        IndentManager indentManager = IndentManager.get();

        ValueExpressionToken targetVariableToken = new ValueExpressionToken(this.assignmentInstruction.getTargetVariable());
        ValueExpressionToken valueToken = new ValueExpressionToken(this.assignmentInstruction.getValue());

        return String.format("%s%s = %s;%s",
                indentManager.getIndentString(),
                targetVariableToken.toString(),
                valueToken.toString(),
                ElementsConstant.LINE_SEPARATOR);
    }
}