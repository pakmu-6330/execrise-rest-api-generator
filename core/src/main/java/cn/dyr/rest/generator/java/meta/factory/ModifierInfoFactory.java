package cn.dyr.rest.generator.java.meta.factory;

import cn.dyr.rest.generator.java.meta.ModifierInfo;

import java.lang.reflect.Field;

/**
 * 一些修饰符常用的方法
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ModifierInfoFactory {

    /**
     * 获得一个修饰符的深拷贝
     * @param modifierInfo 要进行深拷贝的修饰符对象
     * @return 这个修饰符的深拷贝
     */
    public static ModifierInfo cloneModifier(ModifierInfo modifierInfo) {
        ModifierInfo clone = new ModifierInfo();

        try {
            Field modifierField = modifierInfo.getClass().getDeclaredField("modifier");
            modifierField.setAccessible(true);

            int rawModifierValue = (int) modifierField.get(modifierInfo);
            modifierField.set(clone, rawModifierValue);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {

        }

        return clone;
    }

}
