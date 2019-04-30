package com.alvin.framework.multiend.message.push.pusher;

import com.alvin.framework.multiend.message.push.service.PushLocker;
import com.alvin.framework.multiend.message.push.service.MessageReceiptRepository;
import com.alvin.framework.multiend.message.push.service.MessageRepository;
import com.alvin.framework.multiend.message.push.service.TunnelFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * datetime 2019/4/28 20:35
 *
 * @author sin5
 */
public abstract class MessagePusherBuilder {
    /**
     * executor service
     */
    protected ExecutorService executorService;
    /**
     * gather receipt timeout
     */
    protected long receiptTimeout = 30 * 1000;
    /**
     * message repository
     */
    protected MessageRepository messageRepository;
    /**
     * push lock
     */
    protected PushLocker pushLocker;
    /**
     * receipt repository
     */
    protected MessageReceiptRepository messageReceiptRepository;
    /**
     * receiver tunnel repository
     */
    protected TunnelFactory tunnelFactory;

    public abstract MessagePusher build();

    public MessagePusherBuilder withExecutorService(ExecutorService executorService) {
        if (executorService == null) {
            throw new IllegalArgumentException("executorService must not be null");
        }
        this.executorService = executorService;
        return this;
    }

    public MessagePusherBuilder withReceiptTimeout(long duration, TimeUnit timeunit) {
        if (duration <= 0) {
            throw new IllegalArgumentException("duration must be larger than zero");
        }
        this.receiptTimeout = timeunit.toMillis(duration);
        return this;
    }

    public MessagePusherBuilder withMessageRepository(MessageRepository messageRepository) {
        if (messageRepository == null) {
            throw new IllegalArgumentException("messageRepository must not be null");
        }
        this.messageRepository = messageRepository;
        return this;
    }

    public MessagePusherBuilder withMessagePushLocker(PushLocker pushLocker) {
        if (pushLocker == null) {
            throw new IllegalArgumentException("messagePushLocker must not be null");
        }
        this.pushLocker = pushLocker;
        return this;
    }

    public MessagePusherBuilder withMessageReceiptRepository(MessageReceiptRepository messageReceiptRepository) {
        if (messageReceiptRepository == null) {
            throw new IllegalArgumentException("messageReceiptRepository must not be null");
        }
        this.messageReceiptRepository = messageReceiptRepository;
        return this;
    }

    public MessagePusherBuilder withTunnelRepository(TunnelFactory tunnelFactory) {
        if (tunnelFactory == null) {
            throw new IllegalArgumentException("receiverIntegratedTunnelRepository must not be null");
        }
        this.tunnelFactory = tunnelFactory;
        return this;
    }
}
