package cn.dyr.rest.generator.java.meta.flow.instruction;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.generator.analysis.ImportedOperation;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.flow.expression.ChainValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.ClassStaticValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;

/**
 * 表示一个静态函数调用的指令
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class StaticMethodInstruction extends MethodInvocationInstruction {

    private boolean isStaticImport;

    /**
     * 判断该函数是否会被静态引入引入到文件当中
     *
     * @return 一个表示函数是否会被静态引入到类当中
     */
    public boolean isStaticImport() {
        return isStaticImport;
    }

    /**
     * 设置指定静态类是否引入的布尔值
     *
     * @param staticImport 这个静态函数调用是否会被静态引入
     * @return 这个方法调用指令本身
     */
    public StaticMethodInstruction setStaticImport(boolean staticImport) {
        isStaticImport = staticImport;
        return this;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        IValueExpression value = this.getValue();
        if (this.isStaticImport) {
            if (value instanceof ClassStaticValueExpression) {
                // 对直接的类调用进行引入
                context.addStaticImport(
                        ((ClassStaticValueExpression) value).getTypeInfo(),
                        this.getMethodName(), ImportedOperation.TARGET_TYPE_METHOD_PARAMETER_TYPE);
            }
        } else {
            if (value instanceof ClassStaticValueExpression) {
                // 引入静态类
                context.addImportOperation(
                        ((ClassStaticValueExpression) value).getTypeInfo(),
                        ImportedOperation.TARGET_TYPE_CLASS_INTERFACE);
            }
        }
    }

    @Override
    public IInstruction invoke(String methodName, ParameterValue[] parameters) {
        IValueExpression newValueExpression = toValueExpression();

        return new MethodInvocationInstruction()
                .setValue(newValueExpression)
                .setMethodName(methodName)
                .setParameterValues(parameters);
    }

    @Override
    public IValueExpression toValueExpression() {
        ChainValueExpression newValueExpression = new ChainValueExpression()
                .setParentValue(this.getValue())
                .setGenerateType(ChainValueExpression.GENERATE_TYPE_STATIC_METHOD_INVOCATION)
                .setName(this.getMethodName())
                .setStaticImport(this.isStaticImport)
                .setParameterValues(this.getParameterValues());

        if (this.getValue() instanceof ClassStaticValueExpression) {
            TypeInfo typeInfo = ((ClassStaticValueExpression) this.getValue()).getTypeInfo();
            newValueExpression.setStaticMethodType(typeInfo);
        }

        return newValueExpression;
    }
}