package cn.dyr.rest.generator.project.database;

/**
 * MySQL 数据库的配置信息
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class MySQLJdbcConfig implements IJdbcConfig {

    private String host;
    private String username;
    private String password;
    private String database;
    private int port;
    private boolean useUtf8;

    public MySQLJdbcConfig() {
        this.port = 3306;
        this.useUtf8 = true;
    }

    @Override
    public String getDriverClassName() {
        return "com.mysql.jdbc.Driver";
    }

    @Override
    public String getJdbcUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append("jdbc:mysql://");
        builder.append(this.host);

        if (this.port != 3306) {
            builder.append(":");
            builder.append(String.valueOf(this.port));
        }

        builder.append("/");
        builder.append(this.database);

        if (this.useUtf8) {
            builder.append("?useUnicode=true&characterEncoding=utf8");
        }

        return builder.toString();
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public MySQLJdbcConfig setUsername(String username) {
        this.username = username;
        return this;
    }

    public MySQLJdbcConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public int getPort() {
        return port;
    }

    public MySQLJdbcConfig setPort(int port) {
        this.port = port;
        return this;
    }

    public boolean isUseUtf8() {
        return useUtf8;
    }

    public MySQLJdbcConfig setUseUtf8(boolean useUtf8) {
        this.useUtf8 = useUtf8;
        return this;
    }

    public String getHost() {
        return host;
    }

    public MySQLJdbcConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public String getDatabase() {
        return database;
    }

    public MySQLJdbcConfig setDatabase(String database) {
        this.database = database;
        return this;
    }
}
