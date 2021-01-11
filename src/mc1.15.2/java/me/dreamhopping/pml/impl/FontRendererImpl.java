package me.dreamhopping.pml.impl;

import me.dreamhopping.pml.api.client.gui.FontRenderer;
import net.minecraft.client.MinecraftClient;

public class FontRendererImpl extends FontRenderer {
    public int drawString(String text, float x, float y, int color, boolean drawShadow) {
        if (!drawShadow) {
            return MinecraftClient.getInstance().textRenderer.draw(text, x, y, color);
        } else {
            return MinecraftClient.getInstance().textRenderer.drawWithShadow(text, x, y, color);
        }
    }
}
