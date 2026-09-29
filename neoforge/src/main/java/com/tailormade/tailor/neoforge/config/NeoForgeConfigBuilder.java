package com.tailormade.tailor.neoforge.config;

import com.tailormade.tailor.config.ConfigBuilder;
import com.tailormade.tailor.config.ConfigValue;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.Supplier;

public class NeoForgeConfigBuilder implements ConfigBuilder {
    final ModConfigSpec.Builder delegate = new ModConfigSpec.Builder();

    private static <T> ConfigValue<T> wrap(ModConfigSpec.ConfigValue<T> value) {
        return new ConfigValue<>() {
            @Override public T get() { return value.get(); }
            @Override public void set(T v) { value.set(v); }
        };
    }

    @Override
    public void push(String category) { delegate.push(category); }

    @Override
    public void pop() { delegate.pop(); }

    @Override
    public ConfigValue<Float> defineFloat(String name, String comment, float defaultValue) {
        return wrap(delegate.comment(comment).define(name, defaultValue));
    }

    @Override
    public ConfigValue<Integer> defineInt(String name, String comment, int defaultValue, int min, int max) {
        return wrap(delegate.comment(comment).defineInRange(name, defaultValue, min, max));
    }

    @Override
    public ConfigValue<Boolean> defineBoolean(String name, String comment, boolean defaultValue) {
        return wrap(delegate.comment(comment).define(name, defaultValue));
    }

    @Override
    public ConfigValue<String> defineString(String name, String comment, String defaultValue) {
        return wrap(delegate.comment(comment).define(name, defaultValue));
    }
}