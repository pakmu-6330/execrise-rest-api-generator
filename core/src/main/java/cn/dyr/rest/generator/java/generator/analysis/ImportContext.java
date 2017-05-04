package cn.dyr.rest.generator.java.generator.analysis;

import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 用于对当前类分析时接收 import 操作类型的类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ImportContext {

    private Set<ImportedOperation> operations;
    private Set<ImportedOperation> staticOperations;

    private String currentClassPackage;

    public ImportContext(String currentClassPackage) {
        this.currentClassPackage = currentClassPackage;

        this.operations = new HashSet<>();
        this.staticOperations = new HashSet<>();
    }

    private boolean importUseless(TypeInfo typeInfo) {
        return typeInfo.isPrimitiveType() ||
                typeInfo.getPackageName().equals("java.lang") ||
                typeInfo.getPackageName().equals(this.currentClassPackage) ||
                typeInfo == TypeInfoFactory.voidType();

    }

    /**
     * 对类型的泛型信息进行 import 处理
     *
     * @param typeInfo   要进行 import 处理类型
     * @param importType import 请求的来源
     */
    private void handleParameterizeType(TypeInfo typeInfo, int importType) {
        List<TypeInfo> parameterizeType = typeInfo.getParameterizeType();
        if (parameterizeType != null && parameterizeType.size() > 0) {
            for (TypeInfo anotherType : parameterizeType) {
                if (!anotherType.isPlaceHolder()) {
                    addImportOperation(anotherType, importType);
                }
            }
        }
    }

    /**
     * 往这个 context 对象当中添加一个 import 的操作
     *
     * @param operation 要添加到这个 context 对象当中的 import 操作
     */
    public void addImportOperation(ImportedOperation operation) {
        if (importUseless(operation.getTypeInfo())) {
            return;
        }

        operations.add(operation);

        // 处理泛型信息
        handleParameterizeType(operation.getTypeInfo(), operation.getTargetType());
    }

    /**
     * 往这个 context 对象当中添加一个 import 的操作
     *
     * @param typeInfo   要添加 import 操作的类型
     * @param importType 添加 import 类型的方式
     */
    public void addImportOperation(TypeInfo typeInfo, int importType) {
        if (importUseless(typeInfo)) {
            return;
        }

        // 处理类型本身
        ImportedOperation operation = ImportedOperation.fromTypeInfo(typeInfo, importType);
        operations.add(operation);

        // 处理泛型信息
        handleParameterizeType(typeInfo, importType);
    }

    /**
     * 往这个 context 当中添加一个静态 import 操作
     *
     * @param classType  要添加 import 操作的类型
     * @param name       通过静态方式 import 的方法或者常量名称
     * @param importType 添加 import 类型的方式
     */
    public void addStaticImport(TypeInfo classType, String name, int importType) {

        // 处理静态引入
        ImportedOperation operation = ImportedOperation.fromTypeInfoStatic(classType, name, importType);
        staticOperations.add(operation);
    }

    /**
     * 获得一个用于迭代已经导入类操作的迭代器
     *
     * @return 用于迭代导入类操作的迭代器
     */
    public Iterator<ImportedOperation> iterateImport() {
        return operations.iterator();
    }

    /**
     * 获得一个用于迭代已经进行静态导入的迭代器
     *
     * @return 用于迭代静态导入操作的迭代器
     */
    public Iterator<ImportedOperation> iterateStaticImport() {
        return staticOperations.iterator();
    }
}
