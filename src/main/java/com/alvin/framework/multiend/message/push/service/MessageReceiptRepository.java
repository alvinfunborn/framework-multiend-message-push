package com.alvin.framework.multiend.message.push.service;

/**
 * datetime 2019/4/10 17:31
 *
 * @author sin5
 */
public interface MessageReceiptRepository {

    /**
     * store message receipt
     *
     * @param receiver receiver
     * @param messageId msg id
     */
    void storeReceipt(String receiver, String messageId, Tunnel tunnel);

    /**
     * check if message received
     *
     * @param receiver receiver
     * @param messageId msg id
     * @return true on message received
     */
    boolean consumeReceipt(String receiver, String messageId, Tunnel tunnel);

    /**
     * on success of receiving msg from tunnel
     *
     * @param receiver receiver
     * @param messageId id
     * @param tunnel receive msg from which tunnel
     */
    void onSuccess(String receiver, String messageId, Tunnel tunnel);
}
