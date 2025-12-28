package com.rikkamus.clientchatchannels.indicator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.network.chat.Component;

@RequiredArgsConstructor
public enum ChannelIndicatorType {

    NONE(false) {

        @Override
        public Component selectIndicatorComponent(ChannelIndicator indicator) {
            return Component.empty();
        }

    },
    SHORT(true) {

        @Override
        public Component selectIndicatorComponent(ChannelIndicator indicator) {
            return indicator.shortIndicator();
        }

    },
    LONG(true) {

        @Override
        public Component selectIndicatorComponent(ChannelIndicator indicator) {
            return indicator.longIndicator();
        }

    };

    @Getter
    private final boolean enabled;

    public abstract Component selectIndicatorComponent(ChannelIndicator indicator);

}
