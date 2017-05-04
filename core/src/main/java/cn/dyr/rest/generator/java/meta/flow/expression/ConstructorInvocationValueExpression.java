package cn.dyr.rest.generator.java.meta.flow.expression;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.generator.analysis.ImportedOperation;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;

/**
 * 表示调用某个类的构造器返回的值表达式
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ConstructorInvocationValueExpression extends AbstractExpression {

    private TypeInfo typeInfo;
    private ParameterValue[] parameterValues;

    public ConstructorInvocationValueExpression() {
        this.typeInfo = TypeInfoFactory.objectType();
        this.parameterValues = new ParameterValue[0];
    }

    public TypeInfo getTypeInfo() {
        return typeInfo;
    }

    public ConstructorInvocationValueExpression setTypeInfo(TypeInfo typeInfo) {
        if (typeInfo.isPrimitiveType()) {
            throw new IllegalArgumentException("cannot invoke constructor of primitive type");
        }

        this.typeInfo = typeInfo;
        return this;
    }

    public ParameterValue[] getParameterValues() {
        return parameterValues;
    }

    public ConstructorInvocationValueExpression setParameterValues(ParameterValue[] parameterValues) {
        this.parameterValues = parameterValues;
        return this;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        // 构造方法类型的导入
        if (this.typeInfo != null) {
            context.addImportOperation(this.typeInfo, ImportedOperation.TARGET_TYPE_METHOD_PARAMETER_TYPE);
        }

        // 对参数进行相应类型处理
        if (this.parameterValues != null && this.parameterValues.length > 0) {
            for (ParameterValue parameterValue : this.parameterValues) {
                parameterValue.fillImportOperations(context);
            }
        }
    }
}
