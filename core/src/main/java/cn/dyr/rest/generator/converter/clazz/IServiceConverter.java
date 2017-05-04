package cn.dyr.rest.generator.converter.clazz;

import cn.dyr.rest.generator.converter.IConverter;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.java.meta.ClassInfo;

/**
 * 这个接口定义了实体到 Service 类之间转换所需的逻辑
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IServiceConverter extends IConverter {

    /**
     * 从实体信息转换成相应的 Service 类元信息
     *
     * @param entityInfo 实体信息
     * @return Service 类元信息
     */
    ClassInfo fromEntity(EntityInfo entityInfo);

}
