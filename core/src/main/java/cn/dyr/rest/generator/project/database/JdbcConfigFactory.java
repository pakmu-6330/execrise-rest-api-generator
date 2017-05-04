package cn.dyr.rest.generator.project.database;

/**
 * 用于创建数据库配置信息的工厂类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class JdbcConfigFactory {

    /**
     * 创建一个 MYSQL 的数据库配置信息
     *
     * @param host     数据库服务器主机
     * @param db       数据库名称
     * @param username 用户名
     * @param password 密码
     * @return 对应的配置信息
     */
    public static IJdbcConfig mySql(String host, String db, String username, String password) {
        MySQLJdbcConfig config = new MySQLJdbcConfig();

        config.setDatabase(db)
                .setHost(host)
                .setUsername(username)
                .setPassword(password);

        return config;
    }

}
