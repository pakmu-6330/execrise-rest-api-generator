package cn.dyr.rest.generator.java.meta.flow.expression;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 表示一个变量的表达式
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class VariableExpression extends AbstractExpression {

    private static AtomicLong counter;

    static {
        counter = new AtomicLong();
    }

    private boolean isPrimitiveTypeVariable;
    private String name;

    public VariableExpression() {
        this.isPrimitiveTypeVariable = false;
        this.name = String.format("variable_%d", counter.incrementAndGet());
    }

    public boolean isPrimitiveTypeVariable() {
        return isPrimitiveTypeVariable;
    }

    public VariableExpression setPrimitiveTypeVariable(boolean primitiveTypeVariable) {
        isPrimitiveTypeVariable = primitiveTypeVariable;
        return this;
    }

    public String getName() {
        return name;
    }

    public VariableExpression setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        // 这里只是引用变量，没有什么实际的引入，所以不需要导入
    }
}
