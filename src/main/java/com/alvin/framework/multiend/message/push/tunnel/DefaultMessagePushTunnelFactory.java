package com.alvin.framework.multiend.message.push.tunnel;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * datetime 2019/4/28 19:47
 *
 * @author sin5
 */
public class DefaultMessagePushTunnelFactory implements MessagePushTunnelFactory {

    @Override
    public List<MessagePushTunnel> listTunnels() {
        List<MessagePushTunnel> tunnelList = new ArrayList<>();
        ServiceLoader<MessagePushTunnel> loader = ServiceLoader.load(MessagePushTunnel.class);
        loader.iterator().forEachRemaining(tunnelList::add);
        return tunnelList;
    }

    @Override
    public List<String> listTunnelNames() {
        return listTunnels().stream().map(MessagePushTunnel::name).collect(Collectors.toList());
    }

    @Override
    public MessagePushTunnel ofName(String name) {
        if (name == null) {
            return null;
        }
        List<MessagePushTunnel> tunnelList = listTunnels();
        for (MessagePushTunnel tunnel : tunnelList) {
            if (tunnel.name().equals(name)) {
                return tunnel;
            }
        }
        return null;
    }
}
