package com.rikkamus.clientchatchannels.neoforge;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.rikkamus.clientchatchannels.PlayerNameArgument;
import net.minecraft.commands.Commands;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.common.Mod;

import com.rikkamus.clientchatchannels.ClientChatChannelsMod;
import net.neoforged.neoforge.client.event.ClientChatEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = ClientChatChannelsMod.MOD_ID, dist = Dist.CLIENT)
public class ClientChatChannelsModNeoForge {

    private final ClientChatChannelsMod mod = new ClientChatChannelsMod();

    public ClientChatChannelsModNeoForge() {
        NeoForge.EVENT_BUS.addListener(this::onRegisterClientCommands);
        NeoForge.EVENT_BUS.addListener(this::onClientLoggedIn);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::onOutgoingChatMessage);
    }

    private void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        // Register /channel status
        event.getDispatcher().register(Commands.literal("channel").then(Commands.literal("status").executes(context -> {
            this.mod.printStatusToChat(true);
            return Command.SINGLE_SUCCESS;
        })));

        // Register /channel global
        event.getDispatcher().register(Commands.literal("channel").then(Commands.literal("global").executes(context -> {
            this.mod.setGlobalChannel();
            this.mod.printStatusToChat(false);
            return Command.SINGLE_SUCCESS;
        })));

        // Register /channel local
        event.getDispatcher().register(Commands.literal("channel").then(Commands.literal("local").executes(context -> {
            this.mod.setLocalChannel();
            this.mod.printStatusToChat(false);
            return Command.SINGLE_SUCCESS;
        })));

        // Register /channel local <radius>
        event.getDispatcher().register(Commands.literal("channel").then(Commands.literal("local").then(Commands.argument("radius", DoubleArgumentType.doubleArg(0)).executes(context -> {
            this.mod.setLocalChannel(context.getArgument("radius", Double.class));
            this.mod.printStatusToChat(false);
            return Command.SINGLE_SUCCESS;
        }))));

        // Register /channel direct
        event.getDispatcher().register(Commands.literal("channel").then(Commands.literal("direct").executes(context -> {
            if (this.mod.trySetDirectChannelToNearestPlayer()) this.mod.printStatusToChat(false);
            return Command.SINGLE_SUCCESS;
        })));

        // Register /channel direct <recipient>
        event.getDispatcher().register(Commands.literal("channel").then(Commands.literal("direct").then(Commands.argument("recipient", new PlayerNameArgument()).executes(context -> {
            this.mod.setDirectChannel(context.getArgument("recipient", String.class));
            this.mod.printStatusToChat(false);
            return Command.SINGLE_SUCCESS;
        }))));
    }

    private void onClientLoggedIn(ClientPlayerNetworkEvent.LoggingIn event) {
        // Reset channel when joining new world/server
        this.mod.setGlobalChannel();
    }

    private void onOutgoingChatMessage(ClientChatEvent event) {
        this.mod.interceptMessage(new ClientChatEventMessage(event));
    }

}
