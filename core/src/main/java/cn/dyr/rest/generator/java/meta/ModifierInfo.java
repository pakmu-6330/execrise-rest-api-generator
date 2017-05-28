package cn.dyr.rest.generator.java.meta;

/**
 * 表示一个修饰符
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ModifierInfo {

    public static final int PUBLIC = 0x00000001;
    public static final int PRIVATE = 0x00000002;
    public static final int PROTECTED = 0x00000004;

    public static final int STATIC = 0x00000008;
    public static final int FINAL = 0x00000010;
    public static final int SYNCHRONIZED = 0x00000020;
    public static final int VOLATILE = 0x00000040;
    public static final int TRANSIENT = 0x00000080;

    public static final int STRICT = 0x0000800;
    public static final int ABSTRACT = 0x00000400;
    public static final int INTERFACE = 0x00000200;
    public static final int NATIVE = 0x00000100;

    private int modifier;

    /**
     * 将访问修饰符设置为 public
     *
     * @return 这个修饰符信息本身
     */
    public ModifierInfo setPublic() {
        modifier = (modifier & 0xFFFFFFF8) | PUBLIC;

        return this;
    }

    /**
     * 是否为 public
     *
     * @return 如果修饰符的访问修饰符为 public，则返回 true；否则返回 false
     */
    public boolean isPublic() {
        return ((modifier & PUBLIC) == PUBLIC);
    }

    /**
     * 将访问修饰符设置为 private
     *
     * @return 这个修饰符信息本身
     */
    public ModifierInfo setPrivate() {
        modifier = (modifier & 0xFFFFFFF8) | PRIVATE;
        return this;
    }

    /**
     * 是否为 private
     *
     * @return 如果修饰符的访问修饰符为 private，则返回 true；否则返回 false
     */
    public boolean isPrivate() {
        return ((modifier & PRIVATE) == PRIVATE);
    }

    /**
     * 将访问修饰符设置为 protected
     */
    public void setProtected() {
        modifier = (modifier & 0xFFFFFFF8) | PROTECTED;
    }

    /**
     * 是否为 protected
     *
     * @return 如果修饰符的访问修饰符为 protected，则返回 true；否则返回 false
     */
    public boolean isProtected() {
        return ((modifier & PROTECTED) == PROTECTED);
    }

    /**
     * 将访问修饰符设置为缺省
     */
    public void setDefault() {
        modifier = modifier & 0xFFFFFFF8;
    }

    /**
     * 是否为缺省
     *
     * @return 如果这个访问修饰符是缺省，则返回 true；否则返回 false
     */
    public boolean isDefault() {
        return ((modifier & 0x00000007) == 0x00000000);
    }

    /**
     * 设置这个修饰符的静态位
     *
     * @param isStatic 这个修饰符是否为静态
     */
    public void setStatic(boolean isStatic) {
        if (isStatic) {
            modifier = modifier | STATIC;
        } else {
            modifier = modifier & (~STATIC);
        }
    }

    /**
     * 判断修饰符是否含有静态位
     *
     * @return 如果修饰符是静态的，则返回 true；否则返回 false
     */
    public boolean isStatic() {
        return ((modifier & STATIC) == STATIC);
    }

    /**
     * 设置这个修饰符的 final 位
     *
     * @param isFinal final 修饰是否有效
     */
    public void setFinal(boolean isFinal) {
        if (isFinal) {
            modifier = modifier | FINAL;
        } else {
            modifier = modifier & (~FINAL);
        }
    }

    /**
     * 判断修饰符的 final 位是否有效
     *
     * @return 如果修饰符的 final 位有效，则返回 true；否则返回 false
     */
    public boolean isFinal() {
        return ((modifier & FINAL) == FINAL);
    }

    /**
     * 设置这个修饰符的 abstract 位
     *
     * @param isAbstract abstract 位是否有效
     */
    public void setAbstract(boolean isAbstract) {
        if (isAbstract) {
            modifier = modifier | ABSTRACT;
        } else {
            modifier = modifier & (~ABSTRACT);
        }
    }

    /**
     * 判断修饰符的 abstract 位是否有效
     *
     * @return 如果修饰符的 abstract 位有效，则返回 true；否则返回 false
     */
    public boolean isAbstract() {
        return ((modifier & ABSTRACT) == ABSTRACT);
    }

    /**
     * 设置这个修饰符的 interface 位
     *
     * @param isInterface interface 位是否有效
     */
    public void setInterface(boolean isInterface) {
        if (isInterface) {
            modifier = modifier | INTERFACE;
        } else {
            modifier = modifier & (~INTERFACE);
        }
    }

    /**
     * 判断这个修饰符的 interface 是否有效
     *
     * @return 如果这个修饰符的 interface 有效，则返回 true；否则返回 false
     */
    public boolean isInterface() {
        return ((modifier & INTERFACE) == INTERFACE);
    }

    /**
     * 得到这个修饰符对象的深拷贝副本
     *
     * @return 这个修饰符对象的深拷贝副本
     */
    public ModifierInfo copy() {
        ModifierInfo retValue = new ModifierInfo();
        retValue.modifier = this.modifier;

        return retValue;
    }
}
