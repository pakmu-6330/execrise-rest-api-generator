package cn.dyr.rest.generator.java.generator.elements;

import cn.dyr.rest.generator.exception.CodeGenerationException;
import cn.dyr.rest.generator.java.generator.ImportedTypeManager;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory;
import cn.dyr.rest.generator.java.meta.flow.expression.BinaryOperatorExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.ChainValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.ClassStaticValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.ConstructorInvocationValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.EmptyValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.EnumerationValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.PrefixSingleOperandOperationExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.SuffixSingleOperandOperationExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.VariableExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.constant.AbstractConstantExpression;
import cn.dyr.rest.generator.java.meta.flow.expression.constant.StringValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;
import cn.dyr.rest.generator.util.CommaStringBuilder;

/**
 * 所有的值表达式到 java 语法标识的转换类都在这里
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ValueExpressionToken implements IToken {

    private IValueExpression valueExpression;

    public ValueExpressionToken(IValueExpression valueExpression) {
        this.valueExpression = valueExpression;
    }

    public IValueExpression getValueExpression() {
        return valueExpression;
    }

    public ValueExpressionToken setValueExpression(IValueExpression valueExpression) {
        this.valueExpression = valueExpression;
        return this;
    }

    private String handleMethodParameters(ParameterValue[] values) {
        CommaStringBuilder builder = new CommaStringBuilder();

        if (values != null && values.length > 0) {
            for (ParameterValue parameterValue : values) {
                ParameterValueToken token = new ParameterValueToken(parameterValue);
                builder.addValue(token.toString());
            }
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        // 上来之前先做个判空，不急
        if (this.valueExpression == null) {
            throw new CodeGenerationException("value expression is null")
                    .setGenerationField(CodeGenerationException.CodeGenerationField.TOKEN);
        }

        // 这个方法是将值表达式的对象转换成代码当中相应的元素
        String targetValue = "";

        if (this.valueExpression instanceof AbstractConstantExpression) {
            targetValue = ((AbstractConstantExpression) this.valueExpression).getValue();
        } else if (this.valueExpression instanceof EmptyValueExpression) {
            targetValue = "";
        } else if (this.valueExpression instanceof VariableExpression) {
            targetValue = ((VariableExpression) this.valueExpression).getName();
        } else if (this.valueExpression instanceof BinaryOperatorExpression) {
            BinaryOperatorExpression expression = (BinaryOperatorExpression) this.valueExpression;

            ValueExpressionToken leftToken = new ValueExpressionToken(expression.getLeftExpression());
            ValueExpressionToken rightToken = new ValueExpressionToken(expression.getRightExpression());

            StringBuilder builder = new StringBuilder();

            // 处理运算符左边的表达式
            if (expression.isLeftExpressionComplexExpression()) {
                builder.append("(");
            }
            builder.append(leftToken);
            if (expression.isLeftExpressionComplexExpression()) {
                builder.append(")");
            }

            // 添加运算符
            builder.append(" ");
            builder.append(((BinaryOperatorExpression) this.valueExpression).getOperator());
            builder.append(" ");

            // 处理运算符右边的表达式
            if (expression.isRightExpressionComplexExpression()) {
                builder.append("(");
            }
            builder.append(rightToken);
            if (expression.isRightExpressionComplexExpression()) {
                builder.append(")");
            }

            targetValue = builder.toString();
        } else if (this.valueExpression instanceof PrefixSingleOperandOperationExpression) {
            PrefixSingleOperandOperationExpression value = (PrefixSingleOperandOperationExpression) this.valueExpression;

            ValueExpressionToken expressionToken = new ValueExpressionToken(value.getOperand());
            targetValue = value.getOperator() + expressionToken.toString();
        } else if (this.valueExpression instanceof SuffixSingleOperandOperationExpression) {
            SuffixSingleOperandOperationExpression value = (SuffixSingleOperandOperationExpression) this.valueExpression;

            ValueExpressionToken expressionToken = new ValueExpressionToken(value.getOperand());
            targetValue = expressionToken.toString() + value.getOperator();
        } else if (this.valueExpression instanceof ChainValueExpression) {
            ChainValueExpression chainValueExpression = (ChainValueExpression) this.valueExpression;

            StringBuilder builder = new StringBuilder();
            IValueExpression parentValue = chainValueExpression.getParentValue();
            ParameterValue[] parameterValues = chainValueExpression.getParameterValues();

            // 这个布尔值是用来指定是否输出方法前的一串调用
            boolean printParentValue = false;

            // 方法调用前的一系列内容是否输出的判定条件：
            // 1. 如果前面的表达式不为空，则不输出
            // 2. 如果这个表达式是静态调用，如果已经静态导入，则直接输出方法名，不输出前面的类名报名等信息
            if (parentValue != ValueExpressionFactory.empty()) {
                if (parentValue instanceof ClassStaticValueExpression) {
                    ImportedTypeManager manager = ImportedTypeManager.get();
                    TypeInfo staticClassType = ((ClassStaticValueExpression) parentValue).getTypeInfo();

                    if (manager.isTypeStaticImported(staticClassType, chainValueExpression.getName())) {
                        printParentValue = false;
                    } else {
                        printParentValue = true;
                    }
                } else {
                    printParentValue = true;
                }
            }

            if (printParentValue) {
                ValueExpressionToken parentToken = new ValueExpressionToken(parentValue);
                builder.append(parentToken.toString());
                builder.append(".");
            }

            builder.append(chainValueExpression.getName());

            switch (chainValueExpression.getGenerateType()) {
                case ChainValueExpression.GENERATE_TYPE_METHOD_INVOCATION:
                case ChainValueExpression.GENERATE_TYPE_STATIC_METHOD_INVOCATION: {
                    builder.append("(");
                    builder.append(handleMethodParameters(chainValueExpression.getParameterValues()));
                    builder.append(")");
                }
                break;
                case ChainValueExpression.GENERATE_TYPE_FIELD_ACCESS:
                case ChainValueExpression.GENERATE_TYPE_STATIC_FIELD_ACCESS: {
                    // 不用做任何事情
                }
                break;
            }

            targetValue = builder.toString();
        } else if (valueExpression instanceof ConstructorInvocationValueExpression) {
            ConstructorInvocationValueExpression invocationValueExpression = (ConstructorInvocationValueExpression) valueExpression;
            TypeInfoToken targetInfoToken = new TypeInfoToken(invocationValueExpression.getTypeInfo());

            StringBuilder builder = new StringBuilder();
            builder.append("new ");
            builder.append(targetInfoToken.toString());
            builder.append("(");
            builder.append(handleMethodParameters(invocationValueExpression.getParameterValues()));
            builder.append(")");

            targetValue = builder.toString();
        } else if (valueExpression instanceof StringValueExpression) {
            String value = ((StringValueExpression) valueExpression).getValue();

            targetValue = String.format("\"%s\"", value);
        } else if (valueExpression instanceof ClassStaticValueExpression) {
            targetValue = ((ClassStaticValueExpression) valueExpression).getTypeInfo().getName();
        } else if (valueExpression instanceof EnumerationValueExpression) {
            TypeInfoToken typeInfoToken = new TypeInfoToken(((EnumerationValueExpression) valueExpression).getEnumerationTypeInfo());
            String member = ((EnumerationValueExpression) valueExpression).getMember();

            targetValue = typeInfoToken.toString() + "." + member;
        } else {
            throw new IllegalArgumentException("unsupported value expression type: " + valueExpression.getClass().getName());
        }

        if (!valueExpression.isSimpleExpression()) {
            return "(" + targetValue + ")";
        } else {
            return targetValue;
        }
    }
}
