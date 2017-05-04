package cn.dyr.rest.generator.ui.web.exception;

/**
 * 表示操作违背了资源唯一条件的限制
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DuplicatedConstraintException extends RuntimeException {

    private String desc;

    public DuplicatedConstraintException(String desc) {
        super(desc);
    }

    public String getDesc() {
        return desc;
    }

    public DuplicatedConstraintException setDesc(String desc) {
        this.desc = desc;
        return this;
    }
}
