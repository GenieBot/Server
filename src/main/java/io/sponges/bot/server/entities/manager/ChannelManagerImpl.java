package io.sponges.bot.server.entities.manager;

import io.sponges.bot.api.entities.Network;
import io.sponges.bot.api.entities.User;
import io.sponges.bot.api.entities.channel.Channel;
import io.sponges.bot.api.entities.channel.GroupChannel;
import io.sponges.bot.api.entities.channel.PrivateChannel;
import io.sponges.bot.api.entities.manager.ChannelManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelManagerImpl implements ChannelManager {

    private final Map<String, Channel> channels = new HashMap<>();

    private final Network network;

    public ChannelManagerImpl(Network network) {
        this.network = network;
    }

    @Override
    public Network getNetwork() {
        return network;
    }

    @Override
    public Map<String, Channel> getChannels() {
        return channels;
    }

    @Override
    public boolean isChannel(String s) {
        return channels.containsKey(s);
    }

    @Override
    public Channel getChannel(String s) {
        return channels.get(s);
    }

    @Override
    public List<Channel> getChannels(User user) {
        List<Channel> channels = new ArrayList<>();
        String id = user.getId();
        for (Channel channel : this.channels.values()) {
            if (channel instanceof GroupChannel) {
                if (!((GroupChannel) channel).isUser(id)) {
                    continue;
                }
            } else {
                if (!((PrivateChannel) channel).getUser().getId().equals(id)) {
                    continue;
                }
            }
            channels.add(channel);
        }
        return channels;
    }
}