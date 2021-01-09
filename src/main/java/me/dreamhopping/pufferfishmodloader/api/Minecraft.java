package me.dreamhopping.pufferfishmodloader.api;

import me.dreamhopping.pufferfishmodloader.mods.core.MCVersion;

public abstract class Minecraft {
    private static Minecraft impl;

    public static Minecraft getInstance() {
        return impl;
    }

    public static void setInstance(Minecraft instance) {
        if (impl != null) throw new IllegalStateException("Instance already set");
        impl = instance;
    }

    public abstract MCVersion getVersion();
}
