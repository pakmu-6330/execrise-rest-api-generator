package cn.dyr.rest.generator.ui.web.exception;

/**
 * Service 类校验参数不通过时抛出这个异常
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class BadParameterError extends RuntimeException {

    private String desc;

    public BadParameterError(String desc) {
        super(desc);
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public BadParameterError setDesc(String desc) {
        this.desc = desc;
        return this;
    }
}
