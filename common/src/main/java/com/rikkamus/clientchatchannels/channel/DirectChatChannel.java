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
    public Component getDisplayName() {
        if (this.recipients.size() == 1) return Component.translatable("clientchatchannels.channel.direct.display_name_recipient", this.recipients.getFirst());
        else return Component.translatable("clientchatchannels.channel.direct.display_name_recipient_count", this.recipients.size());
    }

    @Override
    public Optional<Component> getStatus() {
        if (this.recipients.size() == 1) return Optional.empty();
        if (this.recipients.isEmpty()) return Optional.of(Component.translatable("clientchatchannels.channel.direct.message.status.no_recipients").withStyle(MessageColors.ERROR));

        return Optional.of(TextListUtil.renderTextList(
            Component.translatable("clientchatchannels.channel.direct.status_header").withStyle(MessageColors.PRIMARY),
            this.recipients.stream().map(recipientName -> Component.literal(recipientName).withStyle(MessageColors.SECONDARY)),
            MessageColors.SUBTLE
        ));
    }

}
