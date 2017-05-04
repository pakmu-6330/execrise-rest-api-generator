package cn.dyr.rest.generator.java.meta.flow.instruction;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.generator.analysis.ImportedOperation;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;

/**
 * 表示这条指令是一条变量赋值的指令
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class AssignmentInstruction implements IInstruction {

    private boolean declaration;
    private TypeInfo targetValueType;
    private IValueExpression targetVariable;
    private IValueExpression value;

    public AssignmentInstruction() {
        this.declaration = false;
        this.targetValueType = TypeInfoFactory.objectType();
    }

    public boolean isDeclaration() {
        return declaration;
    }

    public AssignmentInstruction setDeclaration(boolean declaration) {
        this.declaration = declaration;
        return this;
    }

    public TypeInfo getTargetValueType() {
        return targetValueType;
    }

    public AssignmentInstruction setTargetValueType(TypeInfo targetValueType) {
        this.targetValueType = targetValueType;
        return this;
    }

    public IValueExpression getValue() {
        return value;
    }

    public AssignmentInstruction setValue(IValueExpression value) {
        this.value = value;
        return this;
    }

    public IValueExpression getTargetVariable() {
        return targetVariable;
    }

    public AssignmentInstruction setTargetVariable(IValueExpression targetVariable) {
        this.targetVariable = targetVariable;
        return this;
    }

    @Override
    public int getInstructionType() {
        return IInstruction.INSTRUCTION_TYPE_ASSIGNMENT;
    }

    @Override
    public IInstruction invoke(String methodName, ParameterValue[] parameters) {
        throw new IllegalStateException("method invocation is not allowed in assignment instruction");
    }

    @Override
    public IInstruction invoke(String methodName, Object[] parameters) {
        throw new IllegalStateException("method invocation is not allowed in assignment instruction");
    }

    @Override
    public IValueExpression toValueExpression() {
        throw new IllegalStateException("assignment instruction cannot be converted to value expression");
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        if (declaration) {
            context.addImportOperation(targetValueType, ImportedOperation.TARGET_TYPE_METHOD_PARAMETER_TYPE);
        }

        if (this.value != null) {
            this.value.fillImportOperations(context);
        }
    }
}
