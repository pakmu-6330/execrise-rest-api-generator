package cn.dyr.rest.generator.converter;

import cn.dyr.rest.generator.converter.name.KeywordHolder;
import org.junit.Test;

/**
 * 对关键词的读取等信息进行读取
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class KeywordHolderTest {

    @Test
    public void testLoadData() {
        KeywordHolder holder = KeywordHolder.getHolder();
        System.out.println(holder.isKeyword("clazz"));
    }

}