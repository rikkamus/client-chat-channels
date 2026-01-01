package com.rikkamus.clientchatchannels.channel;

import com.rikkamus.clientchatchannels.*;
import com.rikkamus.clientchatchannels.config.ConfigValueSupplier;
import com.rikkamus.clientchatchannels.indicator.ChannelIndicator;
import com.rikkamus.clientchatchannels.indicator.ChannelIndicatorTooltip;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@AllArgsConstructor
public class LocalChatChannel implements ChatChannel {

    @Getter
    @Setter
    private ConfigValueSupplier<Double> radiusSupplier;

    @Override
    public void interceptMessage(CancelableMessage message) {
        message.cancel();

        Set<String> recipients = LocalPlayerUtil.getNamesOfPlayersWithinRadius(this.radiusSupplier.get());

        if (recipients.isEmpty()) {
            ChatLogger.logTranslatable("clientchatchannels.channel.local.message.intercept.no_recipients", MessageColors.ERROR);
            return;
        }

        for (String recipientName : recipients) {
            Minecraft.getInstance().getConnection().sendCommand(String.format("msg %s %s", recipientName, message.getContent()));
        }
    }

    @Override
    public Component getDisplayName() {
        if (this.radiusSupplier.isUsingConfigValue()) return Component.translatable("clientchatchannels.channel.local.display_name_default_radius");
        else return Component.translatable("clientchatchannels.channel.local.display_name_custom_radius", this.radiusSupplier.get());
    }

    @Override
    public ChannelIndicator getChannelIndicator() {
        ChannelIndicatorTooltip tooltip = new ChannelIndicatorTooltip(
            List.of(Component.translatable("clientchatchannels.channel.local.display_name").withStyle(MessageColors.INDICATOR_LOCAL)),
            List.of(getDisplayName().copy().withStyle(MessageColors.INDICATOR_LOCAL)),
            Stream.concat(
                Stream.of(getDisplayName().copy().withStyle(MessageColors.INDICATOR_LOCAL)),
                getTooltipRecipientStream()
            ).toList()
        );

        return new ChannelIndicator(
            Component.translatable("clientchatchannels.channel.local.indicator_short").withStyle(MessageColors.INDICATOR_LOCAL),
            Component.translatable("clientchatchannels.channel.local.indicator_long").withStyle(MessageColors.INDICATOR_LOCAL),
            tooltip
        );
    }

    private Stream<Component> getTooltipRecipientStream() {
        Set<String> recipients = LocalPlayerUtil.getNamesOfPlayersWithinRadius(this.radiusSupplier.get());

        if (recipients.isEmpty()) return Stream.of(Component.translatable("clientchatchannels.channel.local.message.tooltip.no_recipients").withStyle(MessageColors.ERROR));
        else return recipients.stream().map(recipientName -> Component.literal(recipientName).withStyle(MessageColors.SUBTLE));
    }

    @Override
    public Optional<Component> getStatus() {
        Set<String> recipients = LocalPlayerUtil.getNamesOfPlayersWithinRadius(this.radiusSupplier.get());

        if (recipients.isEmpty()) return Optional.of(Component.translatable("clientchatchannels.channel.local.message.status.no_recipients").withStyle(MessageColors.ERROR));

        return Optional.of(TextListUtil.renderTextList(
            Component.translatable("clientchatchannels.channel.local.status_header").withStyle(MessageColors.PRIMARY),
            recipients.stream().map(recipientName -> Component.literal(recipientName).withStyle(MessageColors.SECONDARY)),
            MessageColors.SUBTLE
        ));
    }

}
