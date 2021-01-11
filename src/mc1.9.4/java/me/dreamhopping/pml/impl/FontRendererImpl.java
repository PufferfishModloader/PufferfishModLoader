package me.dreamhopping.pml.impl;

import me.dreamhopping.pml.api.client.gui.FontRenderer;
import net.minecraft.client.Minecraft;

public class FontRendererImpl extends FontRenderer {
    public int drawString(String text, float x, float y, int color, boolean drawShadow) {
        return Minecraft.getMinecraft().fontRendererObj.drawString(text, x, y, color, drawShadow);
    }
}
