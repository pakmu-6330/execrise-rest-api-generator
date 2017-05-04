package cn.dyr.rest.generator.bridge.channel;

import cn.dyr.rest.generator.bridge.message.Message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 这个表示一个消息队列，用于生成核心与其他组件之间的通讯
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class MessageChannel {

    private static MessageChannel INSTANCE;

    static {
        INSTANCE = new MessageChannel();
    }

    public static MessageChannel getDefaultChannel() {
        return INSTANCE;
    }

    private ExecutorService pushThreadPool;
    private ExecutorService executeThread;

    private Set<IMessageConsumer> consumerSet;

    private final class MessageExecute extends Thread {
        private Message message;

        public MessageExecute(Message message) {
            this.message = message;
        }

        @Override
        public void run() {
            if (consumerSet == null) {
                return;
            }

            for (int i = 0; i < consumerSet.size(); i++) {
                for (IMessageConsumer consumer : consumerSet) {
                    consumer.processMessage(message);
                }
            }
        }
    }

    private MessageChannel() {
        pushThreadPool = Executors.newFixedThreadPool(1);
        executeThread = Executors.newFixedThreadPool(3);

        this.consumerSet = new HashSet<>();
    }

    /**
     * 往当前已经注册的消息处理器推送消息
     *
     * @param message 要进行推送的消息
     */
    public void pushMessage(Message message) {
        MessageExecute execute = new MessageExecute(message);
        this.pushThreadPool.execute(execute);
    }

    /**
     * 往这个消息通道添加一个消息处理器
     *
     * @param consumer 要添加到这个消息通道的消息处理器
     */
    public void addConsumer(IMessageConsumer consumer) {
        if (this.consumerSet == null) {
            this.consumerSet = new HashSet<>();
        }

        if (!this.consumerSet.contains(consumer)) {
            this.consumerSet.add(consumer);
        }
    }

    /**
     * 从这个消息通道中删除一个消息处理器
     *
     * @param consumer 要从消息通道中删除的消息处理器
     */
    public void removeConsumer(IMessageConsumer consumer) {
        if (this.consumerSet == null || this.consumerSet.isEmpty()) {
            return;
        }

        this.consumerSet.remove(consumer);
    }
}
