package cn.dyr.rest.generator.java.meta.parameters.annotation;

/**
 * float 类型的注解参数
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class PrimitiveFloatAnnotationParameter extends AbstractAnnotationParameter {
    private float value;

    public PrimitiveFloatAnnotationParameter(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public PrimitiveFloatAnnotationParameter setValue(float value) {
        this.value = value;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_PRIMITIVE_FLOAT;
    }
}
