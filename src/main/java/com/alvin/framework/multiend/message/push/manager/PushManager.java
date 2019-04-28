package com.alvin.framework.multiend.message.push.manager;

import com.alvin.framework.multiend.message.push.tunnel.MessagePushTunnel;

import java.util.List;

/**
 * datetime 2019/4/28 16:59
 *
 * @author sin5
 */
public interface PushManager<T> {

    /**
     * get all receivers about to receiving messages
     *
     * @return list of receivers
     */
    List<String> listReceivers();

    /**
     * determine which tunnel to use
     *
     * @param receiver receiver
     * @param msg obj
     * @return tunnel
     */
    MessagePushTunnel determineTunnel(String receiver, T msg);

    /**
     * on success of receiving msg
     *
     * @param receiver receiver
     * @param messageId id
     */
    void onSuccess(String receiver, String messageId);
}
