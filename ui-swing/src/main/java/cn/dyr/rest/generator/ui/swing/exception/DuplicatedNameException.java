package cn.dyr.rest.generator.ui.swing.exception;

/**
 * 这个异常表示在一个作用域类存在同名的对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DuplicatedNameException extends RuntimeException {

    public static final String TYPE_ENTITY = "entity";

    public static final String TYPE_ATTRIBUTE = "attribute";

    public static final String TYPE_RELATIONSHIP = "relationship";

    private String type;
    private String name;

    /**
     * 创建一个同名的异常
     *
     * @param type 同名的类型
     * @param name 相同的名称
     */
    public DuplicatedNameException(String type, String name) {
        super(String.format("same type of %s object named %s exists in context", type, name));

        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
