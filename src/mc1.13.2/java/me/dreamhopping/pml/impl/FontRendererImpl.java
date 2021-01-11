package me.dreamhopping.pml.impl;

import me.dreamhopping.pml.api.client.gui.FontRenderer;
import net.minecraft.client.Minecraft;

public class FontRendererImpl extends FontRenderer {
    public int drawString(String text, float x, float y, int color, boolean drawShadow) {
        if (!drawShadow) {
            return Minecraft.getInstance().fontRenderer.drawString(text, x, y, color);
        } else {
            return Minecraft.getInstance().fontRenderer.drawStringWithShadow(text, x, y, color);
        }
    }
}
