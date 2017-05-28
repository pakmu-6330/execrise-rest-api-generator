package cn.dyr.rest.generator.java.meta.flow.instruction;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;

/**
 * 一个空行的实现类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class EmptyInstruction implements IInstruction {
    @Override
    public void fillImportOperations(ImportContext context) {

    }

    @Override
    public int getInstructionType() {
        return IInstruction.INSTRUCTION_TYPE_EMPTY;
    }

    @Override
    public IInstruction invoke(String methodName, ParameterValue[] parameters) {
        throw new IllegalStateException("method invocation is not allowed in empty instruction");
    }

    @Override
    public IInstruction invoke(String methodName, Object[] parameters) {
        throw new IllegalStateException("method invocation is not allowed in empty instruction");
    }

    @Override
    public IInstruction invoke(String methodName, Object parameter) {
        throw new IllegalStateException("method invocation is not allowed in empty instruction");
    }

    @Override
    public IValueExpression toValueExpression() {
        throw new IllegalStateException("empty instruction cannot be converted to value expression");
    }
}
