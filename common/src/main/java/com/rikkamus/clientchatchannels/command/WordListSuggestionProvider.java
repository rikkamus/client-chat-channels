package com.rikkamus.clientchatchannels.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class WordListSuggestionProvider<T> implements SuggestionProvider<T> {

    private final SuggestionProvider<T> wordSuggestionProvider;

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<T> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        final String input = builder.getInput();

        int lastWhitespaceIndex = -1;

        for (int i = input.length() - 1; i >= 0; i--) {
            if (Character.isWhitespace(input.charAt(i))) {
                lastWhitespaceIndex = i;
                break;
            }
        }

        return this.wordSuggestionProvider.getSuggestions(context, builder.createOffset(lastWhitespaceIndex + 1));
    }

}
