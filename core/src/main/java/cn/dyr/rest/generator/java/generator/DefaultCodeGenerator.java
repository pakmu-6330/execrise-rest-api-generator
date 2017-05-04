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

        // 创建一个环境，用于统一对 import 类型的管理
        ImportedTypeManager.remove();
        ImportedTypeManager typeManager = ImportedTypeManager.get().setCurrentPackage(classInfo.getPackageName());

        // 这里先添加普通的引入
        Iterator<ImportStatement> importStatementIterator = classAnalyzer.iterateImportStatements();
        while (importStatementIterator.hasNext()) {
            ImportStatement importStatement = importStatementIterator.next();
            typeManager.addImportType(importStatement.getTypeInfo());
        }

        // 这里添加静态引入
        Iterator<ImportStatement> staticImportStatementIterator = classAnalyzer.iterateStaticImportStatement();
        while (staticImportStatementIterator.hasNext()) {
            ImportStatement importStatement = staticImportStatementIterator.next();
            typeManager.addStaticImport(importStatement.getTypeInfo(), importStatement.getMethodName());
        }

        // 1. 添加包声明语句
        IStatement packageStatement = classAnalyzer.getPackageStatement();
        targetCode.append(packageStatement.toString());
        targetCode.append(ElementsConstant.LINE_SEPARATOR);
        targetCode.append(ElementsConstant.LINE_SEPARATOR);

        // 2. 处理 import 语句
        List<IStatement> allImportedStatement = new ArrayList<>();
        if (config.isStaticImportSeparation()) {

            // 导入普通的 import 操作
            List<ImportedTypeManager.ImportItem> ordinaryImportItem = typeManager.getOrdinaryImportItem();
            mergeImportList(allImportedStatement, ordinaryImportItem);

            // 添加一个空白的语句
            allImportedStatement.add(new EmptyStatement());

            // 导入静态的 static 操作
            List<ImportedTypeManager.ImportItem> staticImportItem = typeManager.getStaticImportItem();
            mergeImportList(allImportedStatement, staticImportItem);

        } else {
            List<ImportedTypeManager.ImportItem> allImportItemList = typeManager.getAllImportItemList();
            mergeImportList(allImportedStatement, allImportItemList);
        }

        // 3. 输出 import 语句
        for (IStatement importStatement : allImportedStatement) {
            targetCode.append(importStatement.toString());

            if (!(importStatement instanceof EmptyStatement)) {
                targetCode.append(ElementsConstant.LINE_SEPARATOR);
            }
        }

        // 4. 处理类本身的语句
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
