package com.tailormade.tailor.config;

import java.util.function.Supplier;

public interface ConfigValue<T> extends Supplier<T> {
    void set(T value);
}
