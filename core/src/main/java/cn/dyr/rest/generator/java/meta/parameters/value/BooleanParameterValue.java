package cn.dyr.rest.generator.java.meta.parameters.value;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;

/**
 * 表示一个 boolean 类型的实参对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class BooleanParameterValue implements ParameterValue {

    private boolean value;

    public BooleanParameterValue(boolean value) {
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }

    public BooleanParameterValue setValue(boolean value) {
        this.value = value;
        return this;
    }

    @Override
    public int getType() {
        return ParameterValue.TYPE_PRIMITIVE_BOOLEAN;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        // 基本数据类型，没有实质性的导入操作
    }
}
