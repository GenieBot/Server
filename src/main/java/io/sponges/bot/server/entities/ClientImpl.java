package io.sponges.bot.server.entities;

import io.netty.channel.Channel;
import io.sponges.bot.api.entities.Client;
import io.sponges.bot.api.entities.data.ClientData;
import io.sponges.bot.api.entities.manager.NetworkManager;
import io.sponges.bot.server.entities.data.ClientDataImpl;
import io.sponges.bot.server.entities.manager.NetworkManagerImpl;
import io.sponges.bot.server.protocol.msg.ChannelMessage;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * Implementation of the Client interface
 */
public class ClientImpl implements Client {

    private final String id;
    private final String defaultPrefix;
    private final Channel channel;
    private final NetworkManager networkManager;
    private final ClientData clientData;

    public ClientImpl(String id, String defaultPrefix, Channel channel) {
        this.id = id;
        this.defaultPrefix = defaultPrefix;
        this.channel = channel;
        this.networkManager = new NetworkManagerImpl(this);
        this.clientData = new ClientDataImpl();
    }

    public void write(String message) {
        channel.writeAndFlush(message + "\r\n");
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDefaultPrefix() {
        return defaultPrefix;
    }


    @Override
    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    @Override
    public ClientData getClientData() {
        return clientData;
    }

    @Override
    public void sendMessage(String s, Consumer<String> consumer) {
        new ChannelMessage(this, UUID.randomUUID().toString(), s, consumer, ChannelMessage.MessageType.REQUEST).send();
    }

    /**
     * Testing method that works within bsh interpreter
     * @param channel the channel to send the response in
     * @param s the message to send to the client
     */
    public void sendMessage(io.sponges.bot.api.entities.channel.Channel channel, String s) {
        new ChannelMessage(this, UUID.randomUUID().toString(), s, response -> {
            channel.sendChatMessage("Message response: " + response);
        }, ChannelMessage.MessageType.REQUEST);
    }
}
