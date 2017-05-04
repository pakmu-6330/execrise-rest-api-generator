package cn.dyr.rest.generator.ui.web.common;

/**
 * 用作响应的元数据
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ResponseMeta {

    private int code;
    private String message;
    private String detailed;

    public int getCode() {
        return code;
    }

    public ResponseMeta setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseMeta setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getDetailed() {
        return detailed;
    }

    public ResponseMeta setDetailed(String detailed) {
        this.detailed = detailed;
        return this;
    }
}
