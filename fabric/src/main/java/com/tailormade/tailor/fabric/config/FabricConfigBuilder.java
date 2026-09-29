package com.tailormade.tailor.fabric.config;

import com.google.gson.*;
import com.tailormade.tailor.config.ConfigBuilder;
import com.tailormade.tailor.config.ConfigValue;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Supplier;

public class FabricConfigBuilder implements ConfigBuilder {
    private final Deque<String> categories = new ArrayDeque<>();
    private final List<ConfigEntry<?>> entries = new ArrayList<>();

    @Override
    public void push(String category) {
        categories.addLast(category);
    }

    @Override
    public void pop() {
        categories.removeLast();
    }

    private List<String> currentPath(String name) {
        List<String> path = new ArrayList<>(categories);
        path.add(name);
        return path;
    }

    @Override
    public ConfigValue<Integer> defineInt(String name, String comment, int defaultValue, int min, int max) {
        IntEntry entry = new IntEntry(currentPath(name), comment, defaultValue, min, max);
        entries.add(entry);
        return entry;
    }

    @Override
    public ConfigValue<Float> defineFloat(String name, String comment, float defaultValue) {
        FloatEntry entry = new FloatEntry(currentPath(name), comment, defaultValue);
        entries.add(entry);
        return entry;
    }

    @Override
    public ConfigValue<Boolean> defineBoolean(String name, String comment, boolean defaultValue) {
        BooleanEntry entry = new BooleanEntry(currentPath(name), comment, defaultValue);
        entries.add(entry);
        return entry;
    }

    @Override
    public ConfigValue<String> defineString(String name, String comment, String defaultValue) {
        StringEntry entry = new StringEntry(currentPath(name), comment, defaultValue);
        entries.add(entry);
        return entry;
    }

    public void load(Path file) {
        JsonObject root = new JsonObject();
        if (Files.exists(file)) {
            try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                JsonElement parsed = JsonParser.parseReader(reader);
                if (parsed.isJsonObject()) {
                    root = parsed.getAsJsonObject();
                }
            } catch (IOException | JsonParseException e) {
                System.out.println("[TailorMade] Failed to read config, using defaults: " + file + " (" + e.getMessage() + ")");
            }
        }

        for (ConfigEntry<?> entry : entries) {
            entry.readFrom(root);
        }

        for (ConfigEntry<?> entry : entries) {
            entry.saveFile = file; // set() 時にどこへ書き戻すか覚えさせる
        }

        JsonObject rebuilt = new JsonObject();
        for (ConfigEntry<?> entry : entries) {
            entry.writeTo(rebuilt);
        }

        try {
            Files.createDirectories(file.getParent());
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Files.writeString(file, gson.toJson(rebuilt), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("[TailorMade] Failed to write config: " + file + " (" + e.getMessage() + ")");
        }
    }

    static void persist(ConfigEntry<?> changed, Path file) {
        persistTyped(changed, file);
    }

    private static <T> void persistTyped(ConfigEntry<T> changed, Path file) {
        if (file == null) return;

        JsonObject root = new JsonObject();
        if (Files.exists(file)) {
            try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                JsonElement parsed = JsonParser.parseReader(reader);
                if (parsed.isJsonObject()) root = parsed.getAsJsonObject();
            } catch (IOException | JsonParseException ignored) {}
        }

        JsonObject current = root;
        for (int i = 0; i < changed.path.size() - 1; i++) {
            current = getOrCreateChild(current, changed.path.get(i));
        }
        String leaf = changed.path.get(changed.path.size() - 1);
        current.add(leaf, changed.toJson(changed.value));

        try {
            Files.createDirectories(file.getParent());
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Files.writeString(file, gson.toJson(root), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("[TailorMade] Failed to persist config change: " + file + " (" + e.getMessage() + ")");
        }
    }

    private static JsonObject getOrCreateChild(JsonObject parent, String key) {
        JsonElement child = parent.get(key);
        if (child instanceof JsonObject obj) return obj;
        JsonObject obj = new JsonObject();
        parent.add(key, obj);
        return obj;
    }

    private static abstract class ConfigEntry<T> implements ConfigValue<T> {
        final List<String> path;
        final String comment;
        final T defaultValue;
        Path saveFile;
        T value;

        ConfigEntry(List<String> path, String comment, T defaultValue) {
            this.path = path;
            this.comment = comment;
            this.defaultValue = defaultValue;
            this.value = defaultValue;
        }

        @Override
        public T get() { return value; }
        @Override
        public void set(T v) {
            this.value = v;
            FabricConfigBuilder.persist(this, saveFile);
        }

        void readFrom(JsonObject root) {
            JsonObject current = root;
            for (int i = 0; i < path.size() - 1; i++) {
                JsonElement child = current.get(path.get(i));
                if (!(child instanceof JsonObject obj)) return; // 該当階層なし → デフォルト維持
                current = obj;
            }
            String leaf = path.get(path.size() - 1);
            if (current.has(leaf)) {
                try {
                    value = parse(current.get(leaf));
                } catch (Exception e) {
                    value = defaultValue;
                }
            }
        }

        void writeTo(JsonObject root) {
            JsonObject current = root;
            for (int i = 0; i < path.size() - 1; i++) {
                current = getOrCreateChild(current, path.get(i));
            }
            String leaf = path.get(path.size() - 1);
            current.add(leaf, toJson(value));
            if (comment != null && !comment.isBlank()) {
                current.addProperty(leaf + "_comment", comment);
            }
        }

        abstract T parse(JsonElement el);
        abstract JsonElement toJson(T value);
    }

    private static final class IntEntry extends ConfigEntry<Integer> {
        final int min, max;
        IntEntry(List<String> path, String comment, int defaultValue, int min, int max) {
            super(path, comment, defaultValue);
            this.min = min;
            this.max = max;
        }
        @Override
        Integer parse(JsonElement el) {
            int v = el.getAsInt();
            return Math.min(max, Math.max(min, v));
        }
        @Override
        JsonElement toJson(Integer value) { return new JsonPrimitive(value); }
        @Override
        public void set(Integer v) {
            super.set(Math.min(max, Math.max(min, v)));
        }
    }

    private static final class FloatEntry extends ConfigEntry<Float> {
        FloatEntry(List<String> path, String comment, float defaultValue) {
            super(path, comment, defaultValue);
        }
        @Override
        Float parse(JsonElement el) { return el.getAsFloat(); }
        @Override
        JsonElement toJson(Float value) { return new JsonPrimitive(value); }
    }

    private static final class BooleanEntry extends ConfigEntry<Boolean> {
        BooleanEntry(List<String> path, String comment, boolean defaultValue) {
            super(path, comment, defaultValue);
        }
        @Override
        Boolean parse(JsonElement el) { return el.getAsBoolean(); }
        @Override
        JsonElement toJson(Boolean value) { return new JsonPrimitive(value); }
    }

    private static final class StringEntry extends ConfigEntry<String> {
        StringEntry(List<String> path, String comment, String defaultValue) {
            super(path, comment, defaultValue);
        }
        @Override
        String parse(JsonElement el) { return el.getAsString(); }
        @Override
        JsonElement toJson(String value) { return new JsonPrimitive(value); }
    }
}