package com.alvin.framework.multiend.message.push.model;

import com.alvin.framework.multiend.message.push.service.Tunnel;

import java.util.List;

/**
 * datetime 2019/4/29 14:33
 *
 * @author sin5
 */
public class IntegratedTunnel{

    private List<Tunnel> tunnels;

    public IntegratedTunnel(List<Tunnel> tunnels) {
        this.tunnels = tunnels;
    }

    public List<Tunnel> getTunnels() {
        return tunnels;
    }
}
