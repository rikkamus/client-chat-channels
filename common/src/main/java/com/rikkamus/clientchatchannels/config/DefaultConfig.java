package com.rikkamus.clientchatchannels.config;

import com.rikkamus.clientchatchannels.indicator.ChannelIndicatorType;

public class DefaultConfig implements ClientChatChannelsConfig {

    public static final double DEFAULT_LOCAL_CHANNEL_RADIUS = 15;

    public static final ChannelIndicatorType DEFAULT_CHANNEL_INDICATOR_TYPE = ChannelIndicatorType.LONG;

    @Override
    public double getDefaultLocalChannelRadius() {
        return DefaultConfig.DEFAULT_LOCAL_CHANNEL_RADIUS;
    }

    @Override
    public ChannelIndicatorType getChannelIndicatorType() {
        return DefaultConfig.DEFAULT_CHANNEL_INDICATOR_TYPE;
    }

}
