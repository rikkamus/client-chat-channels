package com.rikkamus.clientchatchannels.config;

import com.rikkamus.clientchatchannels.indicator.ChannelIndicatorTooltipType;
import com.rikkamus.clientchatchannels.indicator.ChannelIndicatorType;

public class DefaultConfig implements ClientChatChannelsConfig {

    public static final double DEFAULT_LOCAL_CHANNEL_RADIUS = 15;

    public static final ChannelIndicatorType DEFAULT_CHANNEL_INDICATOR_TYPE = ChannelIndicatorType.LONG;

    public static final ChannelIndicatorTooltipType DEFAULT_CHANNEL_INDICATOR_TOOLTIP_TYPE = ChannelIndicatorTooltipType.DETAILED;

    public static final boolean DEFAULT_CHANNEL_SWITCH_LOGGING_ENABLED = true;

    @Override
    public double getDefaultLocalChannelRadius() {
        return DefaultConfig.DEFAULT_LOCAL_CHANNEL_RADIUS;
    }

    @Override
    public ChannelIndicatorType getChannelIndicatorType() {
        return DefaultConfig.DEFAULT_CHANNEL_INDICATOR_TYPE;
    }

    @Override
    public ChannelIndicatorTooltipType getChannelIndicatorTooltipType() {
        return DefaultConfig.DEFAULT_CHANNEL_INDICATOR_TOOLTIP_TYPE;
    }

    @Override
    public boolean isChannelSwitchLoggingEnabled() {
        return DefaultConfig.DEFAULT_CHANNEL_SWITCH_LOGGING_ENABLED;
    }

}
