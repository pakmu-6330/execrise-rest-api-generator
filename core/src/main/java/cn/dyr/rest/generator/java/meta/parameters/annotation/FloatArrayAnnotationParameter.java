package cn.dyr.rest.generator.java.meta.parameters.annotation;

/**
 * float 数组类型的注解参数
 *
 * @author DENG YURONG
 * @version 0.1.00001
 */
public class FloatArrayAnnotationParameter extends AbstractAnnotationParameter {

    private float[] value;

    public FloatArrayAnnotationParameter(float[] value) {
        this.value = value;
    }

    public float[] getValue() {
        return value;
    }

    public FloatArrayAnnotationParameter setValue(float[] value) {
        this.value = value;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_PRIMITIVE_ARRAY_FLOAT;
    }
}
