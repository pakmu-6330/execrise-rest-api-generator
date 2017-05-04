package cn.dyr.rest.generator.exception;

/**
 * 表示在代码生成过程中未能够找到某个字段、方法返回值的类型
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class TypeInfoNotFound extends RuntimeException {

    private String expr;

    public TypeInfoNotFound(String expr) {
        super(String.format("type info not found for: %s", expr));
    }

    public String getExpr() {
        return expr;
    }
}
