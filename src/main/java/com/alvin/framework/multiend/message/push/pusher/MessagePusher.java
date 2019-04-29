package com.alvin.framework.multiend.message.push.pusher;

import com.alvin.framework.multiend.message.push.manager.PushScope;

/**
 * datetime 2019/4/10 16:29
 *
 * @author sin5
 */
public interface MessagePusher<T> {

    /**
     * add to queue
     *
     * @param receiver receiver
     * @param obj msg obj
     * @param head if add to head
     * @param scope push scope control
     * @param closedLoop if set true, repeatedly push this msg until receipt got checked, else push ignore receipt.
     */
    void add(String receiver, T obj, boolean head, PushScope scope, boolean closedLoop);

    /**
     * report receipt
     *
     * @param receiver receiver
     * @param tunnel receive msg from which tunnel
     * @param messageId message id
     */
    void reportReceipt(String receiver, String tunnel, String messageId);

    /**
     * start push process
     *
     * @param receiver receiver
     * @param ordered if push one by one with order. msgs set closedLoop false will not be ordered.
     */
    void triggerPush(String receiver, boolean ordered);

    /**
     * start push process
     *
     * @param receiver receiver
     * @param ordered if push one by one with order
     * @param tunnel trigger push on tunnel
     */
    void triggerPush(String receiver, boolean ordered, String tunnel);

    /**
     * run task after init
     */
    void onInit();
}
