package com.rikkamus.clientchatchannels.indicator;

import net.minecraft.network.chat.Component;

import java.util.List;

public record ChannelIndicatorTooltip(List<Component> minimal, List<Component> simple, List<Component> detailed) {

    public static ChannelIndicatorTooltip simple(List<Component> components) {
        return new ChannelIndicatorTooltip(components, components, components);
    }

    public static ChannelIndicatorTooltip simple(Component component) {
        return ChannelIndicatorTooltip.simple(List.of(component));
    }

}
