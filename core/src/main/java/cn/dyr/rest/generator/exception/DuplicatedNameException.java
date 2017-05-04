package cn.dyr.rest.generator.exception;

/**
 * 表示当前范围当中对象名称已经重复
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DuplicatedNameException extends RuntimeException {

    private String name;

    public DuplicatedNameException(String name) {
        super(String.format("duplicated name: %s", name));

        this.name = name;
    }

    public String getName() {
        return name;
    }
}
