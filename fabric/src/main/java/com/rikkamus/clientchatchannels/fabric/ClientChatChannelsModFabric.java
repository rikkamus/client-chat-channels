package com.rikkamus.clientchatchannels.fabric;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.rikkamus.clientchatchannels.ClientChatChannelsMod;
import com.rikkamus.clientchatchannels.command.PlayerNameSuggestionProvider;
import com.rikkamus.clientchatchannels.command.WordListArgumentType;
import com.rikkamus.clientchatchannels.command.WordListSuggestionProvider;
import com.rikkamus.clientchatchannels.config.ClientChatChannelsConfig;
import com.rikkamus.clientchatchannels.config.ClothConfig;
import com.rikkamus.clientchatchannels.config.DefaultConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.TreeSet;

public class ClientChatChannelsModFabric implements ClientModInitializer {

    private static final ResourceLocation INTERCEPT_MESSAGE_EVENT_PHASE = ResourceLocation.fromNamespaceAndPath(ClientChatChannelsMod.MOD_ID, "intercept_message");

    @Override
    public void onInitializeClient() {
        final ClientChatChannelsMod mod = new ClientChatChannelsMod(buildConfig());

        ClientChatChannelsMod.registerKeyMappings(KeyBindingHelper::registerKeyBinding);

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, buildContext) -> {
            // Register /channel status
            dispatcher.register(ClientCommandManager.literal("channel").then(ClientCommandManager.literal("status").executes(context -> {
                mod.printStatus();
                return Command.SINGLE_SUCCESS;
            })));

            // Register /channel global
            dispatcher.register(ClientCommandManager.literal("channel").then(ClientCommandManager.literal("global").executes(context -> {
                mod.switchToGlobalChannel();
                return Command.SINGLE_SUCCESS;
            })));

            // Register /channel local
            dispatcher.register(ClientCommandManager.literal("channel").then(ClientCommandManager.literal("local").executes(context -> {
                mod.switchToLocalChannel();
                return Command.SINGLE_SUCCESS;
            })));

            // Register /channel local <radius>
            dispatcher.register(ClientCommandManager.literal("channel").then(ClientCommandManager.literal("local").then(ClientCommandManager.argument("radius", DoubleArgumentType.doubleArg(0)).executes(context -> {
                mod.switchToLocalChannel(context.getArgument("radius", Double.class));
                return Command.SINGLE_SUCCESS;
            }))));

            // Register /channel direct
            dispatcher.register(ClientCommandManager.literal("channel").then(ClientCommandManager.literal("direct").executes(context -> {
                mod.switchToDirectChannel();
                return Command.SINGLE_SUCCESS;
            })));

            // Register /channel direct <recipients>
            dispatcher.register(ClientCommandManager.literal("channel").then(ClientCommandManager.literal("direct").then(ClientCommandManager.argument("recipients", new WordListArgumentType()).suggests(new WordListSuggestionProvider<>(new PlayerNameSuggestionProvider<>())).executes(context -> {
                @SuppressWarnings("unchecked")
                TreeSet<String> recipients = new TreeSet<String>(context.getArgument("recipients", List.class));

                mod.switchToDirectChannel(recipients);

                return Command.SINGLE_SUCCESS;
            }))));
        });

        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> mod.handleChannelHotkeys());

        // Reset channel when joining new world/server
        ClientLoginConnectionEvents.INIT.register((clientHandshakePacketListener, minecraft) -> mod.resetChannel());

        ClientSendMessageEvents.ALLOW_CHAT.addPhaseOrdering(Event.DEFAULT_PHASE, ClientChatChannelsModFabric.INTERCEPT_MESSAGE_EVENT_PHASE);
        ClientSendMessageEvents.ALLOW_CHAT.register(ClientChatChannelsModFabric.INTERCEPT_MESSAGE_EVENT_PHASE, message -> {
            SimpleCancelableMessage cancelableMessage = new SimpleCancelableMessage(message);
            mod.interceptMessage(cancelableMessage);

            return !cancelableMessage.isCanceled();
        });
    }

    private ClientChatChannelsConfig buildConfig() {
        if (FabricLoader.getInstance().isModLoaded("cloth-config")) {
            ConfigHolder<ClothConfig> holder = AutoConfig.register(ClothConfig.class, JanksonConfigSerializer::new);
            holder.registerSaveListener((configHolder, clothConfig) -> clothConfig.validate());
            return holder.getConfig();
        } else {
            return new DefaultConfig();
        }
    }

}
