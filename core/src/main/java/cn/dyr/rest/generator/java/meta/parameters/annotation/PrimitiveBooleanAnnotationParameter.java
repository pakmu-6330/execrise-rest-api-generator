package cn.dyr.rest.generator.java.meta.parameters.annotation;

/**
 * boolean 类型的注解参数
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class PrimitiveBooleanAnnotationParameter extends AbstractAnnotationParameter {

    private boolean value;

    public PrimitiveBooleanAnnotationParameter(boolean value) {
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }

    public PrimitiveBooleanAnnotationParameter setValue(boolean value) {
        this.value = value;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_PRIMITIVE_BOOLEAN;
    }
}
