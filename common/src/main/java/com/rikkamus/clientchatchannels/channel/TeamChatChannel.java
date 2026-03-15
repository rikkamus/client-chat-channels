package com.rikkamus.clientchatchannels.channel;

import com.rikkamus.clientchatchannels.*;
import com.rikkamus.clientchatchannels.indicator.ChannelIndicator;
import com.rikkamus.clientchatchannels.indicator.ChannelIndicatorTooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.PlayerTeam;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class TeamChatChannel implements ChatChannel {

    @Override
    public void interceptMessage(CancelableMessage message) {
        message.cancel();

        if (!LocalPlayerUtil.isPlayerInTeam()) {
            ChatLogger.logTranslatable("clientchatchannels.channel.team.message.intercept.not_in_team", MessageColors.ERROR);
            return;
        }

        Minecraft.getInstance().getConnection().sendCommand(String.format("teammsg %s", message.getContent()));
    }

    @Override
    public Component getDisplayName() {
        PlayerTeam team = Minecraft.getInstance().player.getTeam();

        if (team != null) return Component.translatable("clientchatchannels.channel.team.display_name_in_team", team.getDisplayName());
        else return Component.translatable("clientchatchannels.channel.team.display_name_no_team");
    }

    @Override
    public ChannelIndicator getChannelIndicator() {
        ChannelIndicatorTooltip tooltip = new ChannelIndicatorTooltip(
                List.of(Component.translatable("clientchatchannels.channel.team.display_name").withStyle(MessageColors.INDICATOR_TEAM)),
                List.of(getDisplayName().copy().withStyle(MessageColors.INDICATOR_TEAM)),
                Stream.concat(
                        Stream.of(getDisplayName().copy().withStyle(MessageColors.INDICATOR_TEAM)),
                        getTooltipRecipientStream()
                ).toList()
        );

        return new ChannelIndicator(
                Component.translatable("clientchatchannels.channel.team.indicator_short").withStyle(MessageColors.INDICATOR_TEAM),
                Component.translatable("clientchatchannels.channel.team.indicator_long").withStyle(MessageColors.INDICATOR_TEAM),
                tooltip
        );
    }

    private Stream<Component> getTooltipRecipientStream() {
        if (!LocalPlayerUtil.isPlayerInTeam()) return Stream.of(Component.translatable("clientchatchannels.channel.team.message.tooltip.not_in_team").withStyle(MessageColors.ERROR));

        Set<String> recipients = LocalPlayerUtil.getNamesOfPlayersInTeam();

        if (recipients.isEmpty()) return Stream.of(Component.translatable("clientchatchannels.channel.team.message.tooltip.no_recipients").withStyle(MessageColors.ERROR));
        else return recipients.stream().map(recipientName -> Component.literal(recipientName).withStyle(MessageColors.SUBTLE));
    }

    @Override
    public Optional<Component> getStatus() {
        Set<String> recipients = LocalPlayerUtil.getNamesOfPlayersInTeam();

        if (recipients.isEmpty()) return Optional.of(Component.translatable("clientchatchannels.channel.team.message.status.no_recipients").withStyle(MessageColors.ERROR));

        return Optional.of(TextListUtil.renderTextList(
            Component.translatable("clientchatchannels.channel.team.status_header").withStyle(MessageColors.PRIMARY),
            recipients.stream().map(recipientName -> Component.literal(recipientName).withStyle(MessageColors.SECONDARY)),
            MessageColors.SUBTLE
        ));
    }

}
