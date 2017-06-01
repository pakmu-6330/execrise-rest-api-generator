package cn.dyr.rest.generator.bridge.message;

/**
 * 表示一条要进行推送的消息
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class Message {

    public static final int TYPE_GENERATE_PROGRESS = 1;

    private int type;
    private Object data;

    public int getType() {
        return type;
    }

    public Message setType(int type) {
        this.type = type;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Message setData(Object data) {
        this.data = data;
        return this;
    }
}
