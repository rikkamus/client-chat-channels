package com.rikkamus.clientchatchannels.channel;

import com.rikkamus.clientchatchannels.CancelableMessage;
import com.rikkamus.clientchatchannels.ChatLogger;
import com.rikkamus.clientchatchannels.LocalPlayerUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
public class LocalChatChannel implements ChatChannel {

    @Getter
    @Setter
    private double radius;

    @Override
    public void interceptMessage(CancelableMessage message) {
        message.cancel();

        Set<String> recipients = LocalPlayerUtil.getNamesOfPlayersWithinRadius(this.radius);

        if (recipients.isEmpty()) {
            ChatLogger.log("No nearby players found.", ChatFormatting.RED);
            return;
        }

        for (String recipientName : recipients) {
            Minecraft.getInstance().getConnection().sendCommand(String.format("msg %s %s", recipientName, message.getContent()));
        }
    }

    @Override
    public String getDisplayName() {
        return String.format("Local (%s block radius)", this.radius);
    }

    @Override
    public Optional<Component> getStatus() {
        Set<String> recipients = LocalPlayerUtil.getNamesOfPlayersWithinRadius(this.radius);

        if (recipients.isEmpty()) return Optional.of(Component.literal("Nobody can see your messages.").withStyle(ChatFormatting.RED));

        return Optional.of(recipients.stream().map(
            recipientName -> Component.literal(" - ").withStyle(ChatFormatting.GRAY).append(Component.literal(recipientName).withStyle(ChatFormatting.YELLOW))
        ).reduce(
            Component.literal("Players that can see your messages:").withStyle(ChatFormatting.AQUA),
            (acc, component) -> acc.append(Component.literal("\n")).append(component)
        ));
    }

}
