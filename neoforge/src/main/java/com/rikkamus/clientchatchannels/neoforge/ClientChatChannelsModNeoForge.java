package com.rikkamus.clientchatchannels.neoforge;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.rikkamus.clientchatchannels.command.PlayerNameSuggestionProvider;
import com.rikkamus.clientchatchannels.command.WordListArgumentType;
import com.rikkamus.clientchatchannels.command.WordListSuggestionProvider;
import net.minecraft.commands.Commands;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import com.rikkamus.clientchatchannels.ClientChatChannelsMod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.NeoForge;

import java.util.List;
import java.util.TreeSet;

@Mod(value = ClientChatChannelsMod.MOD_ID, dist = Dist.CLIENT)
public class ClientChatChannelsModNeoForge {

    private final ModContainer container;

    public ClientChatChannelsModNeoForge(ModContainer container, IEventBus modEventBus) {
        this.container = container;

        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::onRegisterKeyMappings);
        NeoForge.EVENT_BUS.register(this);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        ClientChatChannelsMod.init(ConfigBuilder.buildConfig(this.container));
    }

    private void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        ClientChatChannelsMod.registerKeyMappings(event::register);
    }

    @SubscribeEvent
    private void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        // Register /channel status
        event.getDispatcher().register(Commands.literal("channel").then(Commands.literal("status").executes(context -> {
            ClientChatChannelsMod.getInstance().printStatus();
            return Command.SINGLE_SUCCESS;
        })));

        // Register /channel global
        event.getDispatcher().register(Commands.literal("channel").then(Commands.literal("global").executes(context -> {
            ClientChatChannelsMod.getInstance().switchToGlobalChannel();
            return Command.SINGLE_SUCCESS;
        })));

        // Register /channel local
        event.getDispatcher().register(Commands.literal("channel").then(Commands.literal("local").executes(context -> {
            ClientChatChannelsMod.getInstance().switchToLocalChannel();
            return Command.SINGLE_SUCCESS;
        }).then(Commands.argument("radius", DoubleArgumentType.doubleArg(0)).executes(context -> {
            ClientChatChannelsMod.getInstance().switchToLocalChannel(context.getArgument("radius", Double.class));
            return Command.SINGLE_SUCCESS;
        }))));

        // Register /channel direct
        event.getDispatcher().register(Commands.literal("channel").then(Commands.literal("direct").executes(context -> {
            ClientChatChannelsMod.getInstance().switchToDirectChannel();
            return Command.SINGLE_SUCCESS;
        }).then(Commands.argument("recipients", new WordListArgumentType()).suggests(new WordListSuggestionProvider<>(new PlayerNameSuggestionProvider<>())).executes(context -> {
            @SuppressWarnings("unchecked")
            TreeSet<String> recipients = new TreeSet<String>(context.getArgument("recipients", List.class));

            ClientChatChannelsMod.getInstance().switchToDirectChannel(recipients);

            return Command.SINGLE_SUCCESS;
        }))));
    }

    @SubscribeEvent
    private void onClientTick(ClientTickEvent.Post event) {
        ClientChatChannelsMod.getInstance().handleChannelHotkeys();
    }

    @SubscribeEvent
    private void onClientLoggedIn(ClientPlayerNetworkEvent.LoggingIn event) {
        // Reset channel when joining new world/server
        ClientChatChannelsMod.getInstance().resetChannel();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    private void onOutgoingChatMessage(ClientChatEvent event) {
        ClientChatChannelsMod.getInstance().interceptMessage(new ClientChatEventMessage(event));
    }

}
