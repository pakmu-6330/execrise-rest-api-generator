package cn.dyr.rest.generator.converter.schema;

import cn.dyr.rest.generator.converter.IConverter;

/**
 * 表示一种转换方式的抽象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IConvertSchema extends IConverter {

    /**
     * 收集这个转换方式中所需的转换器类
     *
     * @param schemaConverterList 转换器类
     */
    void getConverterList(SchemaConverterList schemaConverterList);

    /**
     * 对实体信息和关系信息进行转换
     *
     * @return 如果转换成功，返回 true；否则返回 false
     */
    boolean generate();

}
