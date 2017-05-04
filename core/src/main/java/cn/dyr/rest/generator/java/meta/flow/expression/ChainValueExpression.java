package cn.dyr.rest.generator.java.meta.flow.expression;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.generator.analysis.ImportedOperation;
import cn.dyr.rest.generator.java.generator.elements.ParameterValueToken;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;
import cn.dyr.rest.generator.util.CommaStringBuilder;

/**
 * 这个值对象用于表示字段，信息等链条式调用获得的值
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ChainValueExpression extends AbstractExpression {

    /**
     * 表示这个链式的值是由方法的调用产生的
     */
    public static final int GENERATE_TYPE_METHOD_INVOCATION = 1;

    /**
     * 表示这个链式的值是由字段的访问产生的
     */
    public static final int GENERATE_TYPE_FIELD_ACCESS = 2;

    /**
     * 表示这个链式的值是由静态方法的访问产生的
     */
    public static final int GENERATE_TYPE_STATIC_METHOD_INVOCATION = 3;

    /**
     * 表示这个链式的值是由静态类的字段访问产生的
     */
    public static final int GENERATE_TYPE_STATIC_FIELD_ACCESS = 4;

    private IValueExpression parentValue;
    private String name;
    private ParameterValue[] parameterValues;
    private int generateType;

    private TypeInfo staticMethodType;
    private boolean staticImport;

    public ChainValueExpression() {
    }

    public IValueExpression getParentValue() {
        return parentValue;
    }

    public ChainValueExpression setParentValue(IValueExpression parentValue) {
        this.parentValue = parentValue;
        return this;
    }

    public String getName() {
        return name;
    }

    public ChainValueExpression setName(String name) {
        this.name = name;
        return this;
    }

    public ParameterValue[] getParameterValues() {
        return parameterValues;
    }

    public ChainValueExpression setParameterValues(ParameterValue[] parameterValues) {
        this.parameterValues = parameterValues;
        return this;
    }

    public int getGenerateType() {
        return generateType;
    }

    public ChainValueExpression setGenerateType(int generateType) {
        this.generateType = generateType;
        return this;
    }

    public TypeInfo getStaticMethodType() {
        return staticMethodType;
    }

    public ChainValueExpression setStaticMethodType(TypeInfo staticMethodType) {
        this.staticMethodType = staticMethodType;
        return this;
    }

    public boolean isStaticImport() {
        return staticImport;
    }

    public ChainValueExpression setStaticImport(boolean staticImport) {
        this.staticImport = staticImport;
        return this;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        // 如果当前值是由方法调用派生的，则将参数值所引用进行导入
        if (parameterValues != null && parameterValues.length > 0) {
            for (ParameterValue parameterValue : this.parameterValues) {
                parameterValue.fillImportOperations(context);
            }
        }

        // 根据调用链往上溯源
        if (this.generateType == GENERATE_TYPE_METHOD_INVOCATION ||
                this.generateType == GENERATE_TYPE_STATIC_METHOD_INVOCATION) {

            if (this.generateType == GENERATE_TYPE_STATIC_METHOD_INVOCATION) {
                if (this.staticMethodType != null && this.staticImport) {
                    context.addStaticImport(staticMethodType, this.name, ImportedOperation.TARGET_TYPE_CLASS_INTERFACE);
                }
            }

            if (this.parentValue != null) {
                this.parentValue.fillImportOperations(context);
            }

            if (this.parameterValues != null && this.parameterValues.length > 0) {
                for (ParameterValue parameterValue : this.parameterValues) {
                    parameterValue.fillImportOperations(context);
                }
            }
        } else if (this.generateType == GENERATE_TYPE_FIELD_ACCESS ||
                this.generateType == GENERATE_TYPE_STATIC_FIELD_ACCESS) {
            if (this.generateType == GENERATE_TYPE_FIELD_ACCESS) {
                if (this.staticMethodType != null && this.staticImport) {
                    context.addStaticImport(staticMethodType, this.name, ImportedOperation.TARGET_TYPE_CLASS_INTERFACE);
                }
            }

            if (this.parentValue != null) {
                this.parentValue.fillImportOperations(context);
            }
        }
    }
}
