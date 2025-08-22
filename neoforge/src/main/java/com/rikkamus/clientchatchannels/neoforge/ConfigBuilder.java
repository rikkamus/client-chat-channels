package com.rikkamus.clientchatchannels.neoforge;

import com.rikkamus.clientchatchannels.config.ClientChatChannelsConfig;
import com.rikkamus.clientchatchannels.config.ClothConfig;
import com.rikkamus.clientchatchannels.config.DefaultConfig;
import lombok.experimental.UtilityClass;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.clothconfig2.ClothConfigInitializer;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@UtilityClass
public class ConfigBuilder {

    // Moved outside ClientChatChannelsModNeoForge to prevent a NoClassDefFoundError triggered by NeoForge.EVENT_BUS.register(this)
    public static ClientChatChannelsConfig buildConfig(ModContainer container) {
        if (ModList.get().isLoaded(ClothConfigInitializer.MOD_ID)) {
            ConfigHolder<ClothConfig> holder = AutoConfig.register(ClothConfig.class, JanksonConfigSerializer::new);
            holder.registerSaveListener((configHolder, clothConfig) -> clothConfig.validate());

            container.registerExtensionPoint(
                IConfigScreenFactory.class,
                (modContainer, parent) -> AutoConfig.getConfigScreen(ClothConfig.class, parent).get()
            );

            return holder.getConfig();
        } else {
            return new DefaultConfig();
        }
    }

}
