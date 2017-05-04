package cn.dyr.rest.generator.converter.clazz;

import cn.dyr.rest.generator.converter.IConverter;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.java.meta.ClassInfo;

/**
 * 这个接口定义了 DAO 转换器的接口
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IDAOConverter extends IConverter {

    /**
     * 根据实体信息创建相应的 DAO 接口信息
     *
     * @param entityInfo 实体信息
     * @return 实体信息对应的 DAO 接口的类元信息
     */
    ClassInfo fromEntity(EntityInfo entityInfo);

}
