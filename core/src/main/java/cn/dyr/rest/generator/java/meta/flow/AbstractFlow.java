package cn.dyr.rest.generator.java.meta.flow;

import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;

/**
 * 程序工作流的部分功能的实现
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public abstract class AbstractFlow implements IFlow {

    @Override
    public abstract int getFlowType();

    @Override
    public int getInstructionType() {
        return IInstruction.INSTRUCTION_TYPE_FLOW;
    }

    @Override
    public IInstruction invoke(String methodName, ParameterValue[] parameters) {
        throw new IllegalStateException("method invocation is not allowed in flow instruction");
    }

    @Override
    public IInstruction invoke(String methodName, Object[] parameters) {
        throw new IllegalStateException("method invocation is not allowed in flow instruction");
    }

    @Override
    public IValueExpression toValueExpression() {
        throw new IllegalStateException("flow instruction cannot be converted to value expression");
    }
}
