package com.rikkamus.clientchatchannels.channel;

import com.rikkamus.clientchatchannels.CancelableMessage;
import com.rikkamus.clientchatchannels.MessageColors;
import com.rikkamus.clientchatchannels.TextListUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.*;

@AllArgsConstructor
public class DirectChatChannel implements ChatChannel {

    @Getter
    @Setter
    private SequencedCollection<String> recipients;

    @Override
    public void interceptMessage(CancelableMessage message) {
        message.cancel();

        for (String recipientName : this.recipients) {
            Minecraft.getInstance().getConnection().sendCommand(String.format("msg %s %s", recipientName, message.getContent()));
        }
    }

    @Override
    public String getDisplayName() {
        if (this.recipients.size() == 1) return String.format("Direct (%s)", this.recipients.getFirst());
        else return String.format("Direct (%s recipients)", this.recipients.size());
    }

    @Override
    public Optional<Component> getStatus() {
        if (this.recipients.size() == 1) return Optional.empty();
        if (this.recipients.isEmpty()) return Optional.of(Component.literal("Nobody can see your messages.").withStyle(MessageColors.ERROR));

        return Optional.of(TextListUtil.renderTextList(
            Component.literal("Players that can see your messages:").withStyle(MessageColors.PRIMARY),
            this.recipients.stream().map(recipientName -> Component.literal(recipientName).withStyle(MessageColors.SECONDARY)),
            MessageColors.SUBTLE
        ));
    }

}
