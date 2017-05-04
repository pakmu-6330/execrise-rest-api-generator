package cn.dyr.rest.generator.converter.type;

import cn.dyr.rest.generator.converter.IConverter;
import cn.dyr.rest.generator.entity.AttributeInfo;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;

/**
 * 用于封装从属性到字段类型的逻辑
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface ITypeConverter extends IConverter {

    /**
     * 获得与这个属性相适应的类型
     *
     * @param attributeInfo 属性
     * @return 类型
     */
    TypeInfo convertTypeInfo(AttributeInfo attributeInfo);

    /**
     * 将本项目当中的实体对象转换成相应的 TypeInfo 类型
     *
     * @param entityInfo 本项目的实体信息
     * @return 如果实体信息有效，则返回相应的类型对象；否则返回 null
     */
    TypeInfo fromEntity(EntityInfo entityInfo);
}
