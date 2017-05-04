package cn.dyr.rest.generator.java.meta.parameters.annotation;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.generator.analysis.ImportedOperation;
import cn.dyr.rest.generator.java.meta.TypeInfo;

/**
 * class 数组类型的注解参数
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ClassArrayAnnotationParameter extends AbstractAnnotationParameter {

    private TypeInfo[] value;

    public ClassArrayAnnotationParameter(TypeInfo[] value) {
        this.value = value;
    }

    public TypeInfo[] getValue() {
        return value;
    }

    public ClassArrayAnnotationParameter setValue(TypeInfo[] value) {
        this.value = value;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_ARRAY_CLASS;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        if (this.value != null && this.value.length > 0) {
            for (TypeInfo typeInfo : this.value) {
                context.addImportOperation(typeInfo, ImportedOperation.TARGET_TYPE_ANNOTATION_PARAMETER);
            }
        }
    }
}
