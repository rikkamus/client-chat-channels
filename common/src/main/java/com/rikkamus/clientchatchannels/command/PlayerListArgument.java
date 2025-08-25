package com.rikkamus.clientchatchannels.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.concurrent.CompletableFuture;

public class PlayerListArgument extends AbstractWordListArgument {

    @Override
    protected <S> CompletableFuture<Suggestions> listWordSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        var playerNames = Minecraft.getInstance().player.connection.getOnlinePlayers().stream().map(playerInfo -> playerInfo.getProfile().getName()).sorted();
        return SharedSuggestionProvider.suggest(playerNames, builder);
    }

}
