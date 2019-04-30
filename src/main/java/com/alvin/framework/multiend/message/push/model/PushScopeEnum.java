package com.alvin.framework.multiend.message.push.model;

import java.util.HashSet;
import java.util.Set;

/**
 * datetime 2019/4/28 17:38
 *
 * @author sin5
 */
public enum PushScopeEnum {

    /**
     * push to first connected tunnel in all tunnels
     */
    firstConnectedTunnelInAllTunnels,
    /**
     * push to first connected tunnel in specific tunnels
     */
    firstConnectedTunnelInSpecificTunnels,
    /**
     * push to all specific tunnels
     */
    specificTunnels,
    /**
     * push to all tunnels
     */
    allTunnels;

    public static Set<PushScopeEnum> noSpecificScopes() {
        Set<PushScopeEnum> result = new HashSet<>();
        result.add(allTunnels);
        result.add(firstConnectedTunnelInAllTunnels);
        return result;
    }

    public static Set<PushScopeEnum> multiEndScopes() {
        Set<PushScopeEnum> result = new HashSet<>();
        result.add(firstConnectedTunnelInAllTunnels);
        result.add(firstConnectedTunnelInSpecificTunnels);
        return result;
    }
}
