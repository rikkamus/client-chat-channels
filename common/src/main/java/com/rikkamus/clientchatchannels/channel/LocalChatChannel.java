package com.rikkamus.clientchatchannels.channel;

import com.rikkamus.clientchatchannels.*;
import com.rikkamus.clientchatchannels.config.ConfigValueSupplier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.Optional;
import java.util.Set;

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
