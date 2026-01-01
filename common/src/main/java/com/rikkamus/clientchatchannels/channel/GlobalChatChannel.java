package com.rikkamus.clientchatchannels.channel;

import com.rikkamus.clientchatchannels.CancelableMessage;
import com.rikkamus.clientchatchannels.MessageColors;
import com.rikkamus.clientchatchannels.indicator.ChannelIndicator;
import com.rikkamus.clientchatchannels.indicator.ChannelIndicatorTooltip;
import net.minecraft.network.chat.Component;

public class GlobalChatChannel implements ChatChannel {

    @Override
    public void interceptMessage(CancelableMessage message) {
        // Do nothing
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("clientchatchannels.channel.global.display_name");
    }

    @Override
    public ChannelIndicator getChannelIndicator() {
        return new ChannelIndicator(
            Component.translatable("clientchatchannels.channel.global.indicator_short").withStyle(MessageColors.INDICATOR_GLOBAL),
            Component.translatable("clientchatchannels.channel.global.indicator_long").withStyle(MessageColors.INDICATOR_GLOBAL),
            ChannelIndicatorTooltip.simple(getDisplayName().copy().withStyle(MessageColors.INDICATOR_GLOBAL))
        );
    }

}
