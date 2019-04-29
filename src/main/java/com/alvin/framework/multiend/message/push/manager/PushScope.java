package com.alvin.framework.multiend.message.push.manager;

import java.util.List;

/**
 * datetime 2019/4/28 17:49
 *
 * @author sin5
 */
public class PushScope {

    /**
     * push scope
     */
    private PushScopeEnum scope;
    /**
     * tunnel list
     */
    private List<String> tunnels;

    public static PushScope ofAllTunnels(boolean onlyFirstConnected) {
        PushScope scope = new PushScope();
        if (onlyFirstConnected) {
            scope.setScope(PushScopeEnum.firstConnectedTunnelInAllTunnels);
        } else {
            scope.setScope(PushScopeEnum.allTunnels);
        }
        return scope;
    }

    public static PushScope ofSpecificTunnels(List<String> tunnels, boolean onlyFirstConnected) {
        PushScope scope = new PushScope();
        scope.setTunnels(tunnels);
        if (onlyFirstConnected) {
            scope.setScope(PushScopeEnum.firstConnectedTunnelInSpecificTunnels);
        } else {
            scope.setScope(PushScopeEnum.specificTunnels);
        }
        return scope;
    }

    public PushScopeEnum getScope() {
        return scope;
    }

    public void setScope(PushScopeEnum scope) {
        this.scope = scope;
    }

    public List<String> getTunnels() {
        return tunnels;
    }

    public void setTunnels(List<String> tunnels) {
        this.tunnels = tunnels;
    }
}
