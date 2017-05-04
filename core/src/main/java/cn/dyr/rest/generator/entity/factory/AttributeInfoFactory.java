package cn.dyr.rest.generator.entity.factory;

import cn.dyr.rest.generator.entity.AttributeInfo;
import cn.dyr.rest.generator.entity.AttributeType;

/**
 * 用于创建属性的工厂类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class AttributeInfoFactory {

    /**
     * 创建一个 long 类型的唯一标识符
     *
     * @param name 属性的名称
     * @return 这个属性的对象
     */
    public static AttributeInfo createLongId(String name) {
        AttributeInfo retValue = new AttributeInfo();
        retValue.setName(name);
        retValue.setPrimaryIdentifier(true);
        retValue.setMandatory(true);
        retValue.setType(AttributeType.LONG);

        return retValue;
    }

    /**
     * 创建一个固定长度的字符串属性
     *
     * @param name   属性名称
     * @param length 属性长度
     * @return 表示属性的对象
     */
    public static AttributeInfo fixedString(String name, int length) {
        AttributeInfo retValue = new AttributeInfo();

        retValue.setName(name);
        retValue.setType(AttributeType.FIXED_STRING);
        retValue.setLength(length);

        return retValue;
    }

    /**
     * 创建一个使用 ORM 默认长度的字符串属性
     *
     * @param name 属性名称
     * @return 表示属性的对象
     */
    public static AttributeInfo fixedString(String name) {
        return fixedString(name, 0);
    }

    /**
     * 创建一个可变的最大长度字符串属性
     *
     * @param name      名称
     * @param maxLength 最大长度
     * @return 这个属性的对象
     */
    public static AttributeInfo varString(String name, int maxLength) {
        AttributeInfo retValue = new AttributeInfo();

        retValue.setName(name);
        retValue.setType(AttributeType.VAR_STRING);
        retValue.setLength(maxLength);

        return retValue;
    }

    /**
     * 返回一个使用默认长度的可变字符串属性
     *
     * @param name 属性名称
     * @return 这个属性的对象
     */
    public static AttributeInfo varString(String name) {
        return varString(name, 0);
    }

    /**
     * 创建一个 float 类型的属性
     *
     * @param name 属性名称
     * @return 这个属性对象
     */
    public static AttributeInfo floatAttribute(String name) {
        AttributeInfo retValue = new AttributeInfo();

        retValue.setName(name);
        retValue.setType(AttributeType.FLOAT);

        return retValue;
    }

    /**
     * 创建一个日期时间类型的属性
     *
     * @param name 属性名称
     * @return 这个属性对象
     */
    public static AttributeInfo dateTimeAttribute(String name) {
        AttributeInfo retValue = new AttributeInfo();

        retValue.setName(name);
        retValue.setType(AttributeType.DATETIME);

        return retValue;
    }
}
