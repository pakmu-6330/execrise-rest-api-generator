package cn.dyr.rest.generator.framework.spring.boot;

/**
 * 这个类中存放了 Spring Boot 的配置文件所用的常量
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SpringBootProperties {

    /**
     * Spring Boot Data 数据库驱动程序
     */
    public static final String DATA_SOURCE_DRIVER_CLASS_NAME = "spring.datasource.driver-class-name";

    /**
     * Spring Boot Data 数据库密码
     */
    public static final String DATA_SOURCE_PASSWORD = "spring.datasource.password";

    /**
     * Spring Boot Data 数据库用户名
     */
    public static final String DATA_SOURCE_USERNAME = "spring.datasource.username";

    /**
     * Spring Boot Data 数据库 JDBC URL
     */
    public static final String DATA_SOURCE_URL = "spring.datasource.url";

    /**
     * Spring Boot Data JPA 是否自动创建 DDL 并执行
     */
    public static final String JPA_HIBERNATE_DDL_AUTO = "spring.jpa.hibernate.ddl-auto";

    /**
     * Spring Boot Data JPA 是否自动执行 DDL 并执行的值：update
     */
    public static final String JPA_HIBERNATE_DDL_AUTO_VALUE_UPDATE = "update";

    /**
     * Spring Boot Data JPA 是否启用 SQL 语句的显示
     */
    public static final String JPA_SHOW_SQL = "spring.jpa.show-sql";

    /**
     * Spring Boot 指定的端口号
     */
    public static final String SERVER_PORT = "server.port";
}
