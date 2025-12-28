package com.rikkamus.clientchatchannels.channel;

import com.rikkamus.clientchatchannels.CancelableMessage;
import com.rikkamus.clientchatchannels.indicator.ChannelIndicator;
import net.minecraft.network.chat.Component;

import java.util.Optional;

public interface ChatChannel {

    void interceptMessage(CancelableMessage message);

    Component getDisplayName();

    ChannelIndicator getChannelIndicator();

    default Optional<Component> getStatus() {
        return Optional.empty();
    }

}
