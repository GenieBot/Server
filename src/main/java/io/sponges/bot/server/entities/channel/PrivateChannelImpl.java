package io.sponges.bot.server.entities.channel;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.PrivateChannel;
import io.sponges.bot.api.storage.data.ChannelData;
import io.sponges.bot.server.entities.ClientImpl;
import io.sponges.bot.server.protocol.msg.SendRawMessage;

public class PrivateChannelImpl implements PrivateChannel {

    private final String id;
    private final Network network;
    private final User user;

    private ChannelData channelData = null;

    public PrivateChannelImpl(String id, Network network, User user) {
        this.id = id;
        this.network = network;
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void sendMessage(String message) {
        ClientImpl client = (ClientImpl) network.getClient();
        client.getChannel().writeAndFlush(message + "\r\n");
    }

    @Override
    public void sendChatMessage(String s) {
        String message = new SendRawMessage(network.getClient(), network, this, s).toString();
        sendMessage(message);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Network getNetwork() {
        return network;
    }

    @Override
    public ChannelData getData() {
        return channelData;
    }

    public void setChannelData(ChannelData channelData) {
        this.channelData = channelData;
    }
}
