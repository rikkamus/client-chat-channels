package com.rikkamus.clientchatchannels;

import com.rikkamus.clientchatchannels.channel.ChatChannel;
import com.rikkamus.clientchatchannels.channel.DirectChatChannel;
import com.rikkamus.clientchatchannels.channel.GlobalChatChannel;
import com.rikkamus.clientchatchannels.channel.LocalChatChannel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class InterceptingMessageDispatcher {

    @NotNull
    private ChatChannel channel = new GlobalChatChannel();

    public void interceptMessage(CancelableMessage message) {
        this.channel.interceptMessage(message);
    }

    public void setGlobalChannel() {
        this.channel = new GlobalChatChannel();
    }

    public void setLocalChannel(double radius) {
        this.channel = new LocalChatChannel(radius);
    }

    public boolean trySetDirectChannelToNearestPlayer() {
        Optional<String> optionalRecipient = LocalPlayerUtil.getNameOfNearestPlayer();
        optionalRecipient.ifPresentOrElse(this::setDirectChannel, () -> ChatLogger.log("No nearby players found.", MessageColors.ERROR));

        return optionalRecipient.isPresent();
    }

    public void setDirectChannel(@NotNull String recipientName) {
        this.channel = new DirectChatChannel(recipientName);
    }

    public Component getStatus(boolean includeDetails) {
        final MutableComponent status = Component.literal("Current channel: ").withStyle(MessageColors.PRIMARY)
                                                 .append(Component.literal(this.channel.getDisplayName()).withStyle(MessageColors.SECONDARY));

        if (includeDetails) this.channel.getStatus().ifPresent(details -> status.append(Component.literal("\n")).append(details));

        return status;
    }

}
