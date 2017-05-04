package cn.dyr.rest.generator.java.meta.parameters.annotation;

import cn.dyr.rest.generator.java.generator.ImportedTypeManager;
import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.generator.analysis.ImportedOperation;
import cn.dyr.rest.generator.java.meta.TypeInfo;

import java.util.Set;

/**
 * 用于表示一个枚举的注解参数
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class EnumAnnotationParameter extends AbstractAnnotationParameter {

    private TypeInfo enumType;
    private String value;

    public EnumAnnotationParameter() {
    }

    public TypeInfo getEnumType() {
        return enumType;
    }

    public EnumAnnotationParameter setEnumType(TypeInfo enumType) {
        this.enumType = enumType;
        return this;
    }

    public String getValue() {
        return value;
    }

    public EnumAnnotationParameter setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public int getParameterType() {
        return TYPE_ENUM;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        context.addImportOperation(this.enumType, ImportedOperation.TARGET_TYPE_ANNOTATION_PARAMETER);
    }
}
