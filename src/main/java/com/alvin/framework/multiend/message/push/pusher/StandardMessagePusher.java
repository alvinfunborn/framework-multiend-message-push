package com.alvin.framework.multiend.message.push.pusher;

import com.alvin.framework.multiend.message.push.locker.MessagePushLocker;
import com.alvin.framework.multiend.message.push.manager.PushManager;
import com.alvin.framework.multiend.message.push.repository.MessageReceiptRepository;
import com.alvin.framework.multiend.message.push.repository.MessageRepository;
import com.alvin.framework.multiend.message.push.tunnel.MessagePushTunnel;

import java.util.concurrent.ExecutorService;

/**
 * datetime 2019/4/10 16:38
 *
 * @author sin5
 */
public class StandardMessagePusher<T> implements MessagePusher<T> {
    /**
     * gather receipt timeout
     */
    private long receiptTimeout;
    /**
     * executor service
     */
    private ExecutorService executorService;
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

    StandardMessagePusher(ExecutorService executorService,
                          PushManager<T> pushManager,
                          MessageRepository<T> messageRepository,
                          MessagePushLocker messagePushLocker,
                          MessageReceiptRepository messageReceiptRepository,
                          long receiptTimeout) {
        this.executorService = executorService;
        this.pushManager = pushManager;
        this.messageRepository = messageRepository;
        this.messagePushLocker = messagePushLocker;
        this.messageReceiptRepository = messageReceiptRepository;
        this.receiptTimeout = receiptTimeout;
    }

    @Override
    public void add(String receiver, T obj, boolean head) {
        add(receiver, obj, head, true);
    }

    @Override
    public void add(String receiver, T obj) {
        add(receiver, obj, false);
    }

    private void add(String receiver, T obj, boolean head, boolean startPush) {
        messageRepository.add(receiver, obj, head);
        if (startPush) {
            triggerPush(receiver);
        }
    }

    @Override
    public void reportReceipt(String receiver, String messageId) {
        messageReceiptRepository.storeReceipt(receiver, messageId);
        pushManager.onSuccess(receiver, messageId);
    }

    @Override
    public void triggerPush(String receiver) {
        executorService.execute(() -> pushContinuously(receiver, true));
    }

    @Override
    public void triggerPush(String receiver, boolean ordered) {
        executorService.execute(() -> pushContinuously(receiver, ordered));
    }

    @Override
    public void onInit() {
        pushManager.listReceivers().forEach(this::triggerPush);
    }

    private void pushContinuously(String receiver, boolean ordered) {
        if (messagePushLocker.tryLock(receiver)) {
            T t = messageRepository.take(receiver);
            while (t != null) {
                MessagePushTunnel tunnel = pushManager.determineTunnel(receiver, t);
                if (tunnel == null) {
                    break;
                } else {
                    if (ordered) {
                        pushAndGatherReceipt(receiver, t, tunnel);
                    } else {
                        T finalT = t;
                        executorService.execute(() -> pushAndGatherReceipt(receiver, finalT, tunnel));
                    }
                    pushAndGatherReceipt(receiver, t, tunnel);
                    t = messageRepository.take(receiver);
                }
            }
            messagePushLocker.unlock(receiver);
        }
    }

    private void pushAndGatherReceipt(String receiver, T t, MessagePushTunnel tunnel) {
        String msg = messageRepository.parse(receiver, t);
        String msgId = messageRepository.getMessageId(t);
        tunnel.push(receiver, msg);
        long start = System.currentTimeMillis();
        long now = start;
        while (now - start < receiptTimeout) {
            if (messageReceiptRepository.consumeReceipt(receiver, msgId)) {
                return;
            }
            try {
                Thread.sleep(100); // todo due to distributed reportReceipt, cannot notify instantly
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            now = System.currentTimeMillis();
        }
        add(receiver, t, true, false);
    }
}
