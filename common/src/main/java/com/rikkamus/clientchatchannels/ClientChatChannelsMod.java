package com.rikkamus.clientchatchannels;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class ClientChatChannelsMod {

    public static final String MOD_ID = "clientchatchannels";

    private static final String KEY_CATEGORY = "key.categories.clientchatchannels.default";

    private static final double DEFAULT_LOCAL_CHANNEL_RADIUS = 15;

    private static KeyMapping GLOBAL_CHANNEL_KEY_MAPPING;
    private static KeyMapping LOCAL_CHANNEL_KEY_MAPPING;
    private static KeyMapping DIRECT_CHANNEL_KEY_MAPPING;

    public static KeyMapping getGlobalChannelKeyMapping() {
        if (GLOBAL_CHANNEL_KEY_MAPPING == null) GLOBAL_CHANNEL_KEY_MAPPING = new KeyMapping(
            "key.clientchatchannels.global",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_U,
            KEY_CATEGORY
        );

        return GLOBAL_CHANNEL_KEY_MAPPING;
    }

    public static KeyMapping getLocalChannelKeyMapping() {
        if (LOCAL_CHANNEL_KEY_MAPPING == null) LOCAL_CHANNEL_KEY_MAPPING = new KeyMapping(
            "key.clientchatchannels.local",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_I,
            KEY_CATEGORY
        );

        return LOCAL_CHANNEL_KEY_MAPPING;
    }

    public static KeyMapping getDirectChannelKeyMapping() {
        if (DIRECT_CHANNEL_KEY_MAPPING == null) DIRECT_CHANNEL_KEY_MAPPING = new KeyMapping(
            "key.clientchatchannels.direct",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            KEY_CATEGORY
        );

        return DIRECT_CHANNEL_KEY_MAPPING;
    }

    public static void registerKeyMappings(Consumer<KeyMapping> registry) {
        registry.accept(getGlobalChannelKeyMapping());
        registry.accept(getLocalChannelKeyMapping());
        registry.accept(getDirectChannelKeyMapping());
    }

    private final InterceptingMessageDispatcher dispatcher = new InterceptingMessageDispatcher();

    public void switchToGlobalChannel() {
        this.dispatcher.setGlobalChannel();
        ChatLogger.log(this.dispatcher.getStatus(false));
    }

    public void switchToLocalChannel() {
        switchToLocalChannel(ClientChatChannelsMod.DEFAULT_LOCAL_CHANNEL_RADIUS);
    }

    public void switchToLocalChannel(double radius) {
        this.dispatcher.setLocalChannel(radius);
        ChatLogger.log(this.dispatcher.getStatus(false));
    }

    public void switchToDirectChannel() {
        this.dispatcher.trySetDirectChannelToNearestPlayer();
        ChatLogger.log(this.dispatcher.getStatus(false));
    }

    public void switchToDirectChannel(@NotNull String recipientName) {
        this.dispatcher.setDirectChannel(recipientName);
        ChatLogger.log(this.dispatcher.getStatus(false));
    }

    public void printStatus() {
        ChatLogger.log(this.dispatcher.getStatus(true));
    }

    public void handleChannelHotkeys() {
        if (ClientChatChannelsMod.getGlobalChannelKeyMapping().consumeClick()) switchToGlobalChannel();
        else if (ClientChatChannelsMod.getLocalChannelKeyMapping().consumeClick()) switchToLocalChannel();
        else if (ClientChatChannelsMod.getDirectChannelKeyMapping().consumeClick()) switchToDirectChannel();
    }

    public void resetChannel() {
        this.dispatcher.setGlobalChannel();
    }

    public void interceptMessage(CancelableMessage message) {
        this.dispatcher.interceptMessage(message);
    }

}
