package io.sponges.bot.server.entities;

import io.netty.channel.Channel;
import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.manager.NetworkManager;
import io.sponges.bot.server.entities.manager.NetworkManagerImpl;

/**
 * Implementation of the Client interface
 */
public class ClientImpl implements Client {

    private final String id;
    private final Channel channel;
    private final NetworkManager networkManager;

    public ClientImpl(String id, Channel channel) {
        this.id = id;
        this.channel = channel;
        this.networkManager = new NetworkManagerImpl(this);
    }

    @Override
    public String getId() {
        return id;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public NetworkManager getNetworkManager() {
        return networkManager;
    }
}