package com.alvin.framework.multiend.message.push.pusher;

import com.alvin.framework.multiend.message.push.tunnel.DefaultTunnelFactory;

import java.util.concurrent.Executors;

/**
 * datetime 2019/4/28 17:06
 *
 * @author sin5
 */
public class StandardMessagePusherBuilder extends MessagePusherBuilder {

    public StandardMessagePusher build() {
        if (this.executorService == null) {
            this.executorService = Executors.newCachedThreadPool();
        }
        if (this.pushManager == null) {
            throw new IllegalArgumentException("pushManager must not be null");
        }
        if (this.messageRepository == null) {
            throw new IllegalArgumentException("messageRepository must not be null");
        }
        if (this.pushLocker == null) {
            throw new IllegalArgumentException("messagePushLock must not be null");
        }
        if (this.messageReceiptRepository == null) {
            throw new IllegalArgumentException("messageReceiptRepository must not be null");
        }
        if (this.tunnelFactory == null) {
            if (this.receiverIntegratedTunnelRepository == null) {
                throw new IllegalArgumentException("receiverIntegratedTunnelRepository must not be null");
            }
            this.tunnelFactory = new DefaultTunnelFactory(receiverIntegratedTunnelRepository);
        }
        return new StandardMessagePusher(
                this.executorService,
                this.pushManager,
                this.messageRepository,
                this.pushLocker,
                this.messageReceiptRepository,
                this.tunnelFactory,
                this.receiptTimeout);
    }

}
