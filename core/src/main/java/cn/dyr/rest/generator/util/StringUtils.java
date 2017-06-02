package cn.dyr.rest.generator.util;

/**
 * 字符串相关的工具类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class StringUtils {

    /**
     * 判断这个字符串是否为空（包括 null 值或者只含有空格的字符串）
     *
     * @param str 要检查的字符串
     * @return 如果这个字符串是 null 或者只包含有空字符，则返回 true；否则返回 false
     */
    public static boolean isStringEmpty(String str) {
        return (str == null || "".equals(str.trim()));
    }

    /**
     * 将第一个字母变成大写字母的字符串
     *
     * @param str 字符串
     * @return 第一个字母变成大写的字符串
     */
    public static String upperFirstLatter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 将第一个字母变成小写字母的字符串
     *
     * @param str 字符串
     * @return 第一个字母变成小写的字符串
     */
    public static String lowerFirstLatter(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 给定的字符串能够被转换为整数
     *
     * @param str 字符串
     * @return 一个布尔值，表示这个字符串能否被转换为整数
     */
    public static boolean canBeConvertedToInteger(String str) {
        try {
            int tryConvert = Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
