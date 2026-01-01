package com.rikkamus.clientchatchannels.config;

import com.rikkamus.clientchatchannels.ClientChatChannelsMod;
import com.rikkamus.clientchatchannels.indicator.ChannelIndicatorTooltipType;
import com.rikkamus.clientchatchannels.indicator.ChannelIndicatorType;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.world.InteractionResult;

@Config(name = ClientChatChannelsMod.MOD_ID)
public class ClothConfig implements ClientChatChannelsConfig, ConfigData {

    @ConfigEntry.Category("default")
    @ConfigEntry.Gui.Tooltip
    @Comment("""
        The default radius used for local channel messages when no custom radius is set.
        Specifies the maximum distance (in blocks) from the player within which other players can see the messages.""")
    private double defaultLocalChannelRadius = DefaultConfig.DEFAULT_LOCAL_CHANNEL_RADIUS;

    @ConfigEntry.Category("default")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    @ConfigEntry.Gui.Tooltip
    @Comment("""
        The type of channel indicator to show when typing a message in chat.
        NONE - No indicator
        SHORT - Single-letter indicator
        LONG - Full channel name""")
    private ChannelIndicatorType channelIndicatorType = DefaultConfig.DEFAULT_CHANNEL_INDICATOR_TYPE;

    @ConfigEntry.Category("default")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    @ConfigEntry.Gui.Tooltip
    @Comment("""
        The type of tooltip that appears when hovering over the channel indicator.
        NONE - No tooltip
        MINIMAL - Channel name only
        SIMPLE - Basic channel details
        DETAILED - Full channel details""")
    private ChannelIndicatorTooltipType channelIndicatorTooltipType = DefaultConfig.DEFAULT_CHANNEL_INDICATOR_TOOLTIP_TYPE;

    @Override
    public void validatePostLoad() {
        validate();
    }

    public InteractionResult validate() {
        if (this.defaultLocalChannelRadius < 0) {
            ClientChatChannelsMod.LOGGER.warn("Default local channel radius is less than zero, correcting...");
            this.defaultLocalChannelRadius = DefaultConfig.DEFAULT_LOCAL_CHANNEL_RADIUS;
        }

        if (this.channelIndicatorType == null) {
            ClientChatChannelsMod.LOGGER.warn("Channel indicator type is null, correcting...");
            this.channelIndicatorType = DefaultConfig.DEFAULT_CHANNEL_INDICATOR_TYPE;
        }

        if (this.channelIndicatorTooltipType == null) {
            ClientChatChannelsMod.LOGGER.warn("Channel indicator tooltip type is null, correcting...");
            this.channelIndicatorTooltipType = DefaultConfig.DEFAULT_CHANNEL_INDICATOR_TOOLTIP_TYPE;
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public double getDefaultLocalChannelRadius() {
        return this.defaultLocalChannelRadius;
    }

    @Override
    public ChannelIndicatorType getChannelIndicatorType() {
        return this.channelIndicatorType;
    }

    @Override
    public ChannelIndicatorTooltipType getChannelIndicatorTooltipType() {
        return this.channelIndicatorTooltipType;
    }

}
