package com.rikkamus.clientchatchannels;

import com.rikkamus.clientchatchannels.channel.*;
import com.rikkamus.clientchatchannels.config.ConfigValueSupplier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class InterceptingMessageDispatcher {

    @NotNull
    private ChatChannel channel = new GlobalChatChannel();

    public void interceptMessage(CancelableMessage message) {
        this.channel.interceptMessage(message);
    }

    public void setGlobalChannel() {
        this.channel = new GlobalChatChannel();
    }

    public void setLocalChannel(ConfigValueSupplier<Double> radiusSupplier) {
        this.channel = new LocalChatChannel(radiusSupplier);
    }

    public boolean trySetDirectChannelToNearestPlayer() {
        Optional<List<String>> optionalRecipients = LocalPlayerUtil.getNameOfNearestPlayer().map(List::of);
        optionalRecipients.ifPresentOrElse(this::setDirectChannel, () -> ChatLogger.logTranslatable("clientchatchannels.dispatcher.message.no_recipients", MessageColors.ERROR));

        return optionalRecipients.isPresent();
    }

    public void setDirectChannel(SequencedCollection<String> recipients) {
        this.channel = new DirectChatChannel(recipients);
    }

    public Component getStatus(boolean includeDetails) {
        final MutableComponent status = Component.translatable(
            "clientchatchannels.dispatcher.status.current_channel",
            this.channel.getDisplayName().copy().withStyle(MessageColors.SECONDARY)
        ).withStyle(MessageColors.PRIMARY);

        if (includeDetails) this.channel.getStatus().ifPresent(details -> status.append(Component.literal("\n")).append(details));

        return status;
    }

}
