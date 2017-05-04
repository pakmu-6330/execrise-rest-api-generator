package cn.dyr.rest.generator.java.meta.parameters.annotation;

/**
 * String 数组类型的注解参数
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class StringArrayAnnotationParameter extends AbstractAnnotationParameter {

    private String[] value;

    public StringArrayAnnotationParameter(String[] value) {
        this.value = value;
    }

    public String[] getValue() {
        return value;
    }

    public StringArrayAnnotationParameter setValue(String[] value) {
        this.value = value;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_ARRAY_STRING;
    }

}
