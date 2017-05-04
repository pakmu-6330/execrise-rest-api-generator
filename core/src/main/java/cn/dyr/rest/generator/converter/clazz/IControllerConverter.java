package cn.dyr.rest.generator.converter.clazz;

import cn.dyr.rest.generator.converter.IConverter;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.java.meta.ClassInfo;

/**
 * 表示这个是 Controller 类的转换器
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IControllerConverter extends IConverter {

    /**
     * 生成控制器基本信息
     *
     * @param entityInfo  实体信息
     * @return 这个类的类元信息
     */
    ClassInfo basicInfo(EntityInfo entityInfo);

    /**
     * 生成 Controller 类当中的方法以及方法当中的指令
     *
     * @param entityInfo  实体信息
     * @return 这个类的类元信息
     */
    ClassInfo convertMethod(EntityInfo entityInfo);
}
