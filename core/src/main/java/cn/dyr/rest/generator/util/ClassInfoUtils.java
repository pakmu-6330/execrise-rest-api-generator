package cn.dyr.rest.generator.util;

import cn.dyr.rest.generator.framework.jpa.JPATypeFactory;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.FieldInfo;
import cn.dyr.rest.generator.java.meta.MethodInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.MethodInfoFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * 类相关的工具类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ClassInfoUtils {

    /**
     * 对这个类当中的每一个字段生成对应的 get set 方法，并添加到这个类当中
     *
     * @param classInfo     要生成 get set 方法的类元信息
     * @param builderSetter 生成的 set 方法是否为 builder 形式的 set 方法
     */
    public static void createBothGetterAndSetter(ClassInfo classInfo, boolean builderSetter) {
        Iterator<FieldInfo> infoIterator = classInfo.iterateFields();

        while (infoIterator.hasNext()) {
            FieldInfo fieldInfo = infoIterator.next();
            MethodInfo getMethod = MethodInfoFactory.getter(fieldInfo);

            MethodInfo setMethod;

            if (builderSetter) {
                setMethod = MethodInfoFactory.builderSetter(fieldInfo, classInfo.getType());
            } else {
                setMethod = MethodInfoFactory.setter(fieldInfo);
            }

            classInfo.addMethod(getMethod);
            classInfo.addMethod(setMethod);
        }
    }

    /**
     * 从实体类元信息当中找到 id 的类型
     *
     * @param entityClass 实体类的元信息
     * @return 如果参数传入的实体类为有效的实体类元信息并且存在有效的 id 字段，则返回这个字段的类型数据；否则返回 null
     */
    public static TypeInfo getEntityIdType(ClassInfo entityClass) {
        Objects.requireNonNull(entityClass, "entity class is null");

        List<FieldInfo> fields = entityClass.findFieldsByAnnotationType(JPATypeFactory.idAnnotationType());
        if (fields == null || fields.size() == 0) {
            throw new IllegalStateException("no id annotations were found");
        }

        if (fields.size() > 1) {
            throw new IllegalStateException("more than one id annotations were found");
        }

        return fields.get(0).getType();
    }

    /**
     * 查找这个类当中 id 字段
     *
     * @param classInfo 要查找 id 字段的类信息
     * @return 这个类当中的 id 字段
     */
    public static FieldInfo findSingleId(ClassInfo classInfo) {
        Objects.requireNonNull(classInfo, "class info is null");

        List<FieldInfo> fieldInfoList = classInfo.findFieldsByAnnotationType(JPATypeFactory.idAnnotationType());
        if (fieldInfoList == null || fieldInfoList.size() == 0) {
            throw new IllegalStateException("no id annotations were found");
        }

        if (fieldInfoList.size() > 1) {
            throw new IllegalStateException("more than one id annotations were found");
        }

        return fieldInfoList.get(0);
    }

}
