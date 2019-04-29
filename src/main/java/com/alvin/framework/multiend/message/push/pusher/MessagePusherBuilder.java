package com.alvin.framework.multiend.message.push.pusher;

import com.alvin.framework.multiend.message.push.locker.MessagePushLocker;
import com.alvin.framework.multiend.message.push.manager.PushManager;
import com.alvin.framework.multiend.message.push.repository.MessageReceiptRepository;
import com.alvin.framework.multiend.message.push.repository.MessageRepository;
import com.alvin.framework.multiend.message.push.tunnel.MessagePushTunnelFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * datetime 2019/4/28 20:35
 *
 * @author sin5
 */
public abstract class MessagePusherBuilder<T> {
    /**
     * executor service
     */
    protected ExecutorService executorService;
    /**
     * gather receipt timeout
     */
    protected long receiptTimeout = 30 * 1000;
    /**
     * push helper. need impl
     */
    protected PushManager pushManager;
    /**
     * message repository
     */
    protected MessageRepository<T> messageRepository;
    /**
     * push lock
     */
    protected MessagePushLocker messagePushLocker;
    /**
     * receipt repository
     */
    protected MessageReceiptRepository messageReceiptRepository;
    /**
     * tunnel factory
     */
    protected MessagePushTunnelFactory messagePushTunnelFactory;

    public abstract MessagePusher<T> build();

    public MessagePusherBuilder<T> withExecutorService(ExecutorService executorService) {
        if (executorService == null) {
            throw new IllegalArgumentException("executorService must not be null");
        }
        this.executorService = executorService;
        return this;
    }

    public MessagePusherBuilder<T> withReceiptTimeout(long duration, TimeUnit timeunit) {
        if (duration <= 0) {
            throw new IllegalArgumentException("duration must be larger than zero");
        }
        this.receiptTimeout = timeunit.toMillis(duration);
        return this;
    }

    public MessagePusherBuilder<T> withPushManager(PushManager pushManager) {
        if (pushManager == null) {
            throw new IllegalArgumentException("pushManager must not be null");
        }
        this.pushManager = pushManager;
        return this;
    }

    public MessagePusherBuilder<T> withMessageRepository(MessageRepository<T> messageRepository) {
        if (messageRepository == null) {
            throw new IllegalArgumentException("messageRepository must not be null");
        }
        this.messageRepository = messageRepository;
        return this;
    }

    public MessagePusherBuilder<T> withMessagePushLocker(MessagePushLocker messagePushLocker) {
        if (messagePushLocker == null) {
            throw new IllegalArgumentException("messagePushLocker must not be null");
        }
        this.messagePushLocker = messagePushLocker;
        return this;
    }

    public MessagePusherBuilder<T> withMessageReceiptRepository(MessageReceiptRepository messageReceiptRepository) {
        if (messageReceiptRepository == null) {
            throw new IllegalArgumentException("messageReceiptRepository must not be null");
        }
        this.messageReceiptRepository = messageReceiptRepository;
        return this;
    }

    public MessagePusherBuilder<T> withMessagePushTunnelFactory(MessagePushTunnelFactory messagePushTunnelFactory) {
        if (messagePushTunnelFactory == null) {
            throw new IllegalArgumentException("messagePushTunnelFactory must not be null");
        }
        this.messagePushTunnelFactory = messagePushTunnelFactory;
        return this;
    }
}
