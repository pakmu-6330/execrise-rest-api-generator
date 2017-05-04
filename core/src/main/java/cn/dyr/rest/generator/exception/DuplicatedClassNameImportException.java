package cn.dyr.rest.generator.exception;

/**
 * 这个异常表示在同一个上下文情况下引入两个同名不同包的类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DuplicatedClassNameImportException extends RuntimeException {

    private String classExisted;
    private String classToBeImported;

    public DuplicatedClassNameImportException(String classExisted, String classToBeImported) {
        super(String.format("class name conflicted: %s and %s", classExisted, classToBeImported));

        this.classExisted = classExisted;
        this.classToBeImported = classToBeImported;
    }

    public String getClassExisted() {
        return classExisted;
    }

    public String getClassToBeImported() {
        return classToBeImported;
    }
}
