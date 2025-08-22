package com.rikkamus.clientchatchannels.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigValueSupplier<T> implements Supplier<T> {

    public static <T> ConfigValueSupplier<T> ofConfigValue(Supplier<T> configValueSupplier) {
        return new ConfigValueSupplier<>(configValueSupplier, false);
    }

    public static <T> ConfigValueSupplier<T> ofOverriddenValue(Supplier<T> overridenValueSupplier) {
        return new ConfigValueSupplier<>(overridenValueSupplier, true);
    }

    public static <T> ConfigValueSupplier<T> ofOverriddenValue(T value) {
        return ConfigValueSupplier.ofOverriddenValue(() -> value);
    }

    private final Supplier<T> valueSupplier;

    @Getter
    private final boolean overridden;

    @Override
    public T get() {
        return this.valueSupplier.get();
    }

    public boolean isUsingConfigValue() {
        return !this.overridden;
    }

}
