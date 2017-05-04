package cn.dyr.rest.generator.ui.web.exception;

/**
 * 这个异常表示请求的资源不存在
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ResourceNotFoundException extends RuntimeException {

    private String desc;

    public ResourceNotFoundException(String desc) {
        super(desc);

        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public ResourceNotFoundException setDesc(String desc) {
        this.desc = desc;
        return this;
    }
}
