package cn.dyr.rest.generator.converter.clazz;

import cn.dyr.rest.generator.converter.IConverter;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.java.meta.ClassInfo;

/**
 * 这个接口用于定义资源类转换器的接口
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IResourceClassConverter extends IConverter {

    /**
     * 根据实体信息创建对应的资源装配类
     *
     * @param entityInfo 实体信息
     * @return 这个实体信息对应的资源装配类
     */
    ClassInfo fromEntity(EntityInfo entityInfo);

}
