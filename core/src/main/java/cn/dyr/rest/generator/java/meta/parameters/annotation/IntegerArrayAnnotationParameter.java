package cn.dyr.rest.generator.java.meta.parameters.annotation;

/**
 * int 数组类型的注解参数
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class IntegerArrayAnnotationParameter extends AbstractAnnotationParameter {

    private int[] value;

    public IntegerArrayAnnotationParameter(int[] value) {
        this.value = value;
    }

    public int[] getValue() {
        return value;
    }

    public IntegerArrayAnnotationParameter setValue(int[] value) {
        this.value = value;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_PRIMITIVE_ARRAY_INT;
    }
}
