package cn.dyr.rest.generator.ui.swing.constant;

import java.util.Collections;
import java.util.List;

/**
 * 在界面当中使用得到的常量
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class UIConstant {

    /**
     * 代码生成器支持的数据库类型
     */
    public static final List<String> SUPPORTED_DB_LIST =
            Collections.unmodifiableList(Collections.singletonList("MySQL"));

    /**
     * 属性类型
     */
    public static String[] ATTRIBUTE_TYPES = {
            "byte", "short", "int", "long", "float", "double", "char", "varchar", "boolean", "datetime"
    };

}
