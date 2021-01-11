package me.dreamhopping.pml.impl;

import me.dreamhopping.pml.api.client.gui.FontRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class FontRendererImpl extends FontRenderer {
    public int drawString(String text, float x, float y, int color, boolean drawShadow) {
        if (!drawShadow) {
            return MinecraftClient.getInstance().textRenderer.draw(new MatrixStack(), Text.of(text), x, y, color);
        } else {
            return MinecraftClient.getInstance().textRenderer.drawWithShadow(new MatrixStack(), text, x, y, color);
        }
    }
}
