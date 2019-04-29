package com.alvin.framework.multiend.message.push.repository;

import com.alvin.framework.multiend.message.push.message.Message;
import com.alvin.framework.multiend.message.push.tunnel.Tunnel;
import com.alvin.framework.multiend.message.push.tunnel.IntegratedTunnel;

/**
 * datetime 2019/4/28 16:58
 *
 * @author sin5
 */
public interface MessageRepository {

    /**
     * push to queue. message will be pushed by this specific tunnel
     *
     * @param message message
     * @param tunnel tunnel that will push this msg
     * @param head if add to head
     */
    void addToTunnelQueue(Message message, Tunnel tunnel, boolean head);

    /**
     * push to queue. message will be pushed by first connected tunnel in specific tunnels
     *
     * @param message message
     * @param integratedTunnel tunnels
     * @param head if add to head
     */
    void addToIntegratedTunnelQueue(Message message, IntegratedTunnel integratedTunnel, boolean head);

    /**
     * pop from tunnel queue, non block
     *
     * @param receiver receiver
     * @param tunnel tunnel
     * @return message
     */
    Message popFromTunnelQueue(String receiver, Tunnel tunnel);

    /**
     * pop from tunnels queue, non block
     *
     * @param receiver receiver
     * @param integratedTunnel tunnels
     * @return message
     */
    Message popFromTunnelGroupQueue(String receiver, IntegratedTunnel integratedTunnel);
}
