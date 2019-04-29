package com.alvin.framework.multiend.message.push.repository;

import com.alvin.framework.multiend.message.push.manager.PushScope;
import com.alvin.framework.multiend.message.push.manager.PushScopeEnum;

import java.util.List;
import java.util.Set;

/**
 * datetime 2019/4/28 16:58
 *
 * @author sin5
 */
public interface MessageRepository<T> {

    /**
     * add to queue
     *
     * @param receiver receiver
     * @param obj msg obj
     * @param head if add to head
     */
    void add(String receiver, T obj, boolean head, PushScope scope);

    /**
     * take from queue. non block
     *
     * @param receiver receiver
     * @param scopeEnum scope
     * @param tunnel tunnel
     * @return obj
     */
    T takeFirstByReceiverAndScopeAndTunnelsContain(String receiver, PushScopeEnum scopeEnum, String tunnel);

    /**
     * take from queue. non block
     *
     * @param receiver receiver
     * @param scopeEnums scopes
     * @param tunnel tunnel which has not received receipt
     * @return obj
     */
    T takeFirstByReceiverAndScopeInAndTunnelNotPushed(String receiver, Set<PushScopeEnum> scopeEnums, String tunnel);

    /**
     * take from queue. non block
     *
     * @param receiver receiver
     * @param tunnels tunnels
     * @param scopeEnum scope
     * @return
     */
    T takeFirstByReceiverAndTunnelsAndScope(String receiver, List<String> tunnels, PushScopeEnum scopeEnum);

    /**
     * take from queue. non block
     *
     * @param receiver receiver
     * @param scopeEnums scopes
     * @return obj
     */
    T takeFirstByReceiverAndScopeIn(String receiver, Set<PushScopeEnum> scopeEnums);

    /**
     * take from queue. non block
     *
     * @param receiver receiver
     * @param scopeEnum scope
     * @return obj
     */
    T takeFirstByReceiverAndScope(String receiver, PushScopeEnum scopeEnum);

    /**
     * take from queue. non block
     *
     * @param receiver receiver
     * @return obj
     */
    T take(String receiver);

    /**
     * parse obj to str
     *
     * @param receiver receiver
     * @param obj msg obj
     * @return String
     */
    String parse(String receiver, T obj);

    /**
     * get message id from msg obj
     *
     * @param msg msg obj
     * @return message id
     */
    String getMessageId(T msg);

    /**
     * get push scope of msg obj
     *
     * @param msg msg obj
     * @return PushScope
     */
    PushScope getPushScope(T msg);
}
