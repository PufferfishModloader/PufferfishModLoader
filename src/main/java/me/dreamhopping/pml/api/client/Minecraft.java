package me.dreamhopping.pml.api.client;

import me.dreamhopping.pml.api.client.gui.FontRenderer;
import me.dreamhopping.pml.mods.core.MCVersion;

import java.util.Map;

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

    public abstract boolean is64Bit();

    public abstract boolean isGamePaused();

    public abstract int getFPS();

    public abstract Map<String, String> getSessionInfo();

    public FontRenderer getFontRenderer() {
        return FontRenderer.getInstance();
    }
}
