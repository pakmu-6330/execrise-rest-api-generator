package cn.dyr.rest.generator.java.generator.analysis;

import cn.dyr.rest.generator.java.generator.statement.IStatement;
import cn.dyr.rest.generator.java.generator.statement.ImportStatement;
import cn.dyr.rest.generator.java.generator.statement.PackageStatement;
import cn.dyr.rest.generator.java.meta.ClassInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用于分析类信息的分析类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ClassInfoAnalyzer {

    private Map<String, List<ImportedOperation>> importByName;
    private Map<String, ImportedOperation> importByFullName;
    private List<ImportStatement> parsedImportStatement;
    private List<ImportStatement> parsedStaticImportStatement;
    private PackageStatement packageStatement;

    private IImportStatementChooser importStatementChooser;

    public ClassInfoAnalyzer() {
        this.importByFullName = new HashMap<>();
        this.importByName = new HashMap<>();
        this.parsedImportStatement = new ArrayList<>();
        this.parsedStaticImportStatement = new ArrayList<>();
        this.importStatementChooser = new DefaultImportStatementChooser();
    }

    /**
     * 对类当中所有的 import 语句进行迭代，获得类当中所有的 import 语句
     *
     * @return 一个用于迭代 import 语句的迭代器
     */
    public Iterator<ImportStatement> iterateImportStatements() {
        return this.parsedImportStatement.iterator();
    }

    /**
     * 对类当中所有的静态 import 语句进行迭代，获得类当中所有的静态 import 语句
     *
     * @return 一个用于迭代静态 import 语句的迭代器
     */
    public Iterator<ImportStatement> iterateStaticImportStatement() {
        return this.parsedStaticImportStatement.iterator();
    }

    /**
     * 获得这个类的包声明语句
     *
     * @return 这个类的包声明语句
     */
    public IStatement getPackageStatement() {
        return this.packageStatement;
    }

    /**
     * 对已经解析出来的 import 语句进行清空处理
     */
    public void reset() {
        this.parsedImportStatement.clear();
    }

    /**
     * 加载类信息，分析这个类信息
     *
     * @param classInfo 要进行分析的类信息
     */
    public void loadClass(ClassInfo classInfo) {
        if (classInfo == null) {
            throw new NullPointerException("classInfo is null");
        }

        // 1. 对类的第三方包引入进行扫描
        ImportContext context = new ImportContext(classInfo.getPackageName());
        classInfo.fillImportOperations(context);

        // 2. 检索已经明确导入的第三方包，按照类名进行重新聚合操作
        Iterator<ImportedOperation> importedOperationIterator = context.iterateImport();
        Map<String, List<ImportedOperation>> operations = new HashMap<>();
        while (importedOperationIterator.hasNext()) {
            ImportedOperation importedOperation = importedOperationIterator.next();

            String name = importedOperation.getTypeInfo().getName();
            List<ImportedOperation> sameClassNameList = operations.computeIfAbsent(name, k -> new ArrayList<>());

            sameClassNameList.add(importedOperation);
        }

        // 3. 检索已经导入的静态 import 语句，按照方法的名称进行重新聚合操作
        Iterator<ImportedOperation> staticImporterIterator = context.iterateStaticImport();
        Map<String, List<ImportedOperation>> staticOperations = new HashMap<>();
        while (staticImporterIterator.hasNext()) {
            ImportedOperation staticImport = staticImporterIterator.next();

            String methodName = staticImport.getMethodName();
            List<ImportedOperation> operationList = staticOperations.computeIfAbsent(methodName, k -> new ArrayList<>());

            operationList.add(staticImport);
        }

        // 4. 根据 import 语句的选择逻辑选择最合适的类作为最终的 import 语句
        // 4.1 这里对普通的 import 语句进行选择
        Set<String> classNames = operations.keySet();
        for (String className : classNames) {
            List<ImportedOperation> operationList = operations.get(className);
            ImportedOperation betterImport = null;

            for (ImportedOperation operation : operationList) {
                if (betterImport == null ||
                        this.importStatementChooser.isABetterThanB(operation, betterImport)) {
                    betterImport = operation;
                }
            }

            if (betterImport != null) {
                ImportStatement importStatement = new ImportStatement(betterImport.getTypeInfo());
                this.parsedImportStatement.add(importStatement);
            }
        }

        // 4.2 这里对静态的 import 语句进行选择
        Set<String> staticImportMethodName = staticOperations.keySet();
        for (String staticMethodName : staticImportMethodName) {
            List<ImportedOperation> operationList = staticOperations.get(staticMethodName);
            ImportedOperation betterImport = null;

            for (ImportedOperation operation : operationList) {
                if (betterImport == null ||
                        this.importStatementChooser.isABetterThanB(operation, betterImport)) {
                    betterImport = operation;
                }
            }

            if (betterImport != null) {
                ImportStatement importStatement = new ImportStatement(betterImport.getTypeInfo(),
                        betterImport.getMethodName());
                this.parsedStaticImportStatement.add(importStatement);
            }
        }

        // 5. 产生包声明的语句
        if (!classInfo.isDefaultPackage()) {
            this.packageStatement = new PackageStatement(classInfo.getPackageName());
        }
    }
}
