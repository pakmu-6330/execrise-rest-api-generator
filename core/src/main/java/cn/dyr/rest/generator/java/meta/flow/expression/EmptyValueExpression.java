package cn.dyr.rest.generator.java.meta.flow.expression;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;

/**
 * 表示一个不输出任何信息的空值表达式，用于调用特殊函数（父类或者本类）的场合
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class EmptyValueExpression extends AbstractExpression {

    @Override
    public void fillImportOperations(ImportContext context) {
        // 空白的值更不会引入 import 语句 
    }
}
