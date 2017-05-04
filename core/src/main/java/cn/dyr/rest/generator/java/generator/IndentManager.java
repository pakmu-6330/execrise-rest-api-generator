package cn.dyr.rest.generator.java.generator;

import cn.dyr.rest.generator.java.generator.elements.ElementsConstant;

/**
 * 用于进行代码缩进管理的类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class IndentManager {

    private static ThreadLocal<IndentManager> managers;

    static {
        managers = new ThreadLocal<>();
    }

    private int level;

    private IndentManager() {
        this.level = 0;
        // System.out.println("--- 构造方法 --- " + this.level);
        // InfoPrinter.printStackTrace();
    }

    /**
     * 如果有一个索引管理的类与当前线程相关联，则返回这个类实例；否则则创建一个新的类实例，并且绑定到当前线程上面
     *
     * @return 一个缩进管理类的实例
     */
    public static IndentManager get() {
        IndentManager indentManager = managers.get();
        if (indentManager == null) {
            indentManager = new IndentManager();
            managers.set(indentManager);
        }

        return indentManager;
    }

    /**
     * 删除与当前类相关的缩进管理
     */
    public static void release() {
        managers.remove();
    }

    /**
     * 往当前的缩进级别增加一级
     */
    public void indent() {
        this.level++;

        // System.out.println("--- 增加缩进级别的调用栈 --- " + this.level);
        // InfoPrinter.printStackTrace();
    }

    /**
     * 将当前的缩进级别降低一级
     */
    public void delIndent() {
        this.level--;
    }

    /**
     * 获得当前的缩进等级
     *
     * @return 当前的缩进等级
     */
    public int getCurrentIndentLevel() {
        return this.level;
    }

    /**
     * 获得这个缩进等级相应的字符串
     *
     * @return 当前缩进等级的缩进字符串
     */
    public String getIndentString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < this.level; i++) {
            builder.append(ElementsConstant.CODE_INDENT);
        }

        return builder.toString();
    }
}
