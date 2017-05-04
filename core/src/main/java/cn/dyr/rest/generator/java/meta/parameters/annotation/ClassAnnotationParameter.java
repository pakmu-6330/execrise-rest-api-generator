package cn.dyr.rest.generator.java.meta.parameters.annotation;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.generator.analysis.ImportedOperation;
import cn.dyr.rest.generator.java.meta.TypeInfo;

import java.util.Set;

/**
 * class 类型的注解参数
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ClassAnnotationParameter extends AbstractAnnotationParameter {
    private TypeInfo typeInfo;

    public ClassAnnotationParameter(TypeInfo typeInfo) {
        this.typeInfo = typeInfo;
    }

    public TypeInfo getTypeInfo() {
        return typeInfo;
    }

    public ClassAnnotationParameter setTypeInfo(TypeInfo typeInfo) {
        this.typeInfo = typeInfo;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_CLASS;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        context.addImportOperation(this.typeInfo, ImportedOperation.TARGET_TYPE_ANNOTATION_PARAMETER);
    }
}
