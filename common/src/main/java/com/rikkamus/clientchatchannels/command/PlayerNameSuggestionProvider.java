package com.rikkamus.clientchatchannels.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.concurrent.CompletableFuture;

public class PlayerNameSuggestionProvider<T extends SharedSuggestionProvider> implements SuggestionProvider<T> {

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<T> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(context.getSource().getOnlinePlayerNames().stream().sorted(), builder);
    }

}
