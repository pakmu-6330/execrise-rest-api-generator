package cn.dyr.rest.generator.java.meta.flow.instruction;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;

import java.util.Objects;

/**
 * 这条指令表示表示抛出一个异常
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ExceptionThrowInstruction implements IInstruction {

    private IValueExpression valueExpression;

    public ExceptionThrowInstruction(IValueExpression valueExpression) {
        this.valueExpression = valueExpression;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        if (this.valueExpression != null) {
            this.valueExpression.fillImportOperations(context);
        }
    }

    public IValueExpression getValueExpression() {
        return valueExpression;
    }

    public ExceptionThrowInstruction setValueExpression(IValueExpression valueExpression) {
        Objects.requireNonNull(valueExpression, "value expression is null");

        this.valueExpression = valueExpression;
        return this;
    }

    @Override
    public int getInstructionType() {
        return IInstruction.INSTRUCTION_THROW_INSTRUCTION;
    }

    @Override
    public IInstruction invoke(String methodName, ParameterValue[] parameters) {
        throw new IllegalStateException("method invocation is not allowed on throw instruction");
    }

    @Override
    public IInstruction invoke(String methodName, Object[] parameters) {
        throw new IllegalStateException("method invocation is not allowed on throw instruction");
    }

    @Override
    public IValueExpression toValueExpression() {
        throw new IllegalStateException("throw instruction cannot be converted into value expression");
    }
}
