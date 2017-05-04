package cn.dyr.rest.generator.java.meta;

import java.util.List;

/**
 * 这个类用于表示一种类型
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface TypeInfo {

    /**
     * 获得这种类型的名称<br>
     * 对于基本数据类型及其数组，返回的值基本数据类型的名称<br>
     * 对于引用类型及其数组，这里返回的是类名<br>
     * 对于泛型类型占位符，这里返回的占位符所使用的名称<br>
     *
     * @return 类型的名称
     */
    String getName();

    /**
     * 获得类型的全名，仅适用于引用类型的类型对象
     *
     * @return 类型的全名
     */
    String getFullName();

    /**
     * 类型的包名，仅适用于引用类型的类型对象
     *
     * @return 这个类型的包名
     */
    String getPackageName();

    /**
     * 这个类型是否为占位符类型
     *
     * @return 这个类型的
     */
    boolean isPlaceHolder();

    /**
     * 这个类型是否为基本数据类型
     *
     * @return 表示这个类型是否为基本数据类型的布尔值s
     */
    boolean isPrimitiveType();

    /**
     * 表示这个类型是否为数组类型
     *
     * @return 表示这个类型是否为数组类型的布尔值
     */
    boolean isArray();

    /**
     * 如果这个类型含有泛型类型数据，这个列表储存的就是泛型的类型数据
     *
     * @return 这个类型的泛型数据
     */
    List<TypeInfo> getParameterizeType();
}
