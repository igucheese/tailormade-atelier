package com.tailormade.tailor.config;

import java.util.function.Supplier;

public interface ConfigBuilder {
    void push(String category);
    void pop();
    ConfigValue<Integer> defineInt(String name, String comment, int defaultValue, int min, int max);
    ConfigValue<Float> defineFloat(String name, String comment, float defaultValue);
    ConfigValue<Boolean> defineBoolean(String name, String comment, boolean defaultValue);
    ConfigValue<String> defineString(String name, String comment, String defaultValue);
}