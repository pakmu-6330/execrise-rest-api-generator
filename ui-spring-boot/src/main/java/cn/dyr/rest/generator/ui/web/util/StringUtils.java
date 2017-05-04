package cn.dyr.rest.generator.ui.web.util;

/**
 * 字符串相关的工具类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class StringUtils {

    /**
     * 判断字符串是否为空串
     *
     * @param value 要进行检查的字符串
     * @return 如果这个字符串是空串，则返回 true；否则返回 false
     */
    public static boolean isStringEmpty(String value) {
        return (value == null || "".equals(value.trim()));
    }

    /**
     * 确保字符串不为空
     *
     * @param value 要进行处理的字符串
     * @return 如果字符串为 null，则返回一个空字符串；否则返回字符串本身
     */
    public static String stringOrEmpty(String value) {
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }

}
