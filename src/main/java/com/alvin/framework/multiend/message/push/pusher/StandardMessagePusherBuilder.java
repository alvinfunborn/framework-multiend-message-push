package com.alvin.framework.multiend.message.push.pusher;

import com.alvin.framework.multiend.message.push.locker.MessagePushLocker;
import com.alvin.framework.multiend.message.push.manager.PushManager;
import com.alvin.framework.multiend.message.push.repository.MessageReceiptRepository;
import com.alvin.framework.multiend.message.push.repository.MessageRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * datetime 2019/4/28 17:06
 *
 * @author sin5
 */
public class StandardMessagePusherBuilder<T> {

    /**
     * executor service
     */
    private ExecutorService executorService;
    /**
     * gather receipt timeout
     */
    private long receiptTimeout = 30 * 1000;
    /**
     * push helper. need impl
     */
    private PushManager<T> pushManager;
    /**
     * message repository
     */
    private MessageRepository<T> messageRepository;
    /**
     * push lock
     */
    private MessagePushLocker messagePushLocker;
    /**
     * receipt repository
     */
    private MessageReceiptRepository messageReceiptRepository;

    public StandardMessagePusherBuilder<T> withExecutorService(ExecutorService executorService) {
        if (executorService == null) {
            throw new IllegalArgumentException("executorService must not bu null");
        }
        this.executorService = executorService;
        return this;
    }

    public StandardMessagePusherBuilder<T> withReceiptTimeout(long duration, TimeUnit timeunit) {
        if (duration <= 0) {
            throw new IllegalArgumentException("duration must be larger than zero");
        }
        this.receiptTimeout = timeunit.toMillis(duration);
        return this;
    }

    public StandardMessagePusherBuilder<T> withPushManager(PushManager<T> pushManager) {
        if (pushManager == null) {
            throw new IllegalArgumentException("pushManager must not bu null");
        }
        this.pushManager = pushManager;
        return this;
    }

    public StandardMessagePusherBuilder<T> withMessageRepository(MessageRepository<T> messageRepository) {
        if (messageRepository == null) {
            throw new IllegalArgumentException("messageRepository must not bu null");
        }
        this.messageRepository = messageRepository;
        return this;
    }

    public StandardMessagePusherBuilder<T> withMessagePushLocker(MessagePushLocker messagePushLocker) {
        if (messagePushLocker == null) {
            throw new IllegalArgumentException("messagePushLocker must not bu null");
        }
        this.messagePushLocker = messagePushLocker;
        return this;
    }

    public StandardMessagePusherBuilder<T> withMessageReceiptRepository(MessageReceiptRepository messageReceiptRepository) {
        if (messageReceiptRepository == null) {
            throw new IllegalArgumentException("messageReceiptRepository must not bu null");
        }
        this.messageReceiptRepository = messageReceiptRepository;
        return this;
    }

    public StandardMessagePusher<T> build() {
        if (executorService == null) {
            this.executorService = Executors.newCachedThreadPool();
        }
        if (this.pushManager == null) {
            throw new IllegalArgumentException("no bean of pushManager defined");
        }
        if (messageRepository == null) {
            throw new IllegalArgumentException("messageRepository must not be null");
        }
        if (messagePushLocker == null) {
            throw new IllegalArgumentException("no bean of messagePushLock defined");
        }
        if (messageReceiptRepository == null) {
            throw new IllegalArgumentException("no bean of messageReceiptRepository defined");
        }
        return new StandardMessagePusher<>(executorService,
                pushManager,
                messageRepository,
                messagePushLocker,
                messageReceiptRepository,
                receiptTimeout);
    }

}
