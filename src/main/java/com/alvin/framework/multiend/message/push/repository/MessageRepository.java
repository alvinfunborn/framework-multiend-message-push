package com.alvin.framework.multiend.message.push.repository;

import com.alvin.framework.multiend.message.push.manager.PushScopeEnum;
import com.alvin.framework.multiend.message.push.message.Message;

import java.util.List;
import java.util.Set;

/**
 * datetime 2019/4/28 16:58
 *
 * @author sin5
 */
public interface MessageRepository {

    /**
     * push to queue
     *
     * @param obj msg obj
     * @param head if add to head
     */
    void add(Message obj, boolean head);

    /**
     * pop from queue. non block
     *
     * @param receiver receiver
     * @param scopeEnums scopes
     * @param tunnel tunnel which has not received receipt
     * @return obj
     */
    Message popByReceiverAndScopeInAndTunnelsContainAndTunnelNotPushed(String receiver, Set<PushScopeEnum> scopeEnums, String tunnel);

    /**
     * pop from queue. non block
     *
     * @param receiver receiver
     * @param tunnels tunnels
     * @param scopeEnum scope
     * @return obj
     */
    Message popByReceiverAndTunnelsAndScope(String receiver, List<String> tunnels, PushScopeEnum scopeEnum);

    /**
     * pop from queue. non block
     *
     * @param receiver receiver
     * @param scopeEnum scope
     * @return obj
     */
    Message popByReceiverAndScope(String receiver, PushScopeEnum scopeEnum);

    /**
     * get all from queue.
     *
     * @param receiver receiver
     * @param scopeEnum scope
     * @param tunnel tunnel
     * @return obj list
     */
    List<Message> findAllByReceiverAndScopeAndTunnelsContain(String receiver, PushScopeEnum scopeEnum, String tunnel);
}
