package cn.dyr.rest.generator.java.meta.parameters.annotation;

/**
 * double 数组类型的注解参数
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DoubleArrayAnnotationParameter extends AbstractAnnotationParameter {

    private double[] value;

    public DoubleArrayAnnotationParameter(double[] value) {
        this.value = value;
    }

    public double[] getValue() {
        return value;
    }

    public DoubleArrayAnnotationParameter setValue(double[] value) {
        this.value = value;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_PRIMITIVE_ARRAY_DOUBLE;
    }
}
