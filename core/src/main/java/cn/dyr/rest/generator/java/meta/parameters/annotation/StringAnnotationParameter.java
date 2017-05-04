package cn.dyr.rest.generator.java.meta.parameters.annotation;

/**
 * String 类型的注释参数
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class StringAnnotationParameter extends AbstractAnnotationParameter {

    private String value;

    public StringAnnotationParameter(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public StringAnnotationParameter setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_STRING;
    }

}
