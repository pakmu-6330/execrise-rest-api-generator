package cn.dyr.rest.generator.java.generator;

/**
 * 这是一个用于保存代码生成器类配置信息类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class CodeGeneratorConfig {

    /**
     * 获得含有默认设置的配置类
     *
     * @return 含有默认设置的配置类
     */
    public static CodeGeneratorConfig defaultConfig() {
        CodeGeneratorConfig retValue = new CodeGeneratorConfig();

        retValue.sortImportStatements = true;
        retValue.insertBlankLineBetweenDifferentLatterInImportSection = true;

        return retValue;
    }

    private boolean sortImportStatements;
    private boolean insertBlankLineBetweenDifferentLatterInImportSection;
    private boolean staticImportSeparation;

    /**
     * 获得用于指定是否将 import 语句进行排序的布尔值
     *
     * @return 一个布尔值，这个值用于指定在代码生成的过程中是否会将 import 语句进行字典排序
     */
    public boolean isSortImportStatements() {
        return sortImportStatements;
    }

    /**
     * 设置在代码生成的过程当中是否会对 import 语句进行字典排序
     *
     * @param sortImportStatements 是否会将 import 语句在代码生成的过程当中进行字典排序
     * @return 这个配置信息类本身
     */
    public CodeGeneratorConfig setSortImportStatements(boolean sortImportStatements) {
        this.sortImportStatements = sortImportStatements;
        return this;
    }

    /**
     * 是否在 import 语句部分的不同首字母之间增加一个空行
     *
     * @return 一个布尔值，表示 import 语句部分是否会在不同字母的语句之间增加一个空行
     */
    public boolean isInsertBlankLineBetweenDifferentLatterInImportSection() {
        return insertBlankLineBetweenDifferentLatterInImportSection;
    }

    /**
     * 设置在代码生成的过程当中是否会在 import 部分的不同首字母的语句之间增加一个空白行
     *
     * @param insertBlankLineBetweenDifferentLatterInImportSection 设置值
     * @return 这个配置信息类本身
     */
    public CodeGeneratorConfig setInsertBlankLineBetweenDifferentLatterInImportSection(boolean insertBlankLineBetweenDifferentLatterInImportSection) {
        this.insertBlankLineBetweenDifferentLatterInImportSection = insertBlankLineBetweenDifferentLatterInImportSection;
        return this;
    }

    /**
     * 是否将普通的 import 语句和静态的 import 语句进行分开处理
     *
     * @return 表示是否将普通 import 语句和静态 import 语句进行分开处理
     */
    public boolean isStaticImportSeparation() {
        return staticImportSeparation;
    }

    /**
     * 设置是否将普通的 import 语句和静态的 import 语句进行分开处理
     *
     * @param staticImportSeparation 表示是否将普通 import 语句和静态 import 语句进行分开处理
     * @return 这个配置本身
     */
    public CodeGeneratorConfig setStaticImportSeparation(boolean staticImportSeparation) {
        this.staticImportSeparation = staticImportSeparation;
        return this;
    }
}
