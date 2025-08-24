package com.rikkamus.clientchatchannels.channel;

import com.rikkamus.clientchatchannels.CancelableMessage;
import net.minecraft.network.chat.Component;

import java.util.Optional;

public interface ChatChannel {

    void interceptMessage(CancelableMessage message);

    Component getDisplayName();

    default Optional<Component> getStatus() {
        return Optional.empty();
    }

}
