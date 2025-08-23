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
            ChatLogger.log("No nearby players found.", MessageColors.ERROR);
            return;
        }

        for (String recipientName : recipients) {
            Minecraft.getInstance().getConnection().sendCommand(String.format("msg %s %s", recipientName, message.getContent()));
        }
    }

    @Override
    public String getDisplayName() {
        if (this.radiusSupplier.isUsingConfigValue()) return "Local (default radius)";
        else return String.format("Local (%s block radius)", this.radiusSupplier.get());
    }

    @Override
    public Optional<Component> getStatus() {
        Set<String> recipients = LocalPlayerUtil.getNamesOfPlayersWithinRadius(this.radiusSupplier.get());

        if (recipients.isEmpty()) return Optional.of(Component.literal("Nobody can see your messages.").withStyle(MessageColors.ERROR));

        return Optional.of(TextListUtil.renderTextList(
            Component.literal("Players that can see your messages:").withStyle(MessageColors.PRIMARY),
            recipients.stream().map(recipientName -> Component.literal(recipientName).withStyle(MessageColors.SECONDARY)),
            MessageColors.SUBTLE
        ));
    }

}
