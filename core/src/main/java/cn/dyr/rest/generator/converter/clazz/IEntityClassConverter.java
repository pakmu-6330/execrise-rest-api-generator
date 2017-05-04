package cn.dyr.rest.generator.converter.clazz;

import cn.dyr.rest.generator.converter.IConverter;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.entity.EntityRelationship;
import cn.dyr.rest.generator.java.meta.ClassInfo;

/**
 * 这个接口定义了实体信息到实体类转换器的功能
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IEntityClassConverter extends IConverter {

    /**
     * 生成不包括关联关系的实体类元信息
     *
     * @param entityInfo 实体信息
     * @return 这个实体对应的 PO 类元信息
     */
    ClassInfo basicInfo(EntityInfo entityInfo);

    /**
     * 处理实体之间的关联关系
     *
     * @param relationship 实体关联关系信息
     */
    void processRelationship(EntityRelationship relationship);

}
