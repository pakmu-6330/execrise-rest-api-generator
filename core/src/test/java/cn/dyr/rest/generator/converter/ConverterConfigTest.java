package cn.dyr.rest.generator.converter;

import org.junit.Test;

/**
 * 配置文件相关的测试类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ConverterConfigTest {
    @Test
    public void testParseConfigFile() {
        ConverterConfig converterConfig = ConverterConfig.fromFile(this.getClass().getClassLoader().getResourceAsStream("cn/dyr/rest/generator/config/converter.xml"));

        System.out.println(String.format("name converter: %s", converterConfig.getNameConverterClass()));
        System.out.println(String.format("type converter: %s", converterConfig.getTypeConverterClass()));
        System.out.println(String.format("po package: %s", converterConfig.getPoPackageName()));
    }
}