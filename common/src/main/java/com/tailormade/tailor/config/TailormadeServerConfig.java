package com.tailormade.tailor.config;

public class TailormadeServerConfig {
    private TailormadeServerConfig() {}

    public static void define(ConfigBuilder builder) {
        builder.push("Tailormade Server Config");
        // TODO: 設定可能値を追加していく
        // builder.defineInt とか defineBoolean(...) とかを追加
        builder.pop();
    }
}
