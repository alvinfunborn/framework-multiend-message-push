package com.alvin.framework.multiend.message.push.pusher;

import com.alvin.framework.multiend.message.push.message.Message;

/**
 * datetime 2019/4/10 16:29
 *
 * @author sin5
 */
public interface MessagePusher {

    /**
     * run task after init
     */
    void onInit();

    /**
     * run onConnect when tunnel connected
     *
     * @param receiver receiver
     * @param tunnel tunnel
     */
    void onConnect(String receiver, String tunnel);

    /**
     * add to queue
     *
     * @param message message obj
     * @param head if add to head
     */
    void add(Message message, boolean head);

    /**
     * report receipt
     *
     * @param receiver receiver
     * @param tunnel receive msg from which tunnel
     * @param messageId message id
     */
    void reportReceipt(String receiver, String tunnel, String messageId);
}
