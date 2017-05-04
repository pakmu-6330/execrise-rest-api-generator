package cn.dyr.rest.generator.java.meta.parameters.annotation;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.generator.analysis.ImportedOperation;
import cn.dyr.rest.generator.java.meta.AnnotationInfo;

/**
 * 表示注解数组参数的注解参数
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class AnnotationArrayAnnotationParameter extends AbstractAnnotationParameter {

    private AnnotationInfo[] value;

    public AnnotationArrayAnnotationParameter(AnnotationInfo[] value) {
        this.value = value;
    }

    public AnnotationInfo[] getValue() {
        return value;
    }

    public AnnotationArrayAnnotationParameter setValue(AnnotationInfo[] value) {
        this.value = value;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_ARRAY_ANNOTATION;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        if (value != null && this.value.length > 0) {
            for (AnnotationInfo info : this.value) {
                info.fillImportOperations(context);
            }
        }
    }
}
