package cn.dyr.rest.generator.java.meta.flow;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.generator.analysis.ImportedOperation;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;

/**
 * 表示一个 forEach 循环
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ForEachLoop extends AbstractFlow {

    private TypeInfo loopVariableType;
    private String loopVariableName;
    private IValueExpression loopExpression;
    private IInstruction loopBody;

    public TypeInfo getLoopVariableType() {
        return loopVariableType;
    }

    public ForEachLoop setLoopVariableType(TypeInfo loopVariableType) {
        this.loopVariableType = loopVariableType;
        return this;
    }

    public String getLoopVariableName() {
        return loopVariableName;
    }

    public ForEachLoop setLoopVariableName(String loopVariableName) {
        this.loopVariableName = loopVariableName;
        return this;
    }

    public IValueExpression getLoopExpression() {
        return loopExpression;
    }

    public ForEachLoop setLoopExpression(IValueExpression loopExpression) {
        this.loopExpression = loopExpression;
        return this;
    }

    public IInstruction getLoopBody() {
        return loopBody;
    }

    public ForEachLoop setLoopBody(IInstruction loopBody) {
        this.loopBody = loopBody;
        return this;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        if (this.loopVariableType != null) {
            context.addImportOperation(this.loopVariableType, ImportedOperation.TARGET_TYPE_CLASS_INTERFACE);
        }

        if (this.loopExpression != null) {
            this.loopExpression.fillImportOperations(context);
        }

        if (this.loopBody != null) {
            this.loopBody.fillImportOperations(context);
        }
    }

    @Override
    public int getFlowType() {
        return IFlow.FLOW_LOOP;
    }
}
