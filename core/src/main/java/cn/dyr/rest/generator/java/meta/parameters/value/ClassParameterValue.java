package cn.dyr.rest.generator.java.meta.parameters.value;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.generator.analysis.ImportedOperation;
import cn.dyr.rest.generator.java.meta.TypeInfo;

/**
 * 表示一个类对应的 class 对象实参对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ClassParameterValue implements ParameterValue {

    private TypeInfo value;

    public ClassParameterValue(TypeInfo value) {
        this.value = value;
    }

    public TypeInfo getValue() {
        return value;
    }

    public ClassParameterValue setValue(TypeInfo value) {
        this.value = value;
        return this;
    }

    @Override
    public int getType() {
        return ParameterValue.TYPE_PRIMITIVE_CLASS;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        context.addImportOperation(this.value, ImportedOperation.TARGET_TYPE_METHOD_PARAMETER_TYPE);
    }
}
