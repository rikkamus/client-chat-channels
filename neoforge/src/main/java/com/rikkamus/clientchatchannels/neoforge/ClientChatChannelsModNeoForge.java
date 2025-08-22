package com.rikkamus.clientchatchannels.neoforge;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.rikkamus.clientchatchannels.PlayerNameArgument;
import com.rikkamus.clientchatchannels.config.ClientChatChannelsConfig;
import com.rikkamus.clientchatchannels.config.ClothConfig;
import com.rikkamus.clientchatchannels.config.DefaultConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.clothconfig2.ClothConfigInitializer;
import net.minecraft.commands.Commands;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

import com.rikkamus.clientchatchannels.ClientChatChannelsMod;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = ClientChatChannelsMod.MOD_ID, dist = Dist.CLIENT)
public class ClientChatChannelsModNeoForge {

    private final ClientChatChannelsMod mod;

    public ClientChatChannelsModNeoForge(ModContainer container, IEventBus modEventBus) {
        this.mod = new ClientChatChannelsMod(buildConfig(container));

        modEventBus.addListener(this::onRegisterKeyMappings);
        NeoForge.EVENT_BUS.register(this);
    }

    private ClientChatChannelsConfig buildConfig(ModContainer container) {
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

    private void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        ClientChatChannelsMod.registerKeyMappings(event::register);
    }

    @SubscribeEvent
    private void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        // Register /channel status
        event.getDispatcher().register(Commands.literal("channel").then(Commands.literal("status").executes(context -> {
            this.mod.printStatus();
            return Command.SINGLE_SUCCESS;
        })));

        // Register /channel global
        event.getDispatcher().register(Commands.literal("channel").then(Commands.literal("global").executes(context -> {
            this.mod.switchToGlobalChannel();
            return Command.SINGLE_SUCCESS;
        })));

        // Register /channel local
        event.getDispatcher().register(Commands.literal("channel").then(Commands.literal("local").executes(context -> {
            this.mod.switchToLocalChannel();
            return Command.SINGLE_SUCCESS;
        })));

        // Register /channel local <radius>
        event.getDispatcher().register(Commands.literal("channel").then(Commands.literal("local").then(Commands.argument("radius", DoubleArgumentType.doubleArg(0)).executes(context -> {
            this.mod.switchToLocalChannel(context.getArgument("radius", Double.class));
            return Command.SINGLE_SUCCESS;
        }))));

        // Register /channel direct
        event.getDispatcher().register(Commands.literal("channel").then(Commands.literal("direct").executes(context -> {
            this.mod.switchToDirectChannel();
            return Command.SINGLE_SUCCESS;
        })));

        // Register /channel direct <recipient>
        event.getDispatcher().register(Commands.literal("channel").then(Commands.literal("direct").then(Commands.argument("recipient", new PlayerNameArgument()).executes(context -> {
            this.mod.switchToDirectChannel(context.getArgument("recipient", String.class));
            return Command.SINGLE_SUCCESS;
        }))));
    }

    @SubscribeEvent
    private void onClientTick(ClientTickEvent.Post event) {
        this.mod.handleChannelHotkeys();
    }

    @SubscribeEvent
    private void onClientLoggedIn(ClientPlayerNetworkEvent.LoggingIn event) {
        // Reset channel when joining new world/server
        this.mod.resetChannel();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    private void onOutgoingChatMessage(ClientChatEvent event) {
        this.mod.interceptMessage(new ClientChatEventMessage(event));
    }

}
