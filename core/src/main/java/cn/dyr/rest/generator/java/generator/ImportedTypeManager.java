package cn.dyr.rest.generator.java.generator;

import cn.dyr.rest.generator.java.meta.TypeInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 这是一个用于管理已经通过 import 语句引入的类，其主要目的是在语句转换过程中判断是输出类型全名还是类名
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ImportedTypeManager {

    /**
     * 表示一个类的导入项目
     */
    public static class ImportItem {

        private boolean isStaticImport;
        private TypeInfo typeInfo;
        private String method;

        public boolean isStaticImport() {
            return isStaticImport;
        }

        public ImportItem setStaticImport(boolean staticImport) {
            isStaticImport = staticImport;
            return this;
        }

        public TypeInfo getTypeInfo() {
            return typeInfo;
        }

        public ImportItem setTypeInfo(TypeInfo typeInfo) {
            this.typeInfo = typeInfo;
            return this;
        }

        public String getMethod() {
            return method;
        }

        public ImportItem setMethod(String method) {
            this.method = method;
            return this;
        }

        public String toTargetString() {
            if (this.isStaticImport) {
                return String.format("%s.%s", typeInfo.getFullName(), method);
            } else {
                return this.typeInfo.getFullName();
            }
        }
    }

    private static final ThreadLocal<ImportedTypeManager> INSTANCES;

    static {
        INSTANCES = new ThreadLocal<>();
    }

    public static ImportedTypeManager get() {
        ImportedTypeManager typeManager = INSTANCES.get();

        if (typeManager == null) {
            typeManager = new ImportedTypeManager();
            INSTANCES.set(typeManager);
        }

        return typeManager;
    }

    public static void remove() {
        INSTANCES.remove();
    }

    private String currentPackage;

    private Map<String, TypeInfo> importedTypeInfoByClassName;
    private Map<String, TypeInfo> importedTypeInfoByStaticImport;

    public ImportedTypeManager() {
        this.importedTypeInfoByClassName = new HashMap<>();
        this.importedTypeInfoByStaticImport = new HashMap<>();
        this.currentPackage = "java.lang";
    }

    /**
     * 获得这个 import 管理器所在上下文类的所在包
     *
     * @return 所在包
     */
    public String getCurrentPackage() {
        return currentPackage;
    }

    /**
     * 设置这个 import 管理器所在上下文类的所在包
     *
     * @param currentPackage 类所在包
     * @return 这个管理器本身
     */
    public ImportedTypeManager setCurrentPackage(String currentPackage) {
        this.currentPackage = currentPackage;
        return this;
    }

    /**
     * 添加一个普通的引入类型<br>
     * <p>
     * 这个方法的行为定义如下：<br>
     * 如果这个管理器当中没有存在类名相同的，则记录这条引入记录<br>
     * 如果这个管理器当中存在类名相同，无论这个类型是否为已经引入的类型，都不会执行任何的操作<br>
     * </p>
     *
     * @param typeInfo 要添加的引入类型
     */
    public void addImportType(TypeInfo typeInfo) {
        String className = typeInfo.getName();
        this.importedTypeInfoByClassName.putIfAbsent(className, typeInfo);
    }

    /**
     * 添加一个静态引入<br>
     * <p>
     * 这个方法的行为定义如下：<br>
     * 如果这个方法或者常量还没有被静态引入，则记录这条的静态引用记录
     * 如果这个方法或者常量已经通过静态引入的方式引入，则不会执行任何操作
     * </p>
     *
     * @param typeInfo 要静态引入的类
     * @param name     静态引入类的常量或者方法名称
     */
    public void addStaticImport(TypeInfo typeInfo, String name) {
        this.importedTypeInfoByStaticImport.putIfAbsent(name, typeInfo);
    }

    /**
     * 判断这个类型是否已经被导入
     *
     * @param typeInfo 要判断是否已经导入的类型信息
     * @return 如果这个类型已经被导入，则返回 true；否则返回 false
     */
    public boolean isTypeImported(TypeInfo typeInfo) {
        // 如果引入类与当前类同包，直接返回 true
        if (typeInfo.isPrimitiveType() ||
                typeInfo.getPackageName().equals(this.currentPackage) ||
                typeInfo.getPackageName().equals("java.lang")) {
            return true;
        }

        String name = typeInfo.getName();
        TypeInfo importedType = this.importedTypeInfoByClassName.get(name);
        if (importedType == null) {
            return false;
        }

        return typeInfo.getPackageName().equals(importedType.getPackageName());
    }

    /**
     * 判断这个类是否已经被静态导入
     *
     * @param typeInfo 要判断是否静态导入
     * @param method   判断静态导入的方法或者常量
     * @return 如果这个常量或者方法已经被导入，则返回 true；否则返回 false
     */
    public boolean isTypeStaticImported(TypeInfo typeInfo, String method) {
        TypeInfo staticImportedType = this.importedTypeInfoByStaticImport.get(method);
        if (staticImportedType == null) {
            return false;
        }

        return (staticImportedType.getName().equals(typeInfo.getName()) &&
                staticImportedType.getPackageName().equals(typeInfo.getPackageName()));
    }

    /**
     * 获得一个用于迭代所有所有导入操作（包含普通的导入和静态导入）的迭代器
     *
     * @return 用于迭代所有导入操作的迭代器
     */
    public Iterator<ImportItem> getAllImportItems() {
        return getAllImportItemList().iterator();
    }

    /**
     * 获得一个储存有所有导入操作的列表
     *
     * @return 一个储存有所有导入操作的列表
     */
    public List<ImportItem> getAllImportItemList() {
        List<ImportItem> retValue = new ArrayList<>();

        mergeImportDataToList(retValue);
        mergeStaticDataToList(retValue);

        return retValue;
    }

    /**
     * 获得一个储存有普通导入操作的列表
     *
     * @return 一个储存有普通导入操作的列表
     */
    public List<ImportItem> getOrdinaryImportItem() {
        List<ImportItem> retValue = new ArrayList<>();

        mergeImportDataToList(retValue);

        return retValue;
    }

    /**
     * 将普通的导入操作数据导入到列表当中
     *
     * @param retValue 导入的目的地
     */
    private void mergeImportDataToList(List<ImportItem> retValue) {
        if (this.importedTypeInfoByClassName != null &&
                this.importedTypeInfoByClassName.size() > 0) {
            for (TypeInfo typeInfo : this.importedTypeInfoByClassName.values()) {
                ImportItem importItem = new ImportItem()
                        .setStaticImport(false)
                        .setTypeInfo(typeInfo)
                        .setMethod("");
                retValue.add(importItem);
            }
        }
    }

    /**
     * 获得一个储存有静态导入操作的列表
     *
     * @return 一个储存有静态导入操作的列表
     */
    public List<ImportItem> getStaticImportItem() {
        List<ImportItem> retValue = new ArrayList<>();

        mergeStaticDataToList(retValue);

        return retValue;
    }

    /**
     * 将管理当中的静态导入操作数据导入到列表当中
     *
     * @param retValue 导入的目的地
     */
    private void mergeStaticDataToList(List<ImportItem> retValue) {
        if (this.importedTypeInfoByStaticImport != null &&
                this.importedTypeInfoByStaticImport.size() > 0) {
            Set<Map.Entry<String, TypeInfo>> entries = this.importedTypeInfoByStaticImport.entrySet();
            for (Map.Entry<String, TypeInfo> entry : entries) {
                ImportItem importItem = new ImportItem()
                        .setStaticImport(true)
                        .setTypeInfo(entry.getValue())
                        .setMethod(entry.getKey());
                retValue.add(importItem);
            }
        }
    }
}
