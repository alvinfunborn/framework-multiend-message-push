package com.alvin.framework.multiend.message.push.service;

import com.alvin.framework.multiend.message.push.model.IntegratedTunnel;

import java.util.List;

/**
 * datetime 2019/4/29 16:43
 *
 * @author sin5
 */
public interface TunnelRepository {

    void register(IntegratedTunnel integratedTunnel);

    void register(Tunnel tunnel);

    List<IntegratedTunnel> findAllIntegratedTunnelContains(Tunnel tunnel);

    List<Tunnel> findAllTunnel();
}
