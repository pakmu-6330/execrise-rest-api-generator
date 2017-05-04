package cn.dyr.rest.generator.java.generator.statement;

import cn.dyr.rest.generator.java.generator.ImportedTypeManager;
import cn.dyr.rest.generator.java.meta.TypeInfo;

/**
 * 这个对象表示一个类引入的语句
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ImportStatement implements IStatement {

    private TypeInfo typeInfo;
    private boolean isStaticImport;
    private String methodName;

    /**
     * 根据一个导入的操作初始化导入语句的相关值
     *
     * @param importItem 导入的项目
     */
    public ImportStatement(ImportedTypeManager.ImportItem importItem) {
        this.isStaticImport = importItem.isStaticImport();
        this.typeInfo = importItem.getTypeInfo();
        this.methodName = importItem.getMethod();
    }

    /**
     * 创建一个普通的 import 语句
     *
     * @param typeInfo 要 import 的类型信息
     */
    public ImportStatement(TypeInfo typeInfo) {
        this.typeInfo = typeInfo;
    }

    /**
     * 创建一个静态 import 语句
     *
     * @param typeInfo   要 import 的类型信息
     * @param methodName 要静态引入的方法，常量名称
     */
    public ImportStatement(TypeInfo typeInfo, String methodName) {
        this.typeInfo = typeInfo;
        this.methodName = methodName;

        this.isStaticImport = true;
    }

    public TypeInfo getTypeInfo() {
        return typeInfo;
    }

    public String getFullName() {
        return this.typeInfo.getFullName();
    }

    public boolean isStaticImport() {
        return isStaticImport;
    }

    public ImportStatement setStaticImport(boolean staticImport) {
        isStaticImport = staticImport;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public ImportStatement setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    @Override
    public String toString() {
        if (isStaticImport) {
            return String.format("import static %s.%s;", this.typeInfo.getFullName(), this.methodName);
        } else {
            return String.format("import %s;", this.typeInfo.getFullName());
        }
    }
}
