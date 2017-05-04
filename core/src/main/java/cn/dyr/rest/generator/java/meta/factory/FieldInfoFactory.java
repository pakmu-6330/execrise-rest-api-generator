package cn.dyr.rest.generator.java.meta.factory;

import cn.dyr.rest.generator.java.meta.FieldInfo;
import cn.dyr.rest.generator.java.meta.ModifierInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;

/**
 * 一些字段相关的常用方法
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class FieldInfoFactory {

    /**
     * 创建一个字段的深拷贝
     *
     * @param fieldInfo 要进行深拷贝的字段
     * @return 这个字段的深拷贝
     */
    public static FieldInfo cloneFieldWithoutAnnotation(FieldInfo fieldInfo) {
        ModifierInfo modifierInfo = ModifierInfoFactory.cloneModifier(fieldInfo.getModifier());
        TypeInfo typeInfo = TypeInfoFactory.cloneTypeInfo(fieldInfo.getType());

        return new FieldInfo().setName(fieldInfo.getName()).setType(typeInfo).setModifier(modifierInfo);
    }

}
