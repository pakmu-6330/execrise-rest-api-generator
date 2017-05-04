package cn.dyr.rest.generator.java.meta.parameters.value;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;

/**
 * 表示该实参值是一个 null
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class NullParameterValue implements ParameterValue {
    @Override
    public int getType() {
        return ParameterValue.TYPE_OBJECT_NULL;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        // 基本数据类型，没有实质性的导入操作
    }
}
