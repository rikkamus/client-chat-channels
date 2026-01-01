package com.rikkamus.clientchatchannels.config;

import com.rikkamus.clientchatchannels.indicator.ChannelIndicatorTooltipType;
import com.rikkamus.clientchatchannels.indicator.ChannelIndicatorType;

public interface ClientChatChannelsConfig {

    double getDefaultLocalChannelRadius();

    ChannelIndicatorType getChannelIndicatorType();

    ChannelIndicatorTooltipType getChannelIndicatorTooltipType();

    boolean isChannelSwitchLoggingEnabled();

}
