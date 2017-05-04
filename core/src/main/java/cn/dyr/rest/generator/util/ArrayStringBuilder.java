package cn.dyr.rest.generator.util;

import cn.dyr.rest.generator.java.generator.elements.ElementsConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类用创建一个用于注解或其他地方表示数组的信息
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ArrayStringBuilder {

    private List<String> arrays;

    public ArrayStringBuilder() {
        this.arrays = new ArrayList<>();
    }

    /**
     * 往这个数组当中添加一个布尔值
     *
     * @param value 要添加到构建器的布尔值
     * @return 这个构建器本身
     */
    public ArrayStringBuilder addBoolean(boolean value) {
        this.arrays.add(value ? "true" : "false");
        return this;
    }

    /**
     * 往这个数组当中添加一个 byte 类型
     *
     * @param value 要添加到构建器的 byte 值
     * @return 这个构建器本身
     */
    public ArrayStringBuilder addByte(byte value) {
        this.arrays.add(value + "");
        return this;
    }

    @Override
    public String toString() {
        if (this.arrays == null) {
            return ElementsConstant.EMPTY_ARRAY;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("{");

        int totalSize = this.arrays.size();
        for (int i = 0; i < totalSize; i++) {
            if (i != 0) {
                builder.append(", ");
            }

            builder.append(this.arrays.get(i));
        }

        builder.append("}");

        return builder.toString();
    }

    /**
     * 往这个数组当中添加一个 short 类型
     *
     * @param s 要添加到数组当中的数据
     * @return 这个构建器本身
     */
    public ArrayStringBuilder addShort(short s) {
        this.arrays.add(s + "");
        return this;
    }

    /**
     * 往这个数组当中添加一个 int 类型
     *
     * @param i 要添加到数组当中的数据
     * @return 这个构建器本身
     */
    public ArrayStringBuilder addInt(int i) {
        this.arrays.add(i + "");
        return this;
    }

    /**
     * 往这个数组当中添加一个 long 类型
     *
     * @param l 要添加到数组当中的数据
     * @return 这个构建器本身
     */
    public ArrayStringBuilder addLong(long l) {
        this.arrays.add(l + "L");
        return this;
    }

    /**
     * 往这个数组当中添加一个 float 类型
     *
     * @param f 要添加数组当中的数据
     * @return 这个构建器本身
     */
    public ArrayStringBuilder addFloat(float f) {
        this.arrays.add(f + "f");
        return this;
    }

    /**
     * 往这个数组当中添加一个 double 类型
     *
     * @param d 要添加到数组当中的内容
     * @return 这个构建器本身
     */
    public ArrayStringBuilder addDouble(double d) {
        this.arrays.add(d + "");
        return this;
    }

    /**
     * 往这个数组当中添加一个 char 类型
     *
     * @param c 要添加到数组当中的内容
     * @return 这个构建器本身
     */
    public ArrayStringBuilder addCharacter(char c) {
        this.arrays.add(String.format("'%c'", c));
        return this;
    }

    /**
     * 往这个数组当中添加一个 String 类型
     *
     * @param s 要添加到数组当中的内容
     * @return 这个构建器本身
     */
    public ArrayStringBuilder addString(String s) {
        this.arrays.add(String.format("\"%s\"", s));
        return this;
    }

    /**
     * 往这个数组当中添加一个不经过任何处理的字符串
     *
     * @param rawValue 要添加到数组当中的内容
     * @return 这个构建器本身
     */
    public ArrayStringBuilder addRawString(String rawValue) {
        this.arrays.add(rawValue);
        return this;
    }
}
