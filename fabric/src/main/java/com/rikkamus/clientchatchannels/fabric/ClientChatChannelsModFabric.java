package com.rikkamus.clientchatchannels.fabric;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.rikkamus.clientchatchannels.ClientChatChannelsMod;
import com.rikkamus.clientchatchannels.PlayerNameArgument;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.resources.ResourceLocation;

public class ClientChatChannelsModFabric implements ClientModInitializer {

    private static final ResourceLocation INTERCEPT_MESSAGE_EVENT_PHASE = ResourceLocation.fromNamespaceAndPath(ClientChatChannelsMod.MOD_ID, "intercept_message");

    private final ClientChatChannelsMod mod = new ClientChatChannelsMod();

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, buildContext) -> {
            // Register /channel status
            dispatcher.register(ClientCommandManager.literal("channel").then(ClientCommandManager.literal("status").executes(context -> {
                this.mod.printStatusToChat(true);
                return Command.SINGLE_SUCCESS;
            })));

            // Register /channel global
            dispatcher.register(ClientCommandManager.literal("channel").then(ClientCommandManager.literal("global").executes(context -> {
                this.mod.setGlobalChannel();
                this.mod.printStatusToChat(false);
                return Command.SINGLE_SUCCESS;
            })));

            // Register /channel local
            dispatcher.register(ClientCommandManager.literal("channel").then(ClientCommandManager.literal("local").executes(context -> {
                this.mod.setLocalChannel();
                this.mod.printStatusToChat(false);
                return Command.SINGLE_SUCCESS;
            })));

            // Register /channel local <radius>
            dispatcher.register(ClientCommandManager.literal("channel").then(ClientCommandManager.literal("local").then(ClientCommandManager.argument("radius", DoubleArgumentType.doubleArg(0)).executes(context -> {
                this.mod.setLocalChannel(context.getArgument("radius", Double.class));
                this.mod.printStatusToChat(false);
                return Command.SINGLE_SUCCESS;
            }))));

            // Register /channel direct
            dispatcher.register(ClientCommandManager.literal("channel").then(ClientCommandManager.literal("direct").executes(context -> {
                if (this.mod.trySetDirectChannelToNearestPlayer()) this.mod.printStatusToChat(false);
                return Command.SINGLE_SUCCESS;
            })));

            // Register /channel direct <recipient>
            dispatcher.register(ClientCommandManager.literal("channel").then(ClientCommandManager.literal("direct").then(ClientCommandManager.argument("recipient", new PlayerNameArgument()).executes(context -> {
                this.mod.setDirectChannel(context.getArgument("recipient", String.class));
                this.mod.printStatusToChat(false);
                return Command.SINGLE_SUCCESS;
            }))));
        });

        ClientLoginConnectionEvents.INIT.register((clientHandshakePacketListener, minecraft) -> {
            // Reset channel when joining new world/server
            this.mod.setGlobalChannel();
        });

        ClientSendMessageEvents.ALLOW_CHAT.addPhaseOrdering(Event.DEFAULT_PHASE, ClientChatChannelsModFabric.INTERCEPT_MESSAGE_EVENT_PHASE);
        ClientSendMessageEvents.ALLOW_CHAT.register(ClientChatChannelsModFabric.INTERCEPT_MESSAGE_EVENT_PHASE, message -> {
            SimpleCancelableMessage cancelableMessage = new SimpleCancelableMessage(message);
            this.mod.interceptMessage(cancelableMessage);

            return !cancelableMessage.isCanceled();
        });
    }

}
