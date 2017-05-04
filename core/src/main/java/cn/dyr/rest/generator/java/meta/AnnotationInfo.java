package cn.dyr.rest.generator.java.meta;

import cn.dyr.rest.generator.java.generator.analysis.IImportProcessor;
import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.generator.analysis.ImportedOperation;
import cn.dyr.rest.generator.java.meta.parameters.annotation.AnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.AnnotationParameterFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 表示一个注解信息
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class AnnotationInfo implements IImportProcessor {

    private TypeInfo type;
    private AnnotationParameter defaultValue;
    private Map<String, AnnotationParameter> parameterMap;

    public AnnotationInfo() {
        this.defaultValue = null;
        this.parameterMap = new HashMap<>();
    }

    public TypeInfo getType() {
        return type;
    }

    public AnnotationInfo setType(TypeInfo type) {
        this.type = type;
        return this;
    }

    public AnnotationParameter getDefaultValue() {
        return defaultValue;
    }

    public AnnotationInfo setDefaultValue(AnnotationParameter defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public Map<String, AnnotationParameter> getParameterMap() {
        return parameterMap;
    }

    public AnnotationInfo setParameterMap(Map<String, AnnotationParameter> parameterMap) {
        this.parameterMap = parameterMap;
        return this;
    }

    public AnnotationInfo addParameter(String name, AnnotationParameter value) {
        this.parameterMap.put(name, value);
        return this;
    }

    public AnnotationInfo addParameter(String name, Object value) {
        this.parameterMap.put(name, AnnotationParameterFactory.from(value));
        return this;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        // 添加注解本身的类型信息
        context.addImportOperation(this.getType(), ImportedOperation.TARGET_TYPE_ANNOTATION);

        // 添加注解参数的类型信息
        if (this.defaultValue != null) {
            this.defaultValue.fillImportOperations(context);
        }

        if (this.parameterMap != null && this.parameterMap.size() > 0) {
            for (AnnotationParameter parameter : this.parameterMap.values()) {
                parameter.fillImportOperations(context);
            }
        }
    }
}
