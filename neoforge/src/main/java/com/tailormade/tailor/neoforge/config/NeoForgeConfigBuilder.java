package com.tailormade.tailor.neoforge.config;

import com.tailormade.tailor.config.ConfigBuilder;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.Supplier;

public class NeoForgeConfigBuilder implements ConfigBuilder {
    final ModConfigSpec.Builder delegate = new ModConfigSpec.Builder();

    @Override
    public void push(String category) { delegate.push(category); }

    @Override
    public void pop() { delegate.pop(); }

    @Override
    public Supplier<Integer> defineInt(String name, String comment, int defaultValue, int min, int max) {
        return delegate.comment(comment).defineInRange(name, defaultValue, min, max);
    }

    @Override
    public Supplier<Boolean> defineBoolean(String name, String comment, boolean defaultValue) {
        return delegate.comment(comment).define(name, defaultValue);
    }

    @Override
    public Supplier<String> defineString(String name, String comment, String defaultValue) {
        return delegate.comment(comment).define(name, defaultValue);
    }
}