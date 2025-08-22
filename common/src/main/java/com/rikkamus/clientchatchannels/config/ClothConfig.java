package com.rikkamus.clientchatchannels.config;

import com.rikkamus.clientchatchannels.ClientChatChannelsMod;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.world.InteractionResult;

@Config(name = ClientChatChannelsMod.MOD_ID)
public class ClothConfig implements ClientChatChannelsConfig, ConfigData {

    @ConfigEntry.Category("default")
    @Comment("""
        The default radius (in blocks) used for local channel messages when no custom radius is set.
        Specifies the maximum distance from the player within which other players can see the messages.""")
    private double defaultLocalChannelRadius = DefaultConfig.DEFAULT_LOCAL_CHANNEL_RADIUS;

    @Override
    public void validatePostLoad() {
        validate();
    }

    public InteractionResult validate() {
        if (this.defaultLocalChannelRadius < 0) {
            ClientChatChannelsMod.LOGGER.warn("Default local channel radius is less than zero, correcting...");
            this.defaultLocalChannelRadius = DefaultConfig.DEFAULT_LOCAL_CHANNEL_RADIUS;
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public double getDefaultLocalChannelRadius() {
        return this.defaultLocalChannelRadius;
    }

}
