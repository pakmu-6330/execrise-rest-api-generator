package cn.dyr.rest.generator.java.meta.parameters.value;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;

/**
 * 表示一个函数调用时候的实参
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class LongParameterValue implements ParameterValue {

    private long value;

    public LongParameterValue(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public LongParameterValue setValue(long value) {
        this.value = value;
        return this;
    }

    @Override
    public int getType() {
        return ParameterValue.TYPE_PRIMITIVE_LONG;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        // 基本数据类型，没有实质性的导入操作
    }
}
