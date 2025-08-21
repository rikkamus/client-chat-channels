package com.rikkamus.clientchatchannels.fabric;

import com.rikkamus.clientchatchannels.CancelableMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimpleCancelableMessage implements CancelableMessage {

    private final String content;

    @Getter
    private boolean canceled = false;

    @Override
    public void cancel() {
        this.canceled = true;
    }

    @Override
    public String getContent() {
        return this.content;
    }

}
