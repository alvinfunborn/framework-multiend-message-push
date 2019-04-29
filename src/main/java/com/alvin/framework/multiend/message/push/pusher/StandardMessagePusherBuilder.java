package com.alvin.framework.multiend.message.push.pusher;

import com.alvin.framework.multiend.message.push.tunnel.DefaultMessagePushTunnelFactory;

import java.util.concurrent.Executors;

/**
 * datetime 2019/4/28 17:06
 *
 * @author sin5
 */
public class StandardMessagePusherBuilder<T> extends MessagePusherBuilder<T> {

    public StandardMessagePusher<T> build() {
        if (this.executorService == null) {
            this.executorService = Executors.newCachedThreadPool();
        }
        if (this.pushManager == null) {
            throw new IllegalArgumentException("no bean of pushManager defined");
        }
        if (this.messageRepository == null) {
            throw new IllegalArgumentException("messageRepository must not be null");
        }
        if (this.messagePushLocker == null) {
            throw new IllegalArgumentException("no bean of messagePushLock defined");
        }
        if (this.messageReceiptRepository == null) {
            throw new IllegalArgumentException("no bean of messageReceiptRepository defined");
        }
        if (this.messagePushTunnelFactory == null) {
            this.messagePushTunnelFactory = new DefaultMessagePushTunnelFactory();
        }
        return new StandardMessagePusher<>(
                this.executorService,
                this.pushManager,
                this.messageRepository,
                this.messagePushLocker,
                this.messageReceiptRepository,
                this.messagePushTunnelFactory,
                this.receiptTimeout);
    }

}
