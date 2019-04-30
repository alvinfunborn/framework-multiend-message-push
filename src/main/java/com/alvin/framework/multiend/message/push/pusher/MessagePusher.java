package com.alvin.framework.multiend.message.push.pusher;

import com.alvin.framework.multiend.message.push.model.Message;
import com.alvin.framework.multiend.message.push.service.Tunnel;
import com.alvin.framework.multiend.message.push.model.IntegratedTunnel;

/**
 * datetime 2019/4/10 16:29
 *
 * @author sin5
 */
public interface MessagePusher {

    /**
     * run task after init
     */
    void onInit();

    /**
     * run onConnect when tunnel connected
     *
     * @param receiver receiver
     * @param tunnel tunnel
     */
    void onConnect(String receiver, Tunnel tunnel);

    /**
     * push to queue. message will be pushed by this specific tunnel
     *
     * @param message message
     * @param tunnel tunnel that will push this msg
     * @param head if add to head
     */
    void addToTunnelQueue(Message message, Tunnel tunnel, boolean head);

    /**
     * push to queue. message will be pushed by first connected tunnel in tunnels
     *
     * @param message message
     * @param integratedTunnel tunnels
     * @param head if add to head
     */
    void addToTunnelGroupQueue(Message message, IntegratedTunnel integratedTunnel, boolean head);

    /**
     * report receipt
     *
     * @param receiver receiver
     * @param tunnel receive msg from which tunnel
     * @param messageId message id
     */
    void reportReceipt(String receiver, Tunnel tunnel, String messageId);
}
