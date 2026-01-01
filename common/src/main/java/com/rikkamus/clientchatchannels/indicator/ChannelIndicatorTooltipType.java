package com.rikkamus.clientchatchannels.indicator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.network.chat.Component;

import java.util.List;

@RequiredArgsConstructor
public enum ChannelIndicatorTooltipType {

    NONE(false) {

        @Override
        public List<Component> selectTooltipComponents(ChannelIndicator indicator) {
            return List.of();
        }

    },
    MINIMAL(true) {

        @Override
        public List<Component> selectTooltipComponents(ChannelIndicator indicator) {
            return indicator.tooltip().minimal();
        }

    },
    SIMPLE(true) {

        @Override
        public List<Component> selectTooltipComponents(ChannelIndicator indicator) {
            return indicator.tooltip().simple();
        }

    },
    DETAILED(true) {

        @Override
        public List<Component> selectTooltipComponents(ChannelIndicator indicator) {
            return indicator.tooltip().detailed();
        }

    };

    @Getter
    private final boolean enabled;

    public abstract List<Component> selectTooltipComponents(ChannelIndicator indicator);

}
