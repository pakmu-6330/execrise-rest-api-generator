package cn.dyr.rest.generator.java.meta.parameters.value;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;

/**
 * 表示一个 short 类型的实参对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ShortParameterValue implements ParameterValue {

    private short value;

    public ShortParameterValue(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    public ShortParameterValue setValue(short value) {
        this.value = value;
        return this;
    }

    @Override
    public int getType() {
        return ParameterValue.TYPE_PRIMITIVE_SHORT;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        // 基本数据类型，没有实质性的导入操作
    }
}
