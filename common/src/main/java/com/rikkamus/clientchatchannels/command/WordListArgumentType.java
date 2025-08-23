package com.rikkamus.clientchatchannels.command;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WordListArgumentType implements ArgumentType<List<String>> {

    private static final DynamicCommandExceptionType UNEXPECTED_SYMBOL = new DynamicCommandExceptionType(
        character -> new LiteralMessage(String.format("Unexpected symbol '%s'", character))
    );

    private static final Collection<String> EXAMPLES = List.of("a b c", "1 2 3");

    @Override
    public List<String> parse(StringReader reader) throws CommandSyntaxException {
        List<String> list = new ArrayList<>();

        while (reader.canRead()) {
            reader.skipWhitespace();

            String word = reader.readUnquotedString();

            if (word.isEmpty()) {
                if (reader.canRead()) throw WordListArgumentType.UNEXPECTED_SYMBOL.createWithContext(reader, reader.read());
                else break;
            }

            list.add(word);
        }

        return list;
    }

    @Override
    public Collection<String> getExamples() {
        return WordListArgumentType.EXAMPLES;
    }

}
