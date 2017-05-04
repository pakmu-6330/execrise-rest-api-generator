package cn.dyr.rest.generator.java.meta.parameters.annotation;

/**
 * short 类型的注解参数对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class PrimitiveShortAnnotationParameter extends AbstractAnnotationParameter {

    private short value;

    public PrimitiveShortAnnotationParameter(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    public PrimitiveShortAnnotationParameter setValue(short value) {
        this.value = value;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_PRIMITIVE_SHORT;
    }

}
