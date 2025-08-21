package com.rikkamus.clientchatchannels.channel;

import com.rikkamus.clientchatchannels.CancelableMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class DirectChatChannel implements ChatChannel {

    @Getter
    @Setter
    @NotNull
    private String recipientName;

    @Override
    public void interceptMessage(CancelableMessage message) {
        message.cancel();
        Minecraft.getInstance().getConnection().sendCommand(String.format("msg %s %s", this.recipientName, message.getContent()));
    }

    @Override
    public String getDisplayName() {
        return String.format("Direct (%s)", this.recipientName);
    }

}
