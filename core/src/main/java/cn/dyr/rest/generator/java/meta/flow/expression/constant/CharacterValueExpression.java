package cn.dyr.rest.generator.java.meta.flow.expression.constant;

/**
 * 表示一个 char 类型的常量表达式
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class CharacterValueExpression extends AbstractConstantExpression {

    /**
     * 创建一个以字符形式呈现的字符常量
     *
     * @param value 字符常量
     */
    public CharacterValueExpression(char value) {
        super(String.format("'%c'", value));
    }

    /**
     * 创建一个以 \\uxxxx 形式呈现的字符常量
     *
     * @param value utf 值
     */
    public CharacterValueExpression(int value) {
        super(String.format("'\\u%x'", value));
    }

}
