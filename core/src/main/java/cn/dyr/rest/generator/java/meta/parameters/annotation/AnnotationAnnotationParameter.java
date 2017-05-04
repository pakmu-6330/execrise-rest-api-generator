package cn.dyr.rest.generator.java.meta.parameters.annotation;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.generator.elements.AnnotationToken;
import cn.dyr.rest.generator.java.meta.AnnotationInfo;

/**
 * 注解类型的注解参数
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class AnnotationAnnotationParameter extends AbstractAnnotationParameter {

    private AnnotationInfo value;

    public AnnotationAnnotationParameter(AnnotationInfo value) {
        this.value = value;
    }

    public AnnotationInfo getValue() {
        return value;
    }

    public AnnotationAnnotationParameter setValue(AnnotationInfo value) {
        this.value = value;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_ANNOTATION;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        value.fillImportOperations(context);
    }
}
