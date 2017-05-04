package cn.dyr.rest.generator.java.generator.analysis;

/**
 * 根据引入类型（注解，返回值，字段类型）以及类全名的长度
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DefaultImportStatementChooser implements IImportStatementChooser {

    @Override
    public boolean isABetterThanB(ImportedOperation a, ImportedOperation b) {
        if (a.getTargetType() > b.getTargetType()) {
            return true;
        }

        return a.getFullName().length() > b.getFullName().length();

    }

}
