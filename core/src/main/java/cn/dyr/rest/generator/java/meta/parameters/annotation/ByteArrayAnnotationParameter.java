package cn.dyr.rest.generator.java.meta.parameters.annotation;

/**
 * byte 数组类型的注解参数
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ByteArrayAnnotationParameter extends AbstractAnnotationParameter {
    private byte[] value;

    public ByteArrayAnnotationParameter(byte[] value) {
        this.value = value;
    }

    public byte[] getValue() {
        return value;
    }

    public ByteArrayAnnotationParameter setValue(byte[] value) {
        this.value = value;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_PRIMITIVE_ARRAY_BYTE;
    }
}