package me.dreamhopping.pml.impl;

import me.dreamhopping.pml.api.client.gui.FontRenderer;
import me.dreamhopping.pml.mods.core.MCVersion;
import net.minecraft.client.Minecraft;

import java.util.Map;

public class FontRendererImpl extends FontRenderer {
    public int drawString(String text, float x, float y, int color, boolean drawShadow) {
        return Minecraft.getMinecraft().fontRendererObj.drawString(text, x, y, color, drawShadow);
    }
}
