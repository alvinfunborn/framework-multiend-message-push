package com.alvin.framework.multiend.message.push.pusher;

import com.alvin.framework.multiend.message.push.locker.MessagePushLocker;
import com.alvin.framework.multiend.message.push.manager.PushManager;
import com.alvin.framework.multiend.message.push.manager.PushScope;
import com.alvin.framework.multiend.message.push.manager.PushScopeEnum;
import com.alvin.framework.multiend.message.push.repository.MessageReceiptRepository;
import com.alvin.framework.multiend.message.push.repository.MessageRepository;
import com.alvin.framework.multiend.message.push.tunnel.MessagePushTunnel;
import com.alvin.framework.multiend.message.push.tunnel.MessagePushTunnelFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

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
    private PushManager pushManager;
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
    /**
     * tunnel factory
     */
    private MessagePushTunnelFactory messagePushTunnelFactory;

    StandardMessagePusher(ExecutorService executorService,
                          PushManager pushManager,
                          MessageRepository<T> messageRepository,
                          MessagePushLocker messagePushLocker,
                          MessageReceiptRepository messageReceiptRepository,
                          MessagePushTunnelFactory messagePushTunnelFactory,
                          long receiptTimeout) {
        this.executorService = executorService;
        this.pushManager = pushManager;
        this.messageRepository = messageRepository;
        this.messagePushLocker = messagePushLocker;
        this.messageReceiptRepository = messageReceiptRepository;
        this.messagePushTunnelFactory = messagePushTunnelFactory;
        this.receiptTimeout = receiptTimeout;
    }

    @Override
    public void add(String receiver, T obj, boolean head, PushScope scope, boolean closedLoop) {
        add(receiver, obj, head, true, scope);
    }

    private void add(String receiver, T obj, boolean head, boolean startPush, PushScope scope) {
        messageRepository.add(receiver, obj, head, scope);
        if (startPush) {
            triggerPush(receiver, true);
        }
    }

    @Override
    public void reportReceipt(String receiver, String messageId, String tunnel) {
        messageReceiptRepository.storeReceipt(receiver, messageId);
        pushManager.onSuccess(receiver, messageId, tunnel);
    }

    @Override
    public void triggerPush(String receiver, boolean ordered) {
        executorService.execute(() -> pushContinuously(receiver, ordered));
    }

    @Override
    public void triggerPush(String receiver, boolean ordered, String tunnel) {
        executorService.execute(() -> pushContinuously(receiver, tunnel, ordered));
    }

    @Override
    public void onInit() {
        pushManager.listReceivers().forEach(receiver -> triggerPush(receiver, true));
    }

    private void pushContinuously(String receiver, boolean ordered) {
        String lockKey = "message_pusher:first_connected_in_all:" + receiver;
        if (messagePushLocker.tryLock(lockKey)) {
            List<MessagePushTunnel> tunnels = messagePushTunnelFactory.listTunnels();
            while (true) {
                T t = messageRepository.takeFirstByReceiverAndScope(receiver, PushScopeEnum.firstConnectedTunnelInAllTunnels);
                if (t == null) {
                    break;
                }
                boolean connected = false;
                for (MessagePushTunnel tunnel : tunnels) {
                    if (tunnel.connected(receiver)) {
                        connected = true;
                        if (ordered) {
                            pushAndCheck(receiver, t, tunnel, ordered);
                        } else {
                            executorService.execute(() -> pushAndCheck(receiver, t, tunnel, ordered));
                        }
                        break;
                    }
                }
                if (!connected) {
                    break;
                }
            }
            messagePushLocker.unlock(lockKey);
        }
    }

    private void pushContinuously(String receiver, List<String> tunnels, boolean ordered) {
        String lockKey = "message_pusher:first_connected_in_specific:" + receiver + "_" + String.join(",", tunnels);
        if (messagePushLocker.tryLock(lockKey)) {
            List<MessagePushTunnel> messagePushTunnels = tunnels.stream().map(messagePushTunnelFactory::ofName).filter(Objects::nonNull).collect(Collectors.toList());
            while (true) {
                T t = messageRepository.takeFirstByReceiverAndTunnelsAndScope(receiver, tunnels, PushScopeEnum.firstConnectedTunnelInSpecificTunnels);
                if (t == null) {
                    break;
                }
                boolean connected = false;
                for (MessagePushTunnel tunnel : messagePushTunnels) {
                    if (tunnel.connected(receiver)) {
                        connected = true;
                        if (ordered) {
                            pushAndCheck(receiver, t, tunnel, ordered);
                        } else {
                            executorService.execute(() -> pushAndCheck(receiver, t, tunnel, ordered));
                        }
                        break;
                    }
                }
                if (!connected) {
                    break;
                }
            }
            messagePushLocker.unlock(lockKey);
        }
    }

    private void pushContinuously(String receiver, String tunnel, boolean ordered) {
        String lockKey = "message_pusher:multi_end:" + receiver + "_" + tunnel;
        if (messagePushLocker.tryLock(lockKey)) {
            while (true) {
                T t = messageRepository.takeFirstByReceiverAndScopeInAndTunnelNotPushed(receiver, PushScopeEnum.multiEndScopes(), tunnel);
                if (t == null) {
                    break;
                }
                MessagePushTunnel pushTunnel = messagePushTunnelFactory.ofName(tunnel);
                if (pushTunnel == null || !pushTunnel.connected(receiver)) {
                    break;
                } else {
                    if (ordered) {
                        pushAndCheck(receiver, t, pushTunnel, ordered);
                    } else {
                        executorService.execute(() -> pushAndCheck(receiver, t, pushTunnel, ordered));
                    }
                }
            }
            messagePushLocker.unlock(lockKey);
        }
    }

    private boolean pushAndCheck(String receiver, T t, MessagePushTunnel tunnel, boolean ordered) {
        String msg = messageRepository.parse(receiver, t);
        String msgId = messageRepository.getMessageId(t);
        tunnel.push(receiver, msg);
        if (!ordered) {
            return true;
        }
        long start = System.currentTimeMillis();
        long now = start;
        while (now - start < receiptTimeout) {
            if (messageReceiptRepository.consumeReceipt(receiver, msgId)) {
                return true;
            }
            try {
                Thread.sleep(100); // todo due to distributed reportReceipt, cannot notify instantly
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            now = System.currentTimeMillis();
        }
        add(receiver, t, true, false, messageRepository.getPushScope(t));
        return false;
    }
}
