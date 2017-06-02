package cn.dyr.rest.generator.java.generator;

import cn.dyr.rest.generator.java.generator.analysis.ClassInfoAnalyzer;
import cn.dyr.rest.generator.java.generator.elements.ElementsConstant;
import cn.dyr.rest.generator.java.generator.statement.ClassStatement;
import cn.dyr.rest.generator.java.generator.statement.EmptyStatement;
import cn.dyr.rest.generator.java.generator.statement.IStatement;
import cn.dyr.rest.generator.java.generator.statement.ImportStatement;
import cn.dyr.rest.generator.java.meta.ClassInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * 代码生成器的实现类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DefaultCodeGenerator implements ICodeGenerator {

    private CodeGeneratorConfig config;

    public DefaultCodeGenerator() {
        config = CodeGeneratorConfig.defaultConfig();
    }

    @Override
    public CodeGeneratorConfig getConfig() {
        return this.config;
    }

    @Override
    public ICodeGenerator setConfig(CodeGeneratorConfig config) {
        this.config = config;
        return this;
    }

    @Override
    public String generateSingleClass(ClassInfo classInfo) {
        StringBuilder targetCode = new StringBuilder();

        ClassInfoAnalyzer classAnalyzer = new ClassInfoAnalyzer();
        classAnalyzer.loadClass(classInfo);

        // 上下文重置，确保不会因为老旧数据影响生成结果
        ImportedTypeManager.remove();
        ImportedTypeManager typeManager = ImportedTypeManager.get().setCurrentPackage(classInfo.getPackageName());

        // 处理分析得到的 import 语句
        Iterator<ImportStatement> importStatementIterator = classAnalyzer.iterateImportStatements();
        while (importStatementIterator.hasNext()) {
            ImportStatement importStatement = importStatementIterator.next();
            typeManager.addImportType(importStatement.getTypeInfo());
        }

        // 处理分析得到的静态 import 语句
        Iterator<ImportStatement> staticImportStatementIterator = classAnalyzer.iterateStaticImportStatement();
        while (staticImportStatementIterator.hasNext()) {
            ImportStatement importStatement = staticImportStatementIterator.next();
            typeManager.addStaticImport(importStatement.getTypeInfo(), importStatement.getMethodName());
        }

        // 生成包声明语句
        IStatement packageStatement = classAnalyzer.getPackageStatement();
        targetCode.append(packageStatement.toString());
        targetCode.append(ElementsConstant.LINE_SEPARATOR);
        targetCode.append(ElementsConstant.LINE_SEPARATOR);

        //  产生 import 语句
        List<IStatement> allImportedStatement = new ArrayList<>();
        if (config.isStaticImportSeparation()) {
            // 产生普通的 import 语句
            List<ImportedTypeManager.ImportItem> ordinaryImportItem = typeManager.getOrdinaryImportItem();
            mergeImportList(allImportedStatement, ordinaryImportItem);

            // 产生一个空白行，提高代码可读性
            allImportedStatement.add(new EmptyStatement());

            // 产生静态 import 语句
            List<ImportedTypeManager.ImportItem> staticImportItem = typeManager.getStaticImportItem();
            mergeImportList(allImportedStatement, staticImportItem);

        } else {
            // 产生所有的 import 语句
            List<ImportedTypeManager.ImportItem> allImportItemList = typeManager.getAllImportItemList();
            mergeImportList(allImportedStatement, allImportItemList);
        }

        // 将上一步产生的 import 语句转换为实际的代码
        for (IStatement importStatement : allImportedStatement) {
            targetCode.append(importStatement.toString());

            if (!(importStatement instanceof EmptyStatement)) {
                targetCode.append(ElementsConstant.LINE_SEPARATOR);
            }
        }

        // 生成这个类对应的最终代码
        ClassStatement classStatement = new ClassStatement(classInfo);

        if (allImportedStatement.size() != 0) {
            targetCode.append(ElementsConstant.LINE_SEPARATOR);
        }

        targetCode.append(classStatement.toString());

        return targetCode.toString();
    }

    private void mergeImportList(List<IStatement> target, List<ImportedTypeManager.ImportItem> items) {
        if (config.isSortImportStatements()) {
            items.sort(new ImportItemComparator(false));
        }

        char lastLetter = '\0';
        for (ImportedTypeManager.ImportItem importItem : items) {
            if (config.isSortImportStatements() &&
                    config.isInsertBlankLineBetweenDifferentLatterInImportSection()) {
                char firstLetter = importItem.toTargetString().charAt(0);

                if (lastLetter == '\0') {
                    lastLetter = firstLetter;
                } else if (lastLetter != firstLetter) {
                    target.add(new EmptyStatement());
                    lastLetter = firstLetter;
                }
            }

            ImportStatement importStatement = new ImportStatement(importItem);
            target.add(importStatement);
        }
    }

    private static class ImportItemComparator implements Comparator<ImportedTypeManager.ImportItem> {

        private boolean desc;

        public ImportItemComparator(boolean desc) {
            this.desc = desc;
        }

        @Override
        public int compare(ImportedTypeManager.ImportItem o1, ImportedTypeManager.ImportItem o2) {
            int i = o1.toTargetString().compareTo(o2.toTargetString());

            return (desc ? -i : i);
        }
    }
}
