package me.dreamhopping.pml.impl;

import me.dreamhopping.pml.api.client.gui.FontRenderer;
import net.minecraft.client.Minecraft;

import java.util.List;

public class FontRendererImpl extends FontRenderer {
    public int drawString(String text, float x, float y, int color, boolean drawShadow) {
        if (!drawShadow) {
            return Minecraft.getInstance().fontRenderer.drawString(text, x, y, color);
        } else {
            return Minecraft.getInstance().fontRenderer.drawStringWithShadow(text, x, y, color);
        }
    }

    public int drawString(String text, int x, int y, int color) {
        return Minecraft.getInstance().fontRenderer.drawString(text, x, y, color);
    }

    public int drawStringWithShadow(String text, float x, float y, int color) {
        return Minecraft.getInstance().fontRenderer.drawStringWithShadow(text, x, y, color);
    }

    public void drawSplitString(String text, int x, int y, int width, int color) {
        Minecraft.getInstance().fontRenderer.drawSplitString(text, x, y, color, width);
    }

    public boolean getUnicodeFlag() {
        return true;
    }

    public void setUnicodeFlag(boolean unicodeFlag) {
        // Do nothing
    }

    public int getStringWidth(String string) {
        return Minecraft.getInstance().fontRenderer.getStringWidth(string);
    }

    public int getCharWidth(char character) {
        return getStringWidth(String.valueOf(character));
    }

    public int splitStringWidth(String string, int width) {
        return Minecraft.getInstance().fontRenderer.FONT_HEIGHT * this.listFormattedStringToWidth(string, width).size();
    }

    public List<String> listFormattedStringToWidth(String string, int width) {
        return Minecraft.getInstance().fontRenderer.listFormattedStringToWidth(string, width);
    }

    public String trimStringToWidth(String string, int width) {
        return Minecraft.getInstance().fontRenderer.trimStringToWidth(string, width);
    }

    public String trimStringToWidth(String string, int width, boolean var2) {
        return Minecraft.getInstance().fontRenderer.trimStringToWidth(string, width, var2);
    }
}