package com.rikkamus.clientchatchannels.neoforge;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.rikkamus.clientchatchannels.InterceptingMessageDispatcher;
import com.rikkamus.clientchatchannels.PlayerNameArgument;
import net.minecraft.commands.Commands;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import com.rikkamus.clientchatchannels.ClientChatChannelsMod;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = ClientChatChannelsMod.MOD_ID, dist = Dist.CLIENT)
public class ClientChatChannelsModNeoForge {

    private final InterceptingMessageDispatcher dispatcher = new InterceptingMessageDispatcher();

    public ClientChatChannelsModNeoForge(IEventBus modEventBus) {
        modEventBus.addListener(this::onRegisterKeyMappings);
        NeoForge.EVENT_BUS.register(this);
    }

    private void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        ClientChatChannelsMod.registerKeyMappings(event::register);
    }

    @SubscribeEvent
    private void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        // Register /channel status
        event.getDispatcher().register(Commands.literal("channel").then(Commands.literal("status").executes(context -> {
            this.dispatcher.printStatusToChat(true);
            return Command.SINGLE_SUCCESS;
        })));

        // Register /channel global
        event.getDispatcher().register(Commands.literal("channel").then(Commands.literal("global").executes(context -> {
            this.dispatcher.setGlobalChannel();
            this.dispatcher.printStatusToChat(false);
            return Command.SINGLE_SUCCESS;
        })));

        // Register /channel local
        event.getDispatcher().register(Commands.literal("channel").then(Commands.literal("local").executes(context -> {
            this.dispatcher.setLocalChannel();
            this.dispatcher.printStatusToChat(false);
            return Command.SINGLE_SUCCESS;
        })));

        // Register /channel local <radius>
        event.getDispatcher().register(Commands.literal("channel").then(Commands.literal("local").then(Commands.argument("radius", DoubleArgumentType.doubleArg(0)).executes(context -> {
            this.dispatcher.setLocalChannel(context.getArgument("radius", Double.class));
            this.dispatcher.printStatusToChat(false);
            return Command.SINGLE_SUCCESS;
        }))));

        // Register /channel direct
        event.getDispatcher().register(Commands.literal("channel").then(Commands.literal("direct").executes(context -> {
            this.dispatcher.trySetDirectChannelToNearestPlayer();
            this.dispatcher.printStatusToChat(false);
            return Command.SINGLE_SUCCESS;
        })));

        // Register /channel direct <recipient>
        event.getDispatcher().register(Commands.literal("channel").then(Commands.literal("direct").then(Commands.argument("recipient", new PlayerNameArgument()).executes(context -> {
            this.dispatcher.setDirectChannel(context.getArgument("recipient", String.class));
            this.dispatcher.printStatusToChat(false);
            return Command.SINGLE_SUCCESS;
        }))));
    }

    @SubscribeEvent
    private void onClientTick(ClientTickEvent.Post event) {
        if (ClientChatChannelsMod.getGlobalChannelKeyMapping().consumeClick()) {
            this.dispatcher.setGlobalChannel();
            this.dispatcher.printStatusToChat(false);
        }

        if (ClientChatChannelsMod.getLocalChannelKeyMapping().consumeClick()) {
            this.dispatcher.setLocalChannel();
            this.dispatcher.printStatusToChat(false);
        }

        if (ClientChatChannelsMod.getDirectChannelKeyMapping().consumeClick()) {
            this.dispatcher.trySetDirectChannelToNearestPlayer();
            this.dispatcher.printStatusToChat(false);
        }
    }

    @SubscribeEvent
    private void onClientLoggedIn(ClientPlayerNetworkEvent.LoggingIn event) {
        // Reset channel when joining new world/server
        this.dispatcher.setGlobalChannel();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    private void onOutgoingChatMessage(ClientChatEvent event) {
        this.dispatcher.interceptMessage(new ClientChatEventMessage(event));
    }

}
