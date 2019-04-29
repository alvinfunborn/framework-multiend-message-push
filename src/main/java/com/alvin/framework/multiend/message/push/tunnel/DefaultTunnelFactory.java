package com.alvin.framework.multiend.message.push.tunnel;

import com.alvin.framework.multiend.message.push.repository.ReceiverIntegratedTunnelRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * datetime 2019/4/28 19:47
 *
 * @author sin5
 */
public class DefaultTunnelFactory implements TunnelFactory {

    private ReceiverIntegratedTunnelRepository receiverIntegratedTunnelRepository;

    public DefaultTunnelFactory(ReceiverIntegratedTunnelRepository receiverIntegratedTunnelRepository) {
        this.receiverIntegratedTunnelRepository = receiverIntegratedTunnelRepository;
    }

    @Override
    public List<Tunnel> listTunnels() {
        List<Tunnel> tunnelList = new ArrayList<>();
        ServiceLoader<Tunnel> loader = ServiceLoader.load(Tunnel.class);
        loader.iterator().forEachRemaining(tunnelList::add);
        return tunnelList;
    }

    @Override
    public IntegratedTunnel integrateTunnel(String receiver, List<Tunnel> tunnels) {
        IntegratedTunnel integratedTunnel = new IntegratedTunnel(tunnels);
        receiverIntegratedTunnelRepository.save(receiver, integratedTunnel);
        return integratedTunnel;
    }

    @Override
    public List<IntegratedTunnel> findAllIntegratedTunnelsContainTunnel(String receiver, Tunnel tunnel) {
        return receiverIntegratedTunnelRepository.findAllByReceiverAndIntegratedTunnelContains(receiver, tunnel);
    }
}
