package cn.dyr.rest.generator.java.generator.analysis;

import cn.dyr.rest.generator.java.meta.TypeInfo;

/**
 * 这个类用于表示类当中尝试引入其他类的请求，用于分析类与类之间的关系，主要是解决引入类重名的问题
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ImportedOperation {

    /**
     * 这个值表示这个类型在类当中被使用，适用于这个值的场合有：<br>
     * <ol>
     * <li>这个类型在字段声明中作为字段的类型出现</li>
     * </ol>
     */
    public static final int TARGET_TYPE_CLASS_INTERFACE = 1;

    /**
     * 这个值表示这个类型在类当中作为父类或者接口出现，适用于这个值的场合有：<br>
     * <ol>
     * <li>这个类型是作为类的父类</li>
     * <li>这个类型是作为类所实现的接口出现的</li>
     * </ol>
     */
    public static final int TARGET_TYPE_PARENT_INTERFACE = 2;

    /**
     * 这个值表示这个类型是作为注解的参数的类型出现，适用这个值的场合有：<br>
     * <ol>
     * <li>这个类型的 class 作为注解的参数出现</li>
     * <li>这是一个枚举类型，该枚举类型作为注解的参数出现</li>
     * </ol>
     */
    public static final int TARGET_TYPE_ANNOTATION_PARAMETER = 3;

    /**
     * 这个值表示这个类型是作为注解出现，适用这个值的场合有：<br>
     * <ol>
     * <li>这个类型作为类的注解出现</li>
     * <li>这个类型作为字段的注解出现</li>
     * <li>这个类型作为方法的注解出现</li>
     * <li>这个类型作为方法实参注解出现</li>
     * </ol>
     */
    public static final int TARGET_TYPE_ANNOTATION = 4;

    /**
     * 这个值表示这个类型是作为方法的参数出现，适用这个值的场合有：<br>
     * <ol>
     * <li>这个类型出现在函数的形参列表类型当中</li>
     * <li>这个类型出现在函数的实参类型当中</li>
     * <li>这个类型出现在函数体当中作为变量的数据类型</li>
     * </ol>
     */
    public static final int TARGET_TYPE_METHOD_PARAMETER_TYPE = 5;

    /**
     * 这个值表示这个类型是作为方法的返回值，适用这个值的场合有：<br>
     * <ol>
     * <li>这个类型在函数的返回值当中出现</li>
     * </ol>
     */
    public static final int TARGET_TYPE_RETURN_VALUE = 7;

    /**
     * 这个值表示的是这个类型是方法有可能抛出异常类的类型信息，适用于这个值的场合有：<br>
     * <ol>
     * <li>这个类型作为异常类在方法中以 throws 关键字进行定义</li>
     * </ol>
     */
    public static final int TARGET_TYPE_METHOD_THROWS = 6;

    private String packageName;
    private String className;
    private int targetType;
    private TypeInfo typeInfo;

    private boolean isStaticImport;
    private String methodName;

    public ImportedOperation() {
        this.isStaticImport = false;
        this.methodName = "";
    }

    public String getPackageName() {
        return packageName;
    }

    public TypeInfo getTypeInfo() {
        return typeInfo;
    }

    public ImportedOperation setTypeInfo(TypeInfo typeInfo) {
        this.typeInfo = typeInfo;

        this.className = this.typeInfo.getName();
        this.packageName = this.typeInfo.getPackageName();
        return this;
    }

    public ImportedOperation setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public ImportedOperation setClassName(String className) {
        this.className = className;
        return this;
    }

    public int getTargetType() {
        return targetType;
    }

    public ImportedOperation setTargetType(int targetType) {
        this.targetType = targetType;
        return this;
    }

    public String getFullName() {
        if (this.packageName == null || "".equals(this.packageName.trim())) {
            return this.className;
        } else {
            return String.format("%s.%s", this.packageName, this.className);
        }
    }

    public boolean isStaticImport() {
        return isStaticImport;
    }

    public ImportedOperation setStaticImport(boolean staticImport) {
        isStaticImport = staticImport;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public ImportedOperation setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImportedOperation operation = (ImportedOperation) o;

        if (targetType != operation.targetType) return false;
        if (isStaticImport != operation.isStaticImport) return false;
        if (packageName != null ? !packageName.equals(operation.packageName) : operation.packageName != null)
            return false;
        if (className != null ? !className.equals(operation.className) : operation.className != null) return false;
        return methodName != null ? methodName.equals(operation.methodName) : operation.methodName == null;
    }

    @Override
    public int hashCode() {
        int result = packageName != null ? packageName.hashCode() : 0;
        result = 31 * result + (className != null ? className.hashCode() : 0);
        result = 31 * result + targetType;
        result = 31 * result + (isStaticImport ? 1 : 0);
        result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
        return result;
    }

    public static ImportedOperation fromTypeInfo(TypeInfo typeInfo, int targetType) {
        ImportedOperation operation = new ImportedOperation();

        operation.setTypeInfo(typeInfo);
        operation.setTargetType(targetType);

        return operation;
    }

    public static ImportedOperation fromTypeInfoStatic(TypeInfo typeInfo, String methodName, int targetType) {
        ImportedOperation operation = new ImportedOperation();

        operation.setStaticImport(true);
        operation.setTypeInfo(typeInfo);
        operation.setTargetType(targetType);
        operation.setMethodName(methodName);

        return operation;
    }
}
