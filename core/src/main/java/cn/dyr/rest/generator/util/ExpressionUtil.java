package cn.dyr.rest.generator.util;

import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;

/**
 * 表达式相关的工具类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ExpressionUtil {

    /**
     * 判断这个表达式是否属于复杂类型的表达式
     * @param expression 要进行判断的表达式
     * @return 如果这个表达式是复杂类型的表达式，则返回 true；否则返回 false
     */
    public static boolean isComplexExpression(IValueExpression expression) {
        return false;
    }

}
