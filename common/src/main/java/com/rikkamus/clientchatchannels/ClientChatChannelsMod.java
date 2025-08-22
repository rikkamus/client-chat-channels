package com.rikkamus.clientchatchannels;

import com.mojang.blaze3d.platform.InputConstants;
import lombok.experimental.UtilityClass;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

@UtilityClass
public class ClientChatChannelsMod {

    public static final String MOD_ID = "clientchatchannels";

    private static final String KEY_CATEGORY = "key.categories.clientchatchannels.default";

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

}
