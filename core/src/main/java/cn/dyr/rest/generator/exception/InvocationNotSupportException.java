package cn.dyr.rest.generator.exception;

/**
 * 如果当前的值表达式不能进行方法调用，则抛出这个异常
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class InvocationNotSupportException extends RuntimeException {

    public InvocationNotSupportException(String message) {
        super(message);
    }
}
