package cn.dyr.rest.generator.framework.spring.boot;

/**
 * 这个类定义了 Spring Boot 的版本号，用于 Maven 配置信息的创建
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public enum SpringBootVersion {

    /**
     * 1.5.1.RELEASE 版本
     */
    VER_1_5_1("1.5.1.RELEASE"),

    /**
     * 1.5.2.RELEASE 版本
     */
    VER_1_5_2("1.5.2.RELEASE"),

    /**
     * 1.5.3.RELEASE 版本
     */
    VER_1_5_3("1.5.3.RELEASE"),

    /**
     * 1.5.4.RELEASE 版本
     */
    VER_1_5_4("1.5.4.RELEASE"),

    /**
     * 1.5.5.RELEASE 版本
     */
    VER_1_5_5("1.5.5.RELEASE"),

    /**
     * 1.5.6.RELEASE 版本
     */
    VER_1_5_6("1.5.6.RELEASE"),

    /**
     * 1.5.7.RELEASE 版本
     */
    VER_1_5_7("1.5.7.RELEASE"),

    /**
     * 1.5.8.RELEASE 版本
     */
    VER_1_5_8("1.5.8.RELEASE"),

    /**
     * 1.5.9.RELEASE 版本
     */
    VER_1_5_9("1.5.9.RELEASE"),

    /**
     * 1.5.10.RELEASE 版本
     */
    VER_1_5_10("1.5.10.RELEASE"),

    /**
     * 1.5.11.RELEASE 版本
     */
    VER_1_5_11("1.5.11.RELEASE"),

    /**
     * 1.5.12.RELEASE 版本
     */
    VER_1_5_12("1.5.12.RELEASE"),

    /**
     * 2.0.1.RELEASE 版本
     */
    VER_2_0_1("2.0.1.RELEASE");

    String version;
    int majorVersion;

    SpringBootVersion(String version) {
        this.version = version;
        this.majorVersion = Integer.parseInt(version.substring(0, 1));
    }

    @Override
    public java.lang.String toString() {
        return this.version;
    }

    public int getMajorVersion() {
        return this.majorVersion;
    }
}
