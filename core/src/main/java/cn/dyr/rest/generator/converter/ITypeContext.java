package cn.dyr.rest.generator.converter;

import cn.dyr.rest.generator.java.meta.TypeInfo;

/**
 * 用于给底层的字段转换等转换器提供本项目实体类的信息
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface ITypeContext {

    /**
     * 根据本项目的实体名称获得相应的类型信息
     *
     * @param entityName 实体名称
     * @return 如果实体名称对应的类型存在，则返回相应的 TypeInfo 接口；否则返回 null
     */
    TypeInfo findTypeInfoByEntityName(String entityName);

}
