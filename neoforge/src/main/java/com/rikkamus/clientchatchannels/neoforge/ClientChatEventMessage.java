package com.rikkamus.clientchatchannels.neoforge;

import com.rikkamus.clientchatchannels.CancelableMessage;
import lombok.RequiredArgsConstructor;
import net.neoforged.neoforge.client.event.ClientChatEvent;

@RequiredArgsConstructor
public class ClientChatEventMessage implements CancelableMessage {

    private final ClientChatEvent event;

    @Override
    public void cancel() {
        this.event.setCanceled(true);
    }

    @Override
    public String getContent() {
        return this.event.getMessage();
    }

}
