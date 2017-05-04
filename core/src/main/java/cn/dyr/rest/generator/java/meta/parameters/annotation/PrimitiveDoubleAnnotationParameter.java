package cn.dyr.rest.generator.java.meta.parameters.annotation;

/**
 * double 类型的注解参数
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class PrimitiveDoubleAnnotationParameter extends AbstractAnnotationParameter {

    private double value;

    public PrimitiveDoubleAnnotationParameter(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public PrimitiveDoubleAnnotationParameter setValue(double value) {
        this.value = value;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_PRIMITIVE_DOUBLE;
    }
}

