package cn.dyr.rest.generator.java.meta.parameters.annotation;

import cn.dyr.rest.generator.java.generator.ImportedTypeManager;
import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.generator.analysis.ImportedOperation;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.util.ArrayStringBuilder;

import java.util.Set;

/**
 * 枚举元素数组的注解参数
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class EnumArrayAnnotationParameter extends AbstractAnnotationParameter {

    private TypeInfo enumType;
    private String[] value;

    public EnumArrayAnnotationParameter() {
    }

    public TypeInfo getEnumType() {
        return enumType;
    }

    public EnumArrayAnnotationParameter setEnumType(TypeInfo enumType) {
        this.enumType = enumType;
        return this;
    }

    public String[] getValue() {
        return value;
    }

    public EnumArrayAnnotationParameter setValue(String[] value) {
        this.value = value;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_ARRAY_ENUM;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        context.addImportOperation(this.enumType, ImportedOperation.TARGET_TYPE_ANNOTATION_PARAMETER);
    }
}
