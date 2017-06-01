package cn.dyr.rest.generator.bridge.message;

/**
 * 这个消息里面所有的赋值操作都会被忽略，解决一个消息处理器对消息修改以后引起的消息异常信息
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ReadOnlyMessage extends Message {

    private Message message;

    private ReadOnlyMessage(Message message) {
        this.message = message;
    }

    /**
     * 根据已有的消息创建一个只读消息对象
     *
     * @param message 现有的消息对象
     * @return 这个消息对应只读消息
     */
    public static ReadOnlyMessage fromMessage(Message message) {
        if (message instanceof ReadOnlyMessage) {
            return (ReadOnlyMessage) message;
        }

        return new ReadOnlyMessage(message);
    }

    @Override
    public int getType() {
        return this.message.getType();
    }

    @Override
    public Object getData() {
        return this.message.getData();
    }

    @Override
    public Message setData(Object data) {
        return this;
    }

    @Override
    public Message setType(int type) {
        return this;
    }
}
