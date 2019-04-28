package com.alvin.framework.multiend.message.push.manager;

/**
 * datetime 2019/4/28 17:38
 *
 * @author sin5
 */
public enum MessagePushScope {

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
}
