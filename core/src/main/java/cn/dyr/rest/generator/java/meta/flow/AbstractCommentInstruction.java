package cn.dyr.rest.generator.java.meta.flow;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;

/**
 * 表示一个注释指令的抽象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class AbstractCommentInstruction implements IInstruction {
    @Override
    public void fillImportOperations(ImportContext context) {

    }

    @Override
    public int getInstructionType() {
        return INSTRUCTION_TYPE_COMMENT;
    }

    @Override
    public IInstruction invoke(String methodName, ParameterValue[] parameters) {
        throw new IllegalStateException("method invocation is not allowed in comment instruction");
    }

    @Override
    public IInstruction invoke(String methodName, Object[] parameters) {
        throw new IllegalStateException("method invocation is not allowed in comment instruction");
    }

    @Override
    public IInstruction invoke(String methodName, Object parameter) {
        throw new IllegalStateException("method invocation is not allowed in comment instruction");
    }

    @Override
    public IValueExpression toValueExpression() {
        throw new IllegalStateException("comment cannot be converted into value expression");
    }
}
