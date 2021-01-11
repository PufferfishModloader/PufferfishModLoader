package me.dreamhopping.pml.api.client.gui;

public abstract class FontRenderer {
    private static FontRenderer impl;

    public static FontRenderer getInstance() {
        return impl;
    }

    public static void setInstance(FontRenderer instance) {
        if (impl != null) throw new IllegalStateException("Instance already set");
        impl = instance;
    }

    public abstract int drawString(String text, float x, float y, int color, boolean drawShadow);
}
