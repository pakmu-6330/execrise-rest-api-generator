package cn.dyr.rest.generator.java.generator.statement.method;

import cn.dyr.rest.generator.java.generator.IndentManager;
import cn.dyr.rest.generator.java.generator.elements.ElementsConstant;
import cn.dyr.rest.generator.java.generator.elements.ValueExpressionToken;
import cn.dyr.rest.generator.java.generator.statement.IStatement;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.flow.instruction.MethodInvocationInstruction;

/**
 * 表示一个调用方法的语句
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class MethodInvocationStatement implements IStatement {

    private MethodInvocationInstruction instruction;

    public MethodInvocationStatement(MethodInvocationInstruction instruction) {
        this.instruction = instruction;
    }

    public MethodInvocationInstruction getInstruction() {
        return instruction;
    }

    public MethodInvocationStatement setInstruction(MethodInvocationInstruction instruction) {
        this.instruction = instruction;
        return this;
    }

    @Override
    public String toString() {
        IndentManager indentManager = IndentManager.get();

        IValueExpression expression = instruction.getValue();
        IValueExpression retValueExpression = expression.invokeMethod(instruction.getMethodName(), instruction.getParameterValues());

        ValueExpressionToken token = new ValueExpressionToken(retValueExpression);
        return String.format("%s%s;%s", indentManager.getIndentString(), token.toString(), ElementsConstant.LINE_SEPARATOR);
    }
}
