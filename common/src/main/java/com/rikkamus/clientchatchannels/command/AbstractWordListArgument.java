package com.rikkamus.clientchatchannels.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractWordListArgument implements ArgumentType<List<String>> {

    private static final DynamicCommandExceptionType UNEXPECTED_SYMBOL = new DynamicCommandExceptionType(
        character -> Component.translatable("clientchatchannels.argument.word_list.unexpected_symbol", String.valueOf(character))
    );

    private static final Collection<String> EXAMPLES = List.of("a b c", "1 2 3");

    @Override
    public List<String> parse(StringReader reader) throws CommandSyntaxException {
        List<String> list = new ArrayList<>();

        while (reader.canRead()) {
            reader.skipWhitespace();

            String word = reader.readUnquotedString();

            if (word.isEmpty()) {
                if (reader.canRead()) throw AbstractWordListArgument.UNEXPECTED_SYMBOL.createWithContext(reader, reader.read());
                else break;
            }

            list.add(word);
        }

        return list;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        final String input = builder.getInput();

        int lastWhitespaceIndex = -1;

        for (int i = input.length() - 1; i >= 0; i--) {
            if (Character.isWhitespace(input.charAt(i))) {
                lastWhitespaceIndex = i;
                break;
            }
        }

        return listWordSuggestions(context, builder.createOffset(lastWhitespaceIndex + 1));
    }

    protected abstract <S> CompletableFuture<Suggestions> listWordSuggestions(CommandContext<S> context, SuggestionsBuilder builder);

    @Override
    public Collection<String> getExamples() {
        return AbstractWordListArgument.EXAMPLES;
    }

}
