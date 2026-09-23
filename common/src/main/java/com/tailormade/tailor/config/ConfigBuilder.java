package com.tailormade.tailor.config;

import java.util.function.Supplier;

public interface ConfigBuilder {
    void push(String category);
    void pop();
    Supplier<Integer> defineInt(String name, String comment, int defaultValue, int min, int max);
    Supplier<Boolean> defineBoolean(String name, String comment, boolean defaultValue);
    Supplier<String> defineString(String name, String comment, String defaultValue);
}