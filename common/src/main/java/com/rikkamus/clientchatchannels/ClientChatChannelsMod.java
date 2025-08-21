package com.rikkamus.clientchatchannels;

import com.rikkamus.clientchatchannels.channel.ChatChannel;
import com.rikkamus.clientchatchannels.channel.DirectChatChannel;
import com.rikkamus.clientchatchannels.channel.GlobalChatChannel;
import com.rikkamus.clientchatchannels.channel.LocalChatChannel;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Optional;

public class ClientChatChannelsMod {

    public static final String MOD_ID = "clientchatchannels";

    private ChatChannel channel = new GlobalChatChannel();

    public void interceptMessage(CancelableMessage message) {
        this.channel.interceptMessage(message);
    }

    public void setGlobalChannel() {
        this.channel = new GlobalChatChannel();
    }

    public void setLocalChannel() {
        setLocalChannel(15);
    }

    public void setLocalChannel(double radius) {
        this.channel = new LocalChatChannel(radius);
    }

    public boolean trySetDirectChannelToNearestPlayer() {
        Optional<String> optionalRecipient = LocalPlayerUtil.getNameOfNearestPlayer();
        optionalRecipient.ifPresentOrElse(this::setDirectChannel, () -> ChatLogger.log("No nearby players found.", ChatFormatting.RED));

        return optionalRecipient.isPresent();
    }

    public void setDirectChannel(String recipientName) {
        this.channel = new DirectChatChannel(recipientName);
    }

    public void printStatusToChat(boolean includeDetails) {
        final MutableComponent status = Component.literal("Current channel: ").withStyle(ChatFormatting.AQUA)
                                                 .append(Component.literal(this.channel.getDisplayName()).withStyle(ChatFormatting.YELLOW));

        if (includeDetails) this.channel.getStatus().ifPresent(details -> status.append(Component.literal("\n")).append(details));

        ChatLogger.log(status);
    }

}
