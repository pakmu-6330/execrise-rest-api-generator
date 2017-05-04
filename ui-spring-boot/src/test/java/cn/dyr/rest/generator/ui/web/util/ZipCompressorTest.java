package cn.dyr.rest.generator.ui.web.util;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * 压缩测试
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ZipCompressorTest {

    @Test
    public void test() {
        File targetFile = new File("F:\\output", "db.zip");
        ZipCompressor zipCompressor = new ZipCompressor(targetFile);
        zipCompressor.compressExe("F:\\IPData");
    }

}