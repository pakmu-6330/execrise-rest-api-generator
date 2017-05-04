package cn.dyr.rest.generator.java.generator.analysis;

/**
 * 用于处理多个引入同名类时，如果选择其中一个类创建引入语句
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IImportStatementChooser {

    boolean isABetterThanB(ImportedOperation a, ImportedOperation b);

}
