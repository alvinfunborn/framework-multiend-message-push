package com.alvin.framework.multiend.message.push.model;

import com.alvin.framework.multiend.message.push.service.Tunnel;

import java.util.ArrayList;
import java.util.List;

/**
 * datetime 2019/4/29 14:33
 *
 * @author sin5
 */
public class IntegratedTunnel{

    private List<Tunnel> tunnels;

    public IntegratedTunnel(List<Tunnel> tunnels) {
        this.tunnels = new ArrayList<>(tunnels);
    }

    public List<Tunnel> getTunnels() {
        return tunnels;
    }

    public boolean contains(Tunnel tunnel) {
        return tunnels.contains(tunnel);
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        int size = tunnels.size();
        for (int i = 0; i < size; i++) {
            hashCode += tunnels.get(i).hashCode() + i;
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || getClass() != obj.getClass()) {
            return false;
        }
        IntegratedTunnel o = (IntegratedTunnel) obj;
        int thisSize = this.tunnels.size();
        int size = o.tunnels.size();
        if (thisSize != size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (this.tunnels.get(i).hashCode() != o.tunnels.get(i).hashCode()) {
                return false;
            }
        }
        return true;
    }
}
