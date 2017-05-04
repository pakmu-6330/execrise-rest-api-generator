package cn.dyr.rest.generator.java.meta.parameters.annotation;

/**
 * boolean 数组类型的注解参数
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class BooleanArrayAnnotationParameter extends AbstractAnnotationParameter {

    private boolean[] value;

    public BooleanArrayAnnotationParameter(boolean[] value) {
        this.value = value;
    }

    public boolean[] getValue() {
        return value;
    }

    public BooleanArrayAnnotationParameter setValue(boolean[] value) {
        this.value = value;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_PRIMITIVE_ARRAY_BOOLEAN;
    }
}
