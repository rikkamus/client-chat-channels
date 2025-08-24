package com.rikkamus.clientchatchannels;

import lombok.experimental.UtilityClass;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.stream.Stream;

@UtilityClass
public class TextListUtil {

    public static Component renderTextList(Component title, Stream<Component> items, ChatFormatting bulletColor) {
        return items.map(item -> Component.literal(" - ").withStyle(bulletColor).append(item)).reduce(
            title.copy(),
            (acc, component) -> acc.append(Component.literal("\n")).append(component)
        );
    }

}
