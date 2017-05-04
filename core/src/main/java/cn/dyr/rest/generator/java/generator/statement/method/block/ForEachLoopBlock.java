package cn.dyr.rest.generator.java.generator.statement.method.block;

import cn.dyr.rest.generator.java.generator.IndentManager;
import cn.dyr.rest.generator.java.generator.elements.TypeInfoToken;
import cn.dyr.rest.generator.java.generator.elements.ValueExpressionToken;
import cn.dyr.rest.generator.java.generator.statement.IStatement;
import cn.dyr.rest.generator.java.generator.statement.StatementFactory;
import cn.dyr.rest.generator.java.meta.flow.ForEachLoop;

import java.util.Objects;

/**
 * 表示一个 forEach 循环的语句
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ForEachLoopBlock implements IStatement {

    private ForEachLoop forEachLoop;

    public ForEachLoopBlock(ForEachLoop forEachLoop) {
        Objects.requireNonNull(forEachLoop, "loop object is null");

        this.forEachLoop = forEachLoop;
    }

    public ForEachLoop getForEachLoop() {
        return forEachLoop;
    }

    public ForEachLoopBlock setForEachLoop(ForEachLoop forEachLoop) {
        Objects.requireNonNull(forEachLoop, "loop object is null");

        this.forEachLoop = forEachLoop;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        IndentManager indentManager = IndentManager.get();

        // 生成第一行语句
        builder.append(indentManager.getIndentString());
        builder.append("for (");

        // 类型信息的生成
        TypeInfoToken typeInfoToken = new TypeInfoToken(forEachLoop.getLoopVariableType());
        builder.append(typeInfoToken.toString());
        builder.append(" ");

        // 变量名称的生成
        builder.append(forEachLoop.getLoopVariableName());
        builder.append(" : ");

        // 迭代对象表达式的生成
        ValueExpressionToken expressionToken = new ValueExpressionToken(forEachLoop.getLoopExpression());
        builder.append(expressionToken.toString());
        builder.append(") {");
        builder.append(System.lineSeparator());

        // 进行缩进
        indentManager.indent();
        IStatement bodyStatement = StatementFactory.fromInstruction(forEachLoop.getLoopBody());
        builder.append(bodyStatement.toString());

        // 循环结束，生成大括号，取消缩进
        indentManager.delIndent();
        builder.append(indentManager.getIndentString());
        builder.append("}");
        builder.append(System.lineSeparator());

        return builder.toString();
    }
}
