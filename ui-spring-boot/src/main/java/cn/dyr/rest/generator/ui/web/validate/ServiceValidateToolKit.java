package cn.dyr.rest.generator.ui.web.validate;

import cn.dyr.rest.generator.ui.web.exception.BadParameterError;

/**
 * 这个类当中封装了一些 Service 类常用的验证方法，如果不满足相应的条件，则抛出 BadParameterError 异常
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ServiceValidateToolKit {

    /**
     * 校验某个字符串是否不为空
     *
     * @param value 要进行校验的字段
     * @param desc  如果不满足校验条件，抛出的异常当中的描述信息
     */
    public static void requireStringNotEmpty(String value, String desc) {
        if (value == null || value.trim().length() == 0) {
            throw new BadParameterError(desc);
        }
    }

    /**
     * 检验某个整型值是否位于某个特定的范围内
     *
     * @param minValue 范围下限
     * @param maxValue 范围上线
     * @param value    要进行校验的值
     * @param desc     如果不满足校验条件，抛出的异常当中的描述信息
     */
    public static void requireInRange(int minValue, int maxValue, int value, String desc) {
        if (value < minValue || value > maxValue) {
            throw new BadParameterError(desc);
        }
    }

    /**
     * 校验某个整型是否不等于某个特定的整数值
     *
     * @param unexpected 不希望取得的整型值
     * @param value      要进行校验的整型值
     * @param desc       如果不满足校验条件抛出的异常
     */
    public static void requireNotEqual(int unexpected, int value, String desc) {
        if (unexpected == value) {
            throw new BadParameterError(desc);
        }
    }
}
