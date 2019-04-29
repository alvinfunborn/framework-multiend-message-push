package com.alvin.framework.multiend.message.push.manager;

import com.alvin.framework.multiend.message.push.tunnel.Tunnel;

import java.util.Set;

/**
 * datetime 2019/4/28 16:59
 *
 * @author sin5
 */
public interface PushManager {

    /**
     * get all receivers about to receiving messages
     *
     * @return list of receivers
     */
    Set<String> listReceiversOfMessagesInQueue();

    /**
     * on success of receiving msg from tunnel
     *
     * @param receiver receiver
     * @param messageId id
     * @param tunnel receive msg from which tunnel
     */
    void onSuccess(String receiver, String messageId, Tunnel tunnel);
}
