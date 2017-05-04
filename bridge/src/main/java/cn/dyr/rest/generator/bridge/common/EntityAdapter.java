package cn.dyr.rest.generator.bridge.common;

/**
 * 用于不同模块之间相互转换的适配器
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface EntityAdapter<A, B> {

    A fromB(B b);

    B fromA(A b);

}
