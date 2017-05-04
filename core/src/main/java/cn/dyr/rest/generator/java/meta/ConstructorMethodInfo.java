package cn.dyr.rest.generator.java.meta;

/**
 * 表示一个构造方法
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ConstructorMethodInfo extends MethodInfo {

    /**
     * 创建一个类的构造方法
     *
     * @param classTypeInfo 这个类的类型信息
     */
    public ConstructorMethodInfo(TypeInfo classTypeInfo) {
        setReturnValueType(classTypeInfo);
    }

    /**
     * 因为构造方法是不允许为 static 的，所以对构造方法进行 static 操作会抛出 IllegalStateException 异常
     *
     * @return 这个方法信息本身
     * @throws IllegalStateException 调用必抛
     */
    @Override
    public MethodInfo setStatic() {
        throw new IllegalStateException("constructor static flag operation is not supported");
    }

    /**
     * 因为构造方法是不允许为 static 的，所以对构造方法进行 static 操作会抛出 IllegalStateException 异常
     *
     * @return 这个方法信息本身
     * @throws IllegalStateException 调用必抛
     */
    @Override
    public MethodInfo unsetStatic() {
        throw new IllegalStateException("constructor static flag operation is not supported");
    }

    /**
     * 获得这个方法的方法名称。因为构造方法的名称是固定的，所以这个方法的返回值也是固定的
     *
     * @return &lt;init&gt;
     */
    @Override
    public String getName() {
        return "<init>";
    }

    /**
     * 在构造方法上设置方法名的操作是不被允许的，会抛出 IllegalStateException 异常
     *
     * @param name 要设置的方法的新名称
     * @return 这个方法本身
     */
    @Override
    public MethodInfo setName(String name) {
        throw new IllegalStateException("setName invocation is not allowed on constructor");
    }

    @Override
    public int getMethodType() {
        return METHOD_TYPE_CONSTRUCTOR;
    }
}
