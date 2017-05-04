package cn.dyr.rest.generator.java.meta.parameters.value;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;

/**
 * 表示一个 char 类型的实参对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class CharacterParameterValue implements ParameterValue {

    private char value;

    public CharacterParameterValue(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public CharacterParameterValue setValue(char value) {
        this.value = value;
        return this;
    }

    @Override
    public int getType() {
        return ParameterValue.TYPE_PRIMITIVE_CHAR;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        // 基本数据类型，没有实质性的导入操作
    }
}
