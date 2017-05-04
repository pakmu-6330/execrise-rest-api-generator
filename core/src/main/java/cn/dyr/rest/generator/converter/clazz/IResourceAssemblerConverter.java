package cn.dyr.rest.generator.converter.clazz;

import cn.dyr.rest.generator.converter.IConverter;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.java.meta.ClassInfo;

/**
 * HATEOAS 资源装配类生成器的接口
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IResourceAssemblerConverter extends IConverter {

    /**
     * 根据指定的实体类信息生成相应的 Spring HATEOAS 资源装配类
     *
     * @param entityInfo  实体名称
     * @return 这个实体对应的资源装配类
     */
    ClassInfo toResourceAssembler(EntityInfo entityInfo);

}
