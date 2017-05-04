package cn.dyr.rest.generator.java.meta.parameters.value;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;

/**
 * 表示一个字符串值
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class StringParameterValue implements ParameterValue {

    private String value;

    public StringParameterValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public StringParameterValue setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public int getType() {
        return ParameterValue.TYPE_OBJECT_STRING;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        // 基本数据类型，没有实质性的导入操作
    }
}
