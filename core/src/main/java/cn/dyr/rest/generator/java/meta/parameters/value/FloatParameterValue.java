package cn.dyr.rest.generator.java.meta.parameters.value;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;

/**
 * 表示一个 short 类型的实参对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class FloatParameterValue implements ParameterValue {

    private float value;

    public FloatParameterValue(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public FloatParameterValue setValue(float value) {
        this.value = value;
        return this;
    }

    @Override
    public int getType() {
        return ParameterValue.TYPE_PRIMITIVE_FLOAT;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        // 基本数据类型，没有实质性的导入操作
    }
}
