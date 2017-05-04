package cn.dyr.rest.generator.framework.jdk;

import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;

/**
 * 用于创建集合类当中一些常用的表达式
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class CollectionsValueExpressionFactory {

    /**
     * 调用 Collections 的 emptyList 方法创建一个空白的列表
     *
     * @return 表示空白列表的值
     */
    public static IValueExpression emptyList() {
        TypeInfo typeInfo = TypeInfoFactory.fromClass(CollectionsConstant.COLLECTION_UTILS_CLASS);
        return ValueExpressionFactory.classForStatic(typeInfo)
                .invokeMethod(CollectionsConstant.COLLECTION_UTILS_METHOD_EMPTY_LIST);
    }

}
