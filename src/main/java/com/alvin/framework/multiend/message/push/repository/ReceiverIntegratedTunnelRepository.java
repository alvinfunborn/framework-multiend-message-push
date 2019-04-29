package com.alvin.framework.multiend.message.push.repository;

import com.alvin.framework.multiend.message.push.tunnel.IntegratedTunnel;
import com.alvin.framework.multiend.message.push.tunnel.Tunnel;

import java.util.List;

/**
 * datetime 2019/4/29 16:43
 *
 * @author sin5
 */
public interface ReceiverIntegratedTunnelRepository {

    void save(String receiver, IntegratedTunnel integratedTunnel);

    List<IntegratedTunnel> findAllByReceiverAndIntegratedTunnelContains(String receiver, Tunnel tunnel);
}
