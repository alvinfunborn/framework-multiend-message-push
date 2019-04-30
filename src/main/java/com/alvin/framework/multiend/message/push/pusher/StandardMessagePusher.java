package com.alvin.framework.multiend.message.push.pusher;

import com.alvin.framework.multiend.message.push.service.PushLocker;
import com.alvin.framework.multiend.message.push.model.Message;
import com.alvin.framework.multiend.message.push.service.MessageReceiptRepository;
import com.alvin.framework.multiend.message.push.service.MessageRepository;
import com.alvin.framework.multiend.message.push.service.Tunnel;
import com.alvin.framework.multiend.message.push.model.IntegratedTunnel;
import com.alvin.framework.multiend.message.push.service.TunnelRepository;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

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
     * message repository
     */
    private MessageRepository messageRepository;
    /**
     * push lock
     */
    private PushLocker pushLocker;
    /**
     * receipt repository
     */
    private MessageReceiptRepository messageReceiptRepository;
    /**
     * tunnel factory
     */
    private TunnelRepository tunnelRepository;

    StandardMessagePusher(ExecutorService executorService,
                          MessageRepository messageRepository,
                          PushLocker pushLocker,
                          MessageReceiptRepository messageReceiptRepository,
                          TunnelRepository tunnelRepository,
                          long receiptTimeout) {
        this.executorService = executorService;
        this.messageRepository = messageRepository;
        this.pushLocker = pushLocker;
        this.messageReceiptRepository = messageReceiptRepository;
        this.tunnelRepository = tunnelRepository;
        this.receiptTimeout = receiptTimeout;
    }

    @Override
    public void addToTunnelQueue(Message message, Tunnel tunnel, boolean head) {
        messageRepository.addToTunnelQueue(message, tunnel, head);
        tunnelRepository.register(tunnel);
        triggerPush(message.getReceiver(), tunnel);
    }

    @Override
    public void addToTunnelGroupQueue(Message message, IntegratedTunnel integratedTunnel, boolean head) {
        messageRepository.addToIntegratedTunnelQueue(message, integratedTunnel, head);
        tunnelRepository.register(integratedTunnel);
        triggerPush(message.getReceiver(), integratedTunnel);
    }

    @Override
    public void reportReceipt(String receiver, Tunnel tunnel, String messageId) {
        messageReceiptRepository.storeReceipt(receiver, messageId, tunnel);
    }

    @Override
    public void onInit() {
        Set<String> receivers = messageRepository.listReceiversOfMessagesInQueue();
        List<Tunnel> tunnels = tunnelRepository.findAllTunnel();
        receivers.forEach(receiver -> tunnels.forEach(tunnel -> {
            if (tunnel.connected(receiver)) {
                onConnect(receiver, tunnel);
            }
        }));
    }

    @Override
    public void onConnect(String receiver, Tunnel tunnel) {
        triggerPush(receiver, tunnel);
        List<IntegratedTunnel> integratedTunnels = tunnelRepository.findAllIntegratedTunnelContains(tunnel);
        integratedTunnels.forEach(integratedTunnel -> triggerPush(receiver, integratedTunnel));
    }

    private void triggerPush(String receiver, Tunnel tunnel) {
        executorService.execute(() -> pushContinuously(receiver, tunnel));
    }

    private void triggerPush(String receiver, IntegratedTunnel tunnelGroup) {
        executorService.execute(() -> pushContinuously(receiver, tunnelGroup));
    }

    private void pushContinuously(String receiver, IntegratedTunnel tunnelGroup) {
        String lockKey = "message_pusher:integrated_tunnel_" + tunnelGroup.hashCode() + ":" + receiver;
        if (pushLocker.tryLock(lockKey)) {
            while (true) {
                Message t = messageRepository.popFromTunnelGroupQueue(receiver, tunnelGroup);
                if (t == null) {
                    break;
                }
                if (!doIntegratedPush(receiver, t, tunnelGroup)) {
                    break;
                }
            }
            pushLocker.unlock(lockKey);
        }
    }

    private void pushContinuously(String receiver, Tunnel tunnel) {
        String lockKey = "message_pusher:tunnel_" + tunnel.hashCode() + ":" + receiver;
        if (pushLocker.tryLock(lockKey)) {
            while (true) {
                Message t = messageRepository.popFromTunnelQueue(receiver, tunnel);
                if (t == null) {
                    break;
                }
                boolean connected = tunnel.connected(receiver);
                if (connected) {
                    if (t.getPushOption().isOrdered()) {
                        doPush(receiver, t, tunnel, null);
                    } else {
                        executorService.execute(() -> doPush(receiver, t, tunnel, null));
                    }
                }
                if (t.getPushOption().isOneTimeAttempt()) {
                    connected = true;
                }
                if (!connected) {
                    break;
                }
            }
            pushLocker.unlock(lockKey);
        }
    }

    private boolean doIntegratedPush(String receiver, Message t, IntegratedTunnel integratedTunnel) {
        boolean connected = false;
        List<Tunnel> tunnels = integratedTunnel.getTunnels();
        for (Tunnel tunnel : tunnels) {
            if (tunnel.connected(receiver)) {
                connected = true;
                if (t.getPushOption().isOrdered()) {
                    doPush(receiver, t, tunnel, integratedTunnel);
                } else {
                    executorService.execute(() -> doPush(receiver, t, tunnel, integratedTunnel));
                }
                break;
            }
        }
        if (t.getPushOption().isOneTimeAttempt()) {
            connected = true;
        }
        return connected;
    }

    private void doPush(String receiver, Message t, Tunnel tunnel, IntegratedTunnel integratedTunnel) {
        boolean reliable = t.getPushOption().isReliable();
        String msgId = t.getMessageId();
        String msg = t.getData();
        tunnel.push(receiver, msg);
        if (!reliable) {
            messageRepository.onSuccess(receiver, msgId, tunnel);
            return;
        }
        long start = System.currentTimeMillis();
        long now = start;
        while (now - start < receiptTimeout) {
            if (messageReceiptRepository.consumeReceipt(receiver, msgId, tunnel)) {
                messageRepository.onSuccess(receiver, msgId, tunnel);
                return;
            }
            try {
                Thread.sleep(100); // todo due to distributed reportReceipt, cannot notify instantly
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            now = System.currentTimeMillis();
        }
        if (integratedTunnel != null) {
            addToTunnelGroupQueue(t, integratedTunnel, true);
        } else {
            addToTunnelQueue(t, tunnel, true);
        }
    }
}
