package cn.dyr.rest.generator.ui.web.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 流水号等业务相关编号的生成器
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class IdGenerator {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    /**
     * 产生一个工程流水号
     *
     * @param userId 创建工程的用户 ID
     * @return 工程的工程号
     */
    public static String projectId(long userId) {
        return String.format("P%05d%s", userId, formatter.format(new Date()));
    }
}
