package cn.dyr.rest.generator.bridge.channel;

import cn.dyr.rest.generator.bridge.message.Message;

/**
 * 消息对象的处理器
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IMessageConsumer {

    /**
     * 处理这个消息对象
     *
     * @param message 接收到的消息
     */
    void processMessage(Message message);

}
