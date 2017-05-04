package cn.dyr.rest.generator.ui.web.common;

import java.util.Objects;

/**
 * 这个类用来定义 API 的响应对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class APIResponse<T> {

    private ResponseMeta meta;
    private T data;

    public APIResponse(ResponseMeta meta, T data) {
        Objects.requireNonNull(meta, "meta info is null");
        Objects.requireNonNull(data, "data is null");

        this.meta = meta;
        this.data = data;
    }

    public ResponseMeta getMeta() {
        return meta;
    }

    public APIResponse setMeta(ResponseMeta meta) {
        this.meta = meta;
        return this;
    }

    public T getData() {
        return data;
    }

    public APIResponse setData(T data) {
        this.data = data;
        return this;
    }
}
