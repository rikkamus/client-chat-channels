package com.rikkamus.clientchatchannels.channel;

import com.rikkamus.clientchatchannels.CancelableMessage;

public class GlobalChatChannel implements ChatChannel {

    @Override
    public void interceptMessage(CancelableMessage message) {
        // Do nothing
    }

    @Override
    public String getDisplayName() {
        return "Global";
    }

}
