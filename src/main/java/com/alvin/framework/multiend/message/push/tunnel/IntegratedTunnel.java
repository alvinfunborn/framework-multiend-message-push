package com.alvin.framework.multiend.message.push.tunnel;

import java.util.List;

/**
 * datetime 2019/4/29 14:33
 *
 * @author sin5
 */
public class IntegratedTunnel implements Tunnel{

    private List<Tunnel> tunnels;

    IntegratedTunnel(List<Tunnel> tunnels) {
        this.tunnels = tunnels;
    }

    public List<Tunnel> getTunnels() {
        return tunnels;
    }

    @Override
    public boolean connected(String receiver) {
        return connectedTunnel(receiver) != null;
    }

    private Tunnel connectedTunnel(String receiver) {
        for (Tunnel tunnel : tunnels) {
            if (tunnel.connected(receiver)) {
                return tunnel;
            }
        }
        return null;
    }

    @Override
    public void push(String receiver, String msg) {
        Tunnel tunnel = connectedTunnel(receiver);
        if (tunnel != null) {
            tunnel.push(receiver, msg);
        }
    }
}
