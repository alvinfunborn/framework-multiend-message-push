package com.alvin.framework.multiend.message.push.service;

import com.alvin.framework.multiend.message.push.model.IntegratedTunnel;

import java.util.List;

/**
 * datetime 2019/4/29 16:43
 *
 * @author sin5
 */
public interface TunnelFactory {

    String getNameOfTunnel(Tunnel tunnel);

    String getNameOfTunnel(IntegratedTunnel integratedTunnel);

    Tunnel getTunnelByName(String name);

    IntegratedTunnel getIntegratedTunnelByName(String name);

    List<IntegratedTunnel> findAllIntegratedTunnel();

    List<Tunnel> findAllTunnel();
}
