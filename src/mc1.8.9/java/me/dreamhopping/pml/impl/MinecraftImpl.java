package me.dreamhopping.pml.impl;

import me.dreamhopping.pml.api.client.Minecraft;
import me.dreamhopping.pml.mods.core.MCVersion;

import java.util.Map;

public class MinecraftImpl extends Minecraft {
    public MCVersion getVersion() {
        return MCVersion.V1_8_9;
    }

    public boolean is64Bit() {
        return net.minecraft.client.Minecraft.getMinecraft().isJava64bit();
    }

    public boolean isGamePaused() {
        return net.minecraft.client.Minecraft.getMinecraft().isGamePaused();
    }

    public int getFPS() {
        return net.minecraft.client.Minecraft.getDebugFPS();
    }

    public Map<String, String> getSessionInfo() {
        return net.minecraft.client.Minecraft.getSessionInfo();
    }
}
