package com.rikkamus.clientchatchannels.config;

public class DefaultConfig implements ClientChatChannelsConfig {

    public static final double DEFAULT_LOCAL_CHANNEL_RADIUS = 15;

    @Override
    public double getDefaultLocalChannelRadius() {
        return DefaultConfig.DEFAULT_LOCAL_CHANNEL_RADIUS;
    }

}
