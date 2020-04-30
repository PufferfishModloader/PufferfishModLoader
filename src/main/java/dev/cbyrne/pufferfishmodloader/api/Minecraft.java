package dev.cbyrne.pufferfishmodloader.api;

import dev.cbyrne.pufferfishmodloader.mods.core.MCVersion;

public abstract class Minecraft {
    private static Minecraft impl;

    public abstract MCVersion getVersion();

    public static void setInstance(Minecraft instance) {
        if (impl != null) throw new IllegalStateException("Instance already set");
        impl = instance;
    }

    public static Minecraft getInstance() {
        return impl;
    }
}
