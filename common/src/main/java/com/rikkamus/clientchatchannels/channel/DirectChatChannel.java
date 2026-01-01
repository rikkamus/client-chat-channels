package com.rikkamus.clientchatchannels.channel;

import com.rikkamus.clientchatchannels.CancelableMessage;
import com.rikkamus.clientchatchannels.MessageColors;
import com.rikkamus.clientchatchannels.TextListUtil;
import com.rikkamus.clientchatchannels.indicator.ChannelIndicator;
import com.rikkamus.clientchatchannels.indicator.ChannelIndicatorTooltip;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.*;
import java.util.stream.Stream;

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
    public ChannelIndicator getChannelIndicator() {
        ChannelIndicatorTooltip tooltip = new ChannelIndicatorTooltip(
            List.of(Component.translatable("clientchatchannels.channel.direct.display_name").withStyle(MessageColors.INDICATOR_DIRECT)),
            List.of(getDisplayName().copy().withStyle(MessageColors.INDICATOR_DIRECT)),
            Stream.concat(
                Stream.of(Component.translatable("clientchatchannels.channel.direct.display_name").withStyle(MessageColors.INDICATOR_DIRECT)),
                getTooltipRecipientStream()
            ).toList()
        );

        return new ChannelIndicator(
            Component.translatable("clientchatchannels.channel.direct.indicator_short").withStyle(MessageColors.INDICATOR_DIRECT),
            Component.translatable("clientchatchannels.channel.direct.indicator_long").withStyle(MessageColors.INDICATOR_DIRECT),
            tooltip
        );
    }

    private Stream<Component> getTooltipRecipientStream() {
        if (this.recipients.isEmpty()) return Stream.of(Component.translatable("clientchatchannels.channel.direct.message.tooltip.no_recipients").withStyle(MessageColors.ERROR));
        else return this.recipients.stream().map(recipientName -> Component.literal(recipientName).withStyle(MessageColors.SUBTLE));
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
