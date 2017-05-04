package cn.dyr.rest.generator.java.meta.parameters.annotation;

/**
 * long 类型的注解参数
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class PrimitiveLongAnnotationParameter extends AbstractAnnotationParameter {

    private long value;

    public PrimitiveLongAnnotationParameter(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public PrimitiveLongAnnotationParameter setValue(long value) {
        this.value = value;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_PRIMITIVE_LONG;
    }
}
