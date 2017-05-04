package cn.dyr.rest.generator.java.generator.elements;

/**
 * 表示生成语句当中的一个语法标识符
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IToken {

    /**
     * 用于将表示当前的语法标识符转换成字符串
     *
     * @return 这个语法标识对应的字符串
     */
    String toString();

}
