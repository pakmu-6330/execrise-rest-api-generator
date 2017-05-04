package cn.dyr.rest.generator.java.meta.parameters.annotation;

/**
 * byte 类型的注解参数
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class PrimitiveByteAnnotationParameter extends AbstractAnnotationParameter {

    private byte value;

    PrimitiveByteAnnotationParameter(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public PrimitiveByteAnnotationParameter setValue(byte value) {
        this.value = value;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_PRIMITIVE_BYTE;
    }

}
