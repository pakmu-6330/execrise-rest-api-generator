package cn.dyr.rest.generator.java.meta.flow.instruction;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.meta.factory.ParameterValueFactory;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;

/**
 * 表示一条方法调用的指令
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class MethodInvocationInstruction implements IInstruction {

    private IValueExpression value;
    private String methodName;
    private ParameterValue[] parameterValues;

    public MethodInvocationInstruction() {
    }

    public IValueExpression getValue() {
        return value;
    }

    public MethodInvocationInstruction setValue(IValueExpression value) {
        this.value = value;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public MethodInvocationInstruction setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public ParameterValue[] getParameterValues() {
        return parameterValues;
    }

    public MethodInvocationInstruction setParameterValues(ParameterValue[] parameterValues) {
        this.parameterValues = parameterValues;
        return this;
    }

    @Override
    public int getInstructionType() {
        return IInstruction.INSTRUCTION_TYPE_METHOD_INVOCATION;
    }

    @Override
    public IInstruction invoke(String methodName, ParameterValue[] parameters) {
        // 得到当前方法调用指令的值表达式
        IValueExpression newLeftValueExpression = value.invokeMethod(this.methodName, this.parameterValues);

        // 在本方法调用的值表达式基础上产生一个调用其他方法的指令
        return new MethodInvocationInstruction()
                .setValue(newLeftValueExpression)
                .setMethodName(methodName)
                .setParameterValues(parameters);
    }

    @Override
    public IInstruction invoke(String methodName, Object[] parameters) {
        // 进行了参数的拼装操作
        ParameterValue[] values = ParameterValueFactory.fromObjects(parameters);

        // 调用另外一个函数
        return invoke(methodName, values);
    }

    @Override
    public IInstruction invoke(String methodName, Object parameter) {
        return invoke(methodName, new Object[]{parameter});
    }

    @Override
    public IValueExpression toValueExpression() {
        // 计算表达式返回
        return this.value.invokeMethod(this.methodName, this.parameterValues);
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        // 对引出值进行 import 处理
        if (this.value != null) {
            this.value.fillImportOperations(context);
        }

        // 对参数进行类型导入的处理
        if (this.parameterValues != null && this.parameterValues.length > 0) {
            for (ParameterValue value : this.parameterValues) {
                value.fillImportOperations(context);
            }
        }
    }
}
