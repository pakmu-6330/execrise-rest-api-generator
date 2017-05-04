package cn.dyr.rest.generator.java.meta.parameters.value;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;

/**
 * 表示一个 byte 类型的实参对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ByteParameterValue implements ParameterValue {

    private byte value;

    public ByteParameterValue(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public ByteParameterValue setValue(byte value) {
        this.value = value;
        return this;
    }

    @Override
    public int getType() {
        return ParameterValue.TYPE_PRIMITIVE_BYTE;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        // 基本数据类型，没有实质性的导入操作
    }
}
