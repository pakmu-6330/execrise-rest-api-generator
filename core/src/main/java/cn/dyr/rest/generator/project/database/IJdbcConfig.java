package cn.dyr.rest.generator.project.database;

/**
 * 屏蔽数据库差异，对上层暴露统一的数据库配置生成相关的接口
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IJdbcConfig {

    /**
     * 获得 jdbc 驱动名称
     *
     * @return jdbc 驱动名称
     */
    String getDriverClassName();

    /**
     * 获得 jdbc url
     *
     * @return jdbc url
     */
    String getJdbcUrl();

    /**
     * 获得数据库用户名
     * @return 数据库登录用户名
     */
    String getUsername();

    /**
     * 获得数据库密码
     * @return 数据库登录密码
     */
    String getPassword();
}
