package cn.dyr.rest.generator.converter.name;

import cn.dyr.rest.generator.converter.ConvertDataContext;
import cn.dyr.rest.generator.converter.IConverter;
import cn.dyr.rest.generator.java.meta.ClassInfo;

/**
 * 用于类名或者字段名称的转换
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface INameConverter extends IConverter {

    /**
     * 将实体名称转换成类名
     *
     * @param entityName 实体名称
     * @return 符合语法规范的类名，如果当前的名称转换器不能处理，则返回 null
     */
    String classNameFromEntityName(String entityName);

    /**
     * 将实体名称转换成控制器名称
     *
     * @param entityName 实体名称
     * @return 实体对应控制器的名称
     */
    String controllerNameFromEntityName(String entityName);

    /**
     * 将实体名称转换成 Service 类的名称
     *
     * @param entityName 实体名称
     * @return 对应 Service 类的名称
     */
    String serviceNameFromEntityName(String entityName);

    /**
     * 将实体名转换成 DAO 接口的名称
     *
     * @param entityName 实体名称
     * @return 对应 DAO 接口类的名称
     */
    String daoNameFromEntityName(String entityName);

    /**
     * 将实体名称转换成 HATEOAS 资源类名称
     *
     * @param entityName 实体名称
     * @return 对应的 HATEOAS 资源的类名称
     */
    String hateoasResourceNameFromEntityName(String entityName);

    /**
     * 将实体名称转换成 HATEOAS 资源装配器的类名
     *
     * @param entityName 实体名称
     * @return 对应 HATEOAS 资源装配类的类名
     */
    String hateoasResourceAssemblerNameFromEntityName(String entityName);

    /**
     * 获得某个特定类型字段的默认变量名
     *
     * @param className 类型名
     * @return 这个类型默认的变量名
     */
    String defaultNameOfVariableName(String className);

    /**
     * 将属性名称转换成字段名称
     *
     * @param attributeName 属性名称
     * @return 符合语法规范的字段名称，如果当前的名称转换器不能处理，则返回　null
     */
    String fieldNameFromAttributeName(String attributeName);

    /**
     * 将实体名称转换成表名
     *
     * @param entityName 实体名称
     * @return 表名
     */
    String tableNameFromEntityName(String entityName);

    /**
     * 用在 DAO 当中根据关联一方寻找另外一方标识符获得另外一方的方法的名称
     *
     * @param entityName  实体名称
     * @param idFieldName 实体唯一标识符的字段名称
     * @return 对应的方法名称
     */
    String daoMethodNameFindByAnotherID(String entityName, String idFieldName);

    /**
     * 获得一个跟主控方到被控方反向查询的 DAO 方法名称
     *
     * @param handler 关系主控信息
     * @return 对应的方法名称
     */
    String reversedDAOQueryMethodName(ConvertDataContext.RelationshipHandler handler);

}
