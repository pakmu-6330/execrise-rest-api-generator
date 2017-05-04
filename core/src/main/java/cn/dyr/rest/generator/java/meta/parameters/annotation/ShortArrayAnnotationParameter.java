package cn.dyr.rest.generator.java.meta.parameters.annotation;

/**
 * short 数组类型的注解参数
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ShortArrayAnnotationParameter extends AbstractAnnotationParameter {

    private short[] value;

    public ShortArrayAnnotationParameter(short[] value) {
        this.value = value;
    }

    public short[] getValue() {
        return value;
    }

    public ShortArrayAnnotationParameter setValue(short[] value) {
        this.value = value;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_PRIMITIVE_ARRAY_SHORT;
    }

}
