package com.alvin.framework.multiend.message.push.tunnel;

import java.util.List;

/**
 * datetime 2019/4/28 19:48
 *
 * @author sin5
 */
public interface TunnelFactory {

    /**
     * list all tunnels
     *
     * @return tunnel list
     */
    List<Tunnel> listTunnels();

    /**
     * create integratedTunnel for receiver
     *
     * @param receiver receiver
     * @param tunnels tunnels
     * @return integratedTunnel
     */
    IntegratedTunnel integrateTunnel(String receiver, List<Tunnel> tunnels);

    /**
     * list all integratedTunnels contain tunnel
     *
     * @param receiver receiver
     * @param tunnel tunnel
     * @return integratedTunnel list
     */
    List<IntegratedTunnel> findAllIntegratedTunnelsContainTunnel(String receiver, Tunnel tunnel);
}
