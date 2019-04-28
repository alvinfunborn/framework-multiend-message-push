package com.alvin.framework.multiend.message.push.pusher;

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
     */
    void add(String receiver, T obj, boolean head);

    /**
     * add to queue
     *
     * @param receiver receiver
     * @param obj msg obj
     */
    void add(String receiver, T obj);

    /**
     * report receipt
     *
     * @param receiver receiver
     * @param messageId message id
     */
    void reportReceipt(String receiver, String messageId);

    /**
     * start push process
     * default ordered
     *
     * @param receiver receiver
     */
    void triggerPush(String receiver);

    /**
     * start push process
     *
     * @param receiver receiver
     * @param ordered if push one by one with order
     */
    void triggerPush(String receiver, boolean ordered);

    /**
     * run task after init
     */
    void onInit();
}
