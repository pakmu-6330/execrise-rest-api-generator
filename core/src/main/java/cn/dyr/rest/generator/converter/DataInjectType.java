package cn.dyr.rest.generator.converter;

/**
 * 表示数据注入的类型
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public enum DataInjectType {

    /**
     * 转换过程存放临时数据的上下文对象
     */
    DATA_CONTEXT,
    /**
     * 配置信息对象
     */
    CONFIG,
    /**
     * 实体列表
     */
    ENTITY_LIST,
    /**
     * 实体关联关系列表
     */
    RELATIONSHIP_LIST,
    /**
     * 存放实体包的包名
     */
    ENTITY_PACKAGE_NAME,
    /**
     * 存放 Service 接口包的包名
     */
    SERVICE_INTERFACE_PACKAGE_NAME,
    /**
     * 存放 Service 实现类的包名
     */
    SERVICE_IMPL_PACKAGE_NAME,
    /**
     * 存放 DAO 类包的包名
     */
    DAO_PACKAGE_NAME,
    /**
     * 存放 Controller 类包的包名
     */
    CONTROLLER_PACKAGE_NAME,
    /**
     * 存放 HATEOAS 资源类包的包名
     */
    HATEOAS_RESOURCE_PACKAGE_NAME,
    /**
     * 存放 HATEOAS 资源装配器类包的包名
     */
    HATEOAS_RESOURCE_ASSEMBLER_PACKAGE_NAME,
    /**
     * 存放通用类包的包名
     */
    COMMON_PACKAGE_NAME,
    /**
     * 存放异常类包的包名
     */
    EXCEPTION_PACKAGE_NAME

}
