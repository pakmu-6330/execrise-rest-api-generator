package cn.dyr.experiment;

import org.junit.Test;

import java.util.Map;
import java.util.Set;

/**
 * 用于进行一些小规模的试验
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class EnvTest {

    @Test
    public void testGet() {
        Map<String, String> env = System.getenv();
        Set<Map.Entry<String, String>> entries = env.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println(String.format("%s: %s", entry.getKey(), entry.getValue()));
        }
    }

    @Test
    public void testGetPath() {
        String value = System.getenv("Path");
        System.out.println(value);
    }

}
