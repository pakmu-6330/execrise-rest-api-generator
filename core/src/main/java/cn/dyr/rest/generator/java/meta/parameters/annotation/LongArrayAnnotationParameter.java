package cn.dyr.rest.generator.java.meta.parameters.annotation;

/**
 * long 数组类型的注解参数
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class LongArrayAnnotationParameter extends AbstractAnnotationParameter {

    private long[] value;

    public LongArrayAnnotationParameter(long[] value) {
        this.value = value;
    }

    public long[] getValue() {
        return value;
    }

    public LongArrayAnnotationParameter setValue(long[] value) {
        this.value = value;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_PRIMITIVE_ARRAY_LONG;
    }
}
