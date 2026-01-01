package com.rikkamus.clientchatchannels;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import com.rikkamus.clientchatchannels.config.ClientChatChannelsConfig;
import com.rikkamus.clientchatchannels.config.ConfigValueSupplier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import java.util.SortedSet;
import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientChatChannelsMod {

    public static final String MOD_ID = "clientchatchannels";

    public static final Logger LOGGER = LogUtils.getLogger();

    private static KeyMapping.Category KEY_CATEGORY;

    private static KeyMapping GLOBAL_CHANNEL_KEY_MAPPING;
    private static KeyMapping LOCAL_CHANNEL_KEY_MAPPING;
    private static KeyMapping DIRECT_CHANNEL_KEY_MAPPING;
    private static KeyMapping CHANNEL_STATUS_KEY_MAPPING;

    private static ClientChatChannelsMod INSTANCE;

    private static KeyMapping.Category getKeyCategory() {
        if (KEY_CATEGORY == null) KEY_CATEGORY = KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath(MOD_ID, "default"));

        return KEY_CATEGORY;
    }

    public static KeyMapping getGlobalChannelKeyMapping() {
        if (GLOBAL_CHANNEL_KEY_MAPPING == null) GLOBAL_CHANNEL_KEY_MAPPING = new KeyMapping(
            "key.clientchatchannels.global",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_U,
            getKeyCategory()
        );

        return GLOBAL_CHANNEL_KEY_MAPPING;
    }

    public static KeyMapping getLocalChannelKeyMapping() {
        if (LOCAL_CHANNEL_KEY_MAPPING == null) LOCAL_CHANNEL_KEY_MAPPING = new KeyMapping(
            "key.clientchatchannels.local",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_I,
            getKeyCategory()
        );

        return LOCAL_CHANNEL_KEY_MAPPING;
    }

    public static KeyMapping getDirectChannelKeyMapping() {
        if (DIRECT_CHANNEL_KEY_MAPPING == null) DIRECT_CHANNEL_KEY_MAPPING = new KeyMapping(
            "key.clientchatchannels.direct",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            getKeyCategory()
        );

        return DIRECT_CHANNEL_KEY_MAPPING;
    }

    public static KeyMapping getChannelStatusKeyMapping() {
        if (CHANNEL_STATUS_KEY_MAPPING == null) CHANNEL_STATUS_KEY_MAPPING = new KeyMapping(
            "key.clientchatchannels.status",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            getKeyCategory()
        );

        return CHANNEL_STATUS_KEY_MAPPING;
    }

    public static void registerKeyMappings(Consumer<KeyMapping> registry) {
        registry.accept(getGlobalChannelKeyMapping());
        registry.accept(getLocalChannelKeyMapping());
        registry.accept(getDirectChannelKeyMapping());
        registry.accept(getChannelStatusKeyMapping());
    }

    public static void init(ClientChatChannelsConfig config) {
        if (INSTANCE != null) throw new IllegalStateException("Client chat channels mod has already been initialized.");
        INSTANCE = new ClientChatChannelsMod(config);
    }

    public static ClientChatChannelsMod getInstance() {
        return INSTANCE;
    }

    @Getter
    private final InterceptingMessageDispatcher dispatcher = new InterceptingMessageDispatcher();

    @Getter
    private final ClientChatChannelsConfig config;

    public void switchToGlobalChannel() {
        this.dispatcher.setGlobalChannel();
        ChatLogger.log(this.dispatcher.getStatus(false));
    }

    public void switchToLocalChannel() {
        this.dispatcher.setLocalChannel(ConfigValueSupplier.ofConfigValue(this.config::getDefaultLocalChannelRadius));
        ChatLogger.log(this.dispatcher.getStatus(false));
    }

    public void switchToLocalChannel(double radius) {
        this.dispatcher.setLocalChannel(ConfigValueSupplier.ofOverriddenValue(radius));
        ChatLogger.log(this.dispatcher.getStatus(false));
    }

    public void switchToDirectChannel() {
        this.dispatcher.trySetDirectChannelToNearestPlayer();
        ChatLogger.log(this.dispatcher.getStatus(false));
    }

    public void switchToDirectChannel(SortedSet<String> recipients) {
        this.dispatcher.setDirectChannel(recipients);
        ChatLogger.log(this.dispatcher.getStatus(false));
    }

    public void printStatus() {
        ChatLogger.log(this.dispatcher.getStatus(true));
    }

    public void handleChannelHotkeys() {
        if (ClientChatChannelsMod.getGlobalChannelKeyMapping().consumeClick()) switchToGlobalChannel();
        else if (ClientChatChannelsMod.getLocalChannelKeyMapping().consumeClick()) switchToLocalChannel();
        else if (ClientChatChannelsMod.getDirectChannelKeyMapping().consumeClick()) switchToDirectChannel();
        else if (ClientChatChannelsMod.getChannelStatusKeyMapping().consumeClick()) printStatus();
    }

    public void resetChannel() {
        this.dispatcher.setGlobalChannel();
    }

    public void interceptMessage(CancelableMessage message) {
        this.dispatcher.interceptMessage(message);
    }

}
