package cn.dyr.rest.generator.converter.field;

import cn.dyr.rest.generator.converter.IConverter;
import cn.dyr.rest.generator.entity.AttributeInfo;
import cn.dyr.rest.generator.entity.EntityRelationship;
import cn.dyr.rest.generator.java.meta.FieldInfo;

/**
 * 属性到字段转换器的接口
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IFieldConverter extends IConverter {

    /**
     * 将实体属性转换成字段信息
     *
     * @param attributeInfo 实体属性信息
     * @return 这个属性对应的字段对象
     */
    FieldInfo fromAttributeInfo(AttributeInfo attributeInfo);

    /**
     * 将实体之间的关联关系转换成字段信息
     *
     * @param relationship 要进行转换的关联关系
     * @param forEndA      是否生成用于端 A 的字段
     * @return 这个关联关系在指定端的实现字段
     */
    FieldInfo fromRelationship(EntityRelationship relationship, boolean forEndA);

    /**
     * 这个方法用于定义在所有的的字段转换完成以后进行的一些处理
     */
    void postRelationshipProcess();
}
