package com.rikkamus.clientchatchannels.indicator;

import net.minecraft.network.chat.Component;

public record ChannelIndicator(Component shortIndicator, Component longIndicator, ChannelIndicatorTooltip tooltip) {

}
