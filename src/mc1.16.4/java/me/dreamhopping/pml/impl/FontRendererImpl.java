package me.dreamhopping.pml.impl;

import me.dreamhopping.pml.api.client.gui.FontRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringVisitable;

import java.util.ArrayList;
import java.util.List;

public class FontRendererImpl extends FontRenderer {
    public int drawString(String text, float x, float y, int color, boolean drawShadow) {
        if (!drawShadow) {
            return MinecraftClient.getInstance().textRenderer.draw(new MatrixStack(), text, x, y, color);
        } else {
            return MinecraftClient.getInstance().textRenderer.drawWithShadow(new MatrixStack(), text, x, y, color);
        }
    }

    public int drawString(String text, int x, int y, int color) {
        return MinecraftClient.getInstance().textRenderer.draw(new MatrixStack(), text, x, y, color);
    }

    public int drawStringWithShadow(String text, float x, float y, int color) {
        return MinecraftClient.getInstance().textRenderer.drawWithShadow(new MatrixStack(), text, x, y, color);
    }

    public void drawSplitString(String text, int x, int y, int width, int color) {
        MinecraftClient.getInstance().textRenderer.drawTrimmed(StringVisitable.plain(text), x, y, color, width);
    }

    public boolean getUnicodeFlag() {
        return true;
    }

    public void setUnicodeFlag(boolean unicodeFlag) {
        // Do nothing
    }

    public int getStringWidth(String string) {
        return MinecraftClient.getInstance().textRenderer.getWidth(string);
    }

    public int getCharWidth(char character) {
        return getStringWidth(String.valueOf(character));
    }

    public int splitStringWidth(String string, int width) {
        return MinecraftClient.getInstance().textRenderer.fontHeight * this.listFormattedStringToWidth(string, width).size();
    }

    public List<String> listFormattedStringToWidth(String string, int width) {
        List<String> list = new ArrayList<>();
        MinecraftClient.getInstance().textRenderer.wrapLines(StringVisitable.plain(string), width).forEach(item -> list.add(item.toString()));

        return list;
    }

    public String trimStringToWidth(String string, int width) {
        return MinecraftClient.getInstance().textRenderer.trimToWidth(string, width);
    }

    public String trimStringToWidth(String string, int width, boolean var2) {
        return MinecraftClient.getInstance().textRenderer.trimToWidth(string, width, var2);
    }
}