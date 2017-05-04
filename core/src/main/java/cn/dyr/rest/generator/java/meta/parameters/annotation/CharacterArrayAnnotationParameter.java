package cn.dyr.rest.generator.java.meta.parameters.annotation;

/**
 * char 数组类型的注解参数
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class CharacterArrayAnnotationParameter extends AbstractAnnotationParameter {

    private char[] value;

    public CharacterArrayAnnotationParameter(char[] value) {
        this.value = value;
    }

    public char[] getValue() {
        return value;
    }

    public CharacterArrayAnnotationParameter setValue(char[] value) {
        this.value = value;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_PRIMITIVE_ARRAY_CHAR;
    }

}
