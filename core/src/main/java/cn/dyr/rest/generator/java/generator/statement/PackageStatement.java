package cn.dyr.rest.generator.java.generator.statement;

/**
 * 用于表示每个 Java 文件的包声明语句
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class PackageStatement implements IStatement {

    private String packageName;

    public PackageStatement(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return String.format("package %s;", packageName);
    }
}
