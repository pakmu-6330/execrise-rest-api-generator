package cn.dyr.rest.generator.converter.method;

import cn.dyr.rest.generator.converter.ConvertDataContext;
import cn.dyr.rest.generator.converter.IConverter;
import cn.dyr.rest.generator.java.meta.MethodInfo;

/**
 * 这个接口是定义了产生用于控制器方法的转换器
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IControllerMethodConverter extends IConverter {

    /**
     * 按分页获得资源列表的控制器方法
     *
     * @param entityName 实体的名称
     * @param pageSize   每个页面的大小
     * @return 用于获得分页数据的控制器方法
     */
    MethodInfo getPagedList(String entityName, int pageSize);

    /**
     * 创建一个根据 id 获得资源的控制器方法
     *
     * @param entityName 实体的名称
     * @return 按照唯一标识获得资源的方法
     */
    MethodInfo getById(String entityName);

    /**
     * 创建一个删除资源的控制器方法
     *
     * @param entityName 实体的名称
     * @return 控制器当中删除资源的方法
     */
    MethodInfo delete(String entityName);

    /**
     * 创建一个创建资源的控制器方法
     *
     * @param entityName 实体的名称
     * @return 控制器当中创建资源的方法
     */
    MethodInfo newResource(String entityName);

    /**
     * 创建一个更新资源的控制器方法
     *
     * @param entityName 实体的名称
     * @return 控制器当中创建资源的方法
     */
    MethodInfo updateResource(String entityName);

    /**
     * 添加关联关系相关的控制器查询方法
     *
     * @param entityName          实体名称
     * @param relationshipHandler 关联关系
     * @return 控制器当中获得关联关系对象的方法
     */
    MethodInfo getRelatedResourceById(String entityName,
                                      ConvertDataContext.RelationshipHandler relationshipHandler);

    /**
     * 对于对多关系，添加一个创建关联实体的方法
     *
     * @param entityName          实体名称
     * @param relationshipHandler 关联关系
     * @return 控制器当中用于创建对多关联关系对象的方法
     */
    MethodInfo getRelatedResourcesCreateForHandler(String entityName,
                                                   ConvertDataContext.RelationshipHandler relationshipHandler);

    /**
     * 对于对多关系，添加一个删除关联关系实体的方法
     *
     * @param entityName          实体名称
     * @param relationshipHandler 关联关系
     * @return 控制器当中用于删除对多关联关系对象的方法
     */
    MethodInfo getRelatedResourcesDeleteForHandler(String entityName,
                                                   ConvertDataContext.RelationshipHandler relationshipHandler);

    /**
     * 针对被控方 Controller 创建多对一（主控方为多方）的相应方法
     *
     * @param entityName          实体名称
     * @param relationshipHandler 关联关系数据
     * @return 如果符合创建 Controller 方法的条件，则返回相应的 Controller 方法，否则返回 null
     */
    MethodInfo getRelatedManyToOneCreateForHandled(String entityName,
                                                   ConvertDataContext.RelationshipHandler relationshipHandler);
}
