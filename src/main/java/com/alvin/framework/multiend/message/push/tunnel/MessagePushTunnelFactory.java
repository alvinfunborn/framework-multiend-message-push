package com.alvin.framework.multiend.message.push.tunnel;

import java.util.List;

/**
 * datetime 2019/4/28 19:48
 *
 * @author sin5
 */
public interface MessagePushTunnelFactory {

    /**
     * list all tunnels
     *
     * @return tunnel list
     */
    List<MessagePushTunnel> listTunnels();

    /**
     * get tunnel by name
     *
     * @param name name
     * @return tunnel
     */
    MessagePushTunnel ofName(String name);
}
