package cn.dyr.rest.generator.converter;

/**
 * 表示要注入的参数类型
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public enum ConverterInjectType {

    /**
     * 表示名称转换器
     */
    NAME,
    /**
     * 表示类型转换器
     */
    TYPE,
    /**
     * 表示字段转换器
     */
    FIELD,
    /**
     * 表示的是 Service 类的指令的转换器
     */
    SERVICE_INSTRUCTION,
    /**
     * 表示的是实体类元信息的转换器
     */
    ENTITY,
    /**
     * 表示的是 DAO 类元信息的转换器
     */
    DAO,
    /**
     * 表示 Service 类转换器
     */
    SERVICE,
    /**
     * 表示 Controller 类转换器
     */
    CONTROLLER,
    /**
     * 表示 Spring HATEOAS 资源类
     */
    RESOURCE,
    /**
     * 表示 Spring HATEOAS 资源装配器转换器
     */
    RESOURCE_ASSEMBLER,
    /**
     * 用于控制器的
     */
    CONTROLLER_METHOD,
    /**
     * 用于控制整体逻辑的转换器
     */
    CONVERT_SCHEMA,

}
