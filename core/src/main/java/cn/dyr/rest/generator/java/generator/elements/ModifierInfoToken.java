package cn.dyr.rest.generator.java.generator.elements;

import cn.dyr.rest.generator.java.meta.ModifierInfo;

/**
 * 表示一个修饰符元素
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ModifierInfoToken implements IToken {

    private ModifierInfo modifierInfo;

    public ModifierInfoToken(ModifierInfo modifierInfo) {
        this.modifierInfo = modifierInfo;
    }

    /**
     * 获得这个修饰符元素对应的修饰符信息
     *
     * @return 这个元素对应的修饰符信息类
     */
    public ModifierInfo getModifierInfo() {
        return modifierInfo;
    }

    /**
     * 设置这个修饰符元素对应的修饰符信息
     *
     * @param modifierInfo 要设置的修饰符信息
     * @return 这个修饰符元素本身
     */
    public ModifierInfoToken setModifierInfo(ModifierInfo modifierInfo) {
        this.modifierInfo = modifierInfo;
        return this;
    }

    /**
     * 生成通用的修饰字符串（同时适用于类、字段、方法对象的修饰字符串）
     *
     * @return 这个修饰符对应的字符串
     */
    private String generateCommonModifierString() {
        String retValue = "";

        if (this.modifierInfo.isPrivate()) {
            retValue = "private ";
        } else if (this.modifierInfo.isPublic()) {
            retValue = "public ";
        } else if (this.modifierInfo.isProtected()) {
            retValue = "protected ";
        }

        if (this.modifierInfo.isStatic()) {
            retValue = retValue + "static ";
        }

        if (this.modifierInfo.isFinal()) {
            retValue = retValue + "final ";
        }

        return retValue;
    }

    /**
     * 生成用于类的修饰字符串
     *
     * @return 这个修饰符对应的字符串
     */
    public String generateClassModifierString() {
        String retValue = generateCommonModifierString();

        if (this.modifierInfo.isInterface()) {
            retValue = retValue + "interface ";
        } else {
            if (this.modifierInfo.isAbstract()) {
                retValue = retValue + "abstract ";
            }

            retValue = retValue + "class ";
        }

        return retValue.trim();
    }

    /**
     * 用于生成用于字段声明语句的修饰符串
     *
     * @return 这个修饰符对应的字符串
     */
    public String generateFieldModifierString() {
        String retValue = generateCommonModifierString();

        return retValue.trim();
    }

    /**
     * 用于生成方法修饰符字符串
     *
     * @return 这个修饰符对应的字符串
     */
    public String generateMethodModifierString() {
        return generateCommonModifierString().trim();
    }
}
