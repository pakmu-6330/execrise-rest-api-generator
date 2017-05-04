package cn.dyr.rest.generator.ui.swing.exception;

/**
 * 这个类表示转换器在转换过程抛出的异常
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ConverterException extends RuntimeException {

    public ConverterException(String message) {
        super(message);
    }
}
