package cn.dyr.rest.generator.java.meta.flow.instruction;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;

/**
 * 表示一条函数返回语句
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ReturnInstruction implements IInstruction {

    private IValueExpression returnValue;

    public ReturnInstruction(IValueExpression returnValue) {
        this.returnValue = returnValue;
    }

    public IValueExpression getReturnValue() {
        return returnValue;
    }

    public ReturnInstruction setReturnValue(IValueExpression returnValue) {
        this.returnValue = returnValue;
        return this;
    }

    @Override
    public int getInstructionType() {
        return IInstruction.INSTRUCTION_TYPE_RETURN;
    }

    @Override
    public IInstruction invoke(String methodName, ParameterValue[] parameters) {
        throw new IllegalStateException("method invocation is not allowed in return instruction");
    }

    @Override
    public IInstruction invoke(String methodName, Object[] parameters) {
        throw new IllegalStateException("method invocation is not allowed in return instruction");
    }

    @Override
    public IValueExpression toValueExpression() {
        throw new IllegalStateException("return instruction cannot be converted to value expression");
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        this.returnValue.fillImportOperations(context);
    }
}
