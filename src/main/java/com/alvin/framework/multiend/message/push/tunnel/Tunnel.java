package com.alvin.framework.multiend.message.push.tunnel;

/**
 * datetime 2019/4/4 20:14
 *
 *
 * @author sin5
 */
public interface Tunnel{

    /**
     * if receiver connected to this tunnel
     *
     * @param receiver receiver
     * @return true if connected
     */
    boolean connected(String receiver);

    /**
     * push msg
     *
     * @param receiver receiver
     * @param msg msg
     */
    void push(String receiver, String msg);
}
