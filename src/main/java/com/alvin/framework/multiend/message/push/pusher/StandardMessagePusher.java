package com.alvin.framework.multiend.message.push.pusher;

import com.alvin.framework.multiend.message.push.locker.MessagePushLocker;
import com.alvin.framework.multiend.message.push.manager.PushManager;
import com.alvin.framework.multiend.message.push.manager.PushScope;
import com.alvin.framework.multiend.message.push.manager.PushScopeEnum;
import com.alvin.framework.multiend.message.push.message.Message;
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
public class StandardMessagePusher implements MessagePusher {
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
    private MessageRepository messageRepository;
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
                          MessageRepository messageRepository,
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
    public void add(Message message, boolean head) {
        add(message, head, true);
    }

    private void add( Message obj, boolean head, boolean startPush) {
        messageRepository.add(obj, head);
        if (startPush) {
            String receiver = obj.getReceiver();
            PushScope scope = obj.getPushOption().getPushScope();
            switch (scope.getScope()) {
                case firstConnectedTunnelInAllTunnels: triggerPush(receiver); break;
                case firstConnectedTunnelInSpecificTunnels: triggerPush(receiver, scope.getTunnels()); break;
                case specificTunnels: scope.getTunnels().forEach(tunnel -> triggerPush(receiver, tunnel)); break;
                case allTunnels: messagePushTunnelFactory.listTunnelNames().forEach(tunnel -> triggerPush(receiver, tunnel)); break;
            }
            triggerPush(receiver);
        }
    }

    @Override
    public void reportReceipt(String receiver, String messageId, String tunnel) {
        messageReceiptRepository.storeReceipt(receiver, messageId, tunnel);
        pushManager.onSuccess(receiver, messageId, tunnel);
    }

    @Override
    public void onConnect(String receiver, String tunnel) {
        triggerPush(receiver);
        triggerPush(receiver, tunnel);
        List<Message> messages = messageRepository.findAllByReceiverAndScopeAndTunnelsContain(receiver, PushScopeEnum.firstConnectedTunnelInSpecificTunnels, tunnel);
        messages.forEach(message -> triggerPush(receiver, message.getPushOption().getPushScope().getTunnels()));
    }

    @Override
    public void onInit() {
        List<String> receivers = pushManager.listReceivers();
        List<MessagePushTunnel> tunnels = messagePushTunnelFactory.listTunnels();
        receivers.forEach(receiver -> tunnels.forEach(tunnel -> {
            if (tunnel.connected(receiver)) {
                onConnect(receiver, tunnel.name());
            }
        }));
    }

    private void triggerPush(String receiver) {
        executorService.execute(() -> pushContinuously(receiver));
    }

    private void triggerPush(String receiver, String tunnel) {
        executorService.execute(() -> pushContinuously(receiver, tunnel));
    }

    private void triggerPush(String receiver, List<String> tunnels) {
        executorService.execute(() -> pushContinuously(receiver, tunnels));
    }

    private void pushContinuously(String receiver) {
        String lockKey = "message_pusher:first_connected_in_all:" + receiver;
        if (messagePushLocker.tryLock(lockKey)) {
            List<MessagePushTunnel> tunnels = messagePushTunnelFactory.listTunnels();
            while (true) {
                Message t = messageRepository.popByReceiverAndScope(receiver, PushScopeEnum.firstConnectedTunnelInAllTunnels);
                if (t == null) {
                    break;
                }
                if (!pushAndBreak(receiver, t, tunnels)) {
                    break;
                }
            }
            messagePushLocker.unlock(lockKey);
        }
    }

    private void pushContinuously(String receiver, List<String> tunnels) {
        String lockKey = "message_pusher:first_connected_in_specific:" + receiver + "_" + String.join(",", tunnels);
        if (messagePushLocker.tryLock(lockKey)) {
            List<MessagePushTunnel> messagePushTunnels = tunnels.stream().map(messagePushTunnelFactory::ofName).filter(Objects::nonNull).collect(Collectors.toList());
            while (true) {
                Message t = messageRepository.popByReceiverAndTunnelsAndScope(receiver, tunnels, PushScopeEnum.firstConnectedTunnelInSpecificTunnels);
                if (t == null) {
                    break;
                }
                if (!pushAndBreak(receiver, t, messagePushTunnels)) {
                    break;
                }
            }
            messagePushLocker.unlock(lockKey);
        }
    }

    private void pushContinuously(String receiver, String tunnel) {
        String lockKey = "message_pusher:multi_end:" + receiver + "_" + tunnel;
        if (messagePushLocker.tryLock(lockKey)) {
            while (true) {
                Message t = messageRepository.popByReceiverAndScopeInAndTunnelsContainAndTunnelNotPushed(receiver, PushScopeEnum.multiEndScopes(), tunnel);
                if (t == null) {
                    break;
                }
                MessagePushTunnel pushTunnel = messagePushTunnelFactory.ofName(tunnel);
                if (pushTunnel == null || !pushTunnel.connected(receiver)) {
                    break;
                } else {
                    if (t.getPushOption().isOrdered()) {
                        doPush(receiver, t, pushTunnel);
                    } else {
                        executorService.execute(() -> doPush(receiver, t, pushTunnel));
                    }
                }
            }
            messagePushLocker.unlock(lockKey);
        }
    }

    private boolean pushAndBreak(String receiver, Message t, List<MessagePushTunnel> tunnels) {
        boolean connected = false;
        for (MessagePushTunnel tunnel : tunnels) {
            if (tunnel.connected(receiver)) {
                connected = true;
                if (t.getPushOption().isOrdered()) {
                    doPush(receiver, t, tunnel);
                } else {
                    executorService.execute(() -> doPush(receiver, t, tunnel));
                }
                break;
            }
        }
        return connected;
    }

    private void doPush(String receiver, Message t, MessagePushTunnel tunnel) {
        boolean closedLoop = t.getPushOption().isClosedLoop();
        String msgId = t.getMessageId();
        String msg = t.getData();
        tunnel.push(receiver, msg);
        if (!closedLoop) {
            return;
        }
        long start = System.currentTimeMillis();
        long now = start;
        while (now - start < receiptTimeout) {
            if (messageReceiptRepository.consumeReceipt(receiver, msgId, tunnel.name())) {
                return;
            }
            try {
                Thread.sleep(100); // todo due to distributed reportReceipt, cannot notify instantly
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            now = System.currentTimeMillis();
        }
        add(t, true, false);
    }
}
