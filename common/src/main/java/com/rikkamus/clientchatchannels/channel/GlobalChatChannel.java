package com.rikkamus.clientchatchannels.channel;

import com.rikkamus.clientchatchannels.CancelableMessage;
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

}
