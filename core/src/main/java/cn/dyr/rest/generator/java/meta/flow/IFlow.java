package cn.dyr.rest.generator.java.meta.flow;

/**
 * 表示代码逻辑的一个基类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IFlow extends IInstruction {

    /**
     * 表示顺序程序结构
     */
    int FLOW_SEQUENCE = 1;

    /**
     * 表示分支结构
     */
    int FLOW_CHOICE = 2;

    /**
     * 表示循环结构
     */
    int FLOW_LOOP = 3;

    /**
     * 获得这个程序控制流的类型
     *
     * @return 程序控制流的类型
     */
    int getFlowType();
}
