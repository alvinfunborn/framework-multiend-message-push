package com.alvin.framework.multiend.message.push.manager;

import java.util.List;

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
    List<String> listReceivers();

    /**
     * on success of receiving msg from tunnel
     *
     * @param receiver receiver
     * @param messageId id
     * @param tunnel receive msg from which tunnel
     */
    void onSuccess(String receiver, String messageId, String tunnel);
}
