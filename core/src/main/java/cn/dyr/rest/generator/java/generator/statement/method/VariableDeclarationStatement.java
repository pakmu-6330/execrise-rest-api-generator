package cn.dyr.rest.generator.java.generator.statement.method;

import cn.dyr.rest.generator.java.generator.IndentManager;
import cn.dyr.rest.generator.java.generator.elements.ElementsConstant;
import cn.dyr.rest.generator.java.generator.elements.TypeInfoToken;
import cn.dyr.rest.generator.java.generator.elements.ValueExpressionToken;
import cn.dyr.rest.generator.java.generator.statement.IStatement;
import cn.dyr.rest.generator.java.meta.flow.instruction.AssignmentInstruction;

/**
 * 表示一个变量声明的语句
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class VariableDeclarationStatement implements IStatement {

    private AssignmentInstruction assignmentInstruction;

    public VariableDeclarationStatement(AssignmentInstruction assignmentInstruction) {
        this.assignmentInstruction = assignmentInstruction;
    }

    @Override
    public String toString() {
        IndentManager indentManager = IndentManager.get();
        StringBuilder builder = new StringBuilder();

        if (assignmentInstruction != null && assignmentInstruction.isDeclaration()) {
            TypeInfoToken token = new TypeInfoToken(assignmentInstruction.getTargetValueType());
            ValueExpressionToken valueExpressionToken = new ValueExpressionToken(assignmentInstruction.getTargetVariable());

            builder.append(indentManager.getIndentString());
            builder.append(token.toString());
            builder.append(" ");
            builder.append(valueExpressionToken.toString());

            // 如果表达式的值不为空，则表示这个变量声明的时候会赋初值
            if (assignmentInstruction.getValue() != null) {
                builder.append(" = ");

                ValueExpressionToken fromExpression = new ValueExpressionToken(assignmentInstruction.getValue());
                builder.append(fromExpression.toString());
            }

            builder.append(";");
            builder.append(ElementsConstant.LINE_SEPARATOR);
            return builder.toString();
        }

        throw new IllegalArgumentException();
    }
}
