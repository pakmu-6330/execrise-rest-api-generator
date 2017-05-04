package cn.dyr.rest.generator.java.meta.parameters.value;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;

/**
 * 表示一个 int 型的实参对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class IntegerParameterValue implements ParameterValue {

    private int value;

    public IntegerParameterValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public IntegerParameterValue setValue(int value) {
        this.value = value;
        return this;
    }

    @Override
    public int getType() {
        return ParameterValue.TYPE_PRIMITIVE_INT;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        // 基本数据类型，没有实质性的导入操作
    }
}
