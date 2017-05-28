package cn.dyr.rest.generator.java.generator.statement;

import cn.dyr.rest.generator.java.generator.IndentManager;
import cn.dyr.rest.generator.java.generator.elements.AnnotationToken;
import cn.dyr.rest.generator.java.generator.elements.ElementsConstant;
import cn.dyr.rest.generator.java.generator.elements.MethodParameterToken;
import cn.dyr.rest.generator.java.generator.elements.ModifierInfoToken;
import cn.dyr.rest.generator.java.generator.elements.TypeInfoToken;
import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.MethodInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.parameters.Parameter;
import cn.dyr.rest.generator.util.CommaStringBuilder;

import java.util.Iterator;
import java.util.List;

/**
 * 表示方法的语句
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class MethodStatement implements IStatement {

    private MethodInfo methodInfo;
    private boolean ignoreModifier;

    public MethodStatement(MethodInfo methodInfo) {
        this.methodInfo = methodInfo;
        this.ignoreModifier = false;
    }

    public MethodInfo getMethodInfo() {
        return methodInfo;
    }

    public MethodStatement setMethodInfo(MethodInfo methodInfo) {
        this.methodInfo = methodInfo;
        return this;
    }

    public boolean isIgnoreModifier() {
        return ignoreModifier;
    }

    public MethodStatement setIgnoreModifier(boolean ignoreModifier) {
        this.ignoreModifier = ignoreModifier;
        return this;
    }

    @Override
    public String toString() {
        IndentManager indentManager = IndentManager.get();
        StringBuilder builder = new StringBuilder();

        // 生成这个方法上面注解的信息
        Iterator<AnnotationInfo> annotationInfoIterator = this.methodInfo.iterateAnnotations();
        while (annotationInfoIterator.hasNext()) {
            AnnotationInfo annotationInfo = annotationInfoIterator.next();

            AnnotationToken token = new AnnotationToken(annotationInfo);
            builder.append(indentManager.getIndentString());
            builder.append(token);
            builder.append(ElementsConstant.LINE_SEPARATOR);
        }

        // 生成方法修饰符
        String modifierString = "";
        if (!this.ignoreModifier) {
            ModifierInfoToken modifierInfoToken = new ModifierInfoToken(this.methodInfo.getModifier());
            modifierString = modifierInfoToken.generateMethodModifierString();
        }

        // 生成返回值的标识
        TypeInfoToken typeInfoToken = new TypeInfoToken(this.methodInfo.getReturnValueType());
        String returnTypeString = typeInfoToken.toString();

        // 生成方法的参数部分
        CommaStringBuilder parametersStringBuilder = new CommaStringBuilder();
        List<Parameter> parameters = this.methodInfo.getParameters();
        for (Parameter parameter : parameters) {
            MethodParameterToken token = new MethodParameterToken(parameter);
            parametersStringBuilder.addValue(token.toString());
        }

        // 不同函数类型的不同函数签名格式
        // 普通方法：修饰符+返回值+方法名+形参列表
        // 构造方法：修饰符+类名（采用返回值代替）+形参列表

        builder.append(indentManager.getIndentString());
        builder.append(modifierString);
        if (modifierString != null && !modifierString.trim().equals("")) {
            builder.append(" ");
        }

        builder.append(returnTypeString);

        if (methodInfo.getMethodType() == MethodInfo.METHOD_TYPE_COMMON) {
            builder.append(" ");
            builder.append(this.methodInfo.getName());
        }

        builder.append("(");
        builder.append(parametersStringBuilder.toString());
        builder.append(")");

        // 生成 throws 语句
        List<TypeInfo> throwable = this.methodInfo.getThrowable();
        if (throwable != null && throwable.size() > 0) {
            CommaStringBuilder throwsCauseBuilder = new CommaStringBuilder();

            for (TypeInfo throwableType : throwable) {
                TypeInfoToken token = new TypeInfoToken(throwableType);
                throwsCauseBuilder.addValue(token.toString());
            }

            builder.append(" throws ");
            builder.append(throwsCauseBuilder.toString());
        }

        // 只有在方法不仅仅是定义的情况下才输出方法体
        if (!methodInfo.isDefineOnly()) {
            builder.append(" {");
            builder.append(ElementsConstant.LINE_SEPARATOR);

            // 增加一个缩进等级
            indentManager.indent();

            // 生成代码的逻辑
            handleMethodBody(builder);

            // 减少一个缩进等级
            indentManager.delIndent();

            builder.append(indentManager.getIndentString());
            builder.append("}");
        } else {
            builder.append(";");
        }

        return builder.toString();
    }

    private void handleMethodBody(StringBuilder builder) {
        IStatement statement = StatementFactory.fromInstruction(methodInfo.getRootInstruction());
        builder.append(statement.toString());
    }
}
