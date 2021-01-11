package me.dreamhopping.pml.impl;

import com.google.common.collect.Maps;
import me.dreamhopping.pml.mods.core.MCVersion;
import me.dreamhopping.pml.api.client.Minecraft;
import net.minecraft.client.util.Session;

import java.util.Map;

public class MinecraftImpl extends Minecraft {
    public MCVersion getVersion() {
        return MCVersion.V1_14_4;
    }

    public boolean is64Bit() {
        return net.minecraft.client.MinecraftClient.getInstance().is64Bit();
    }

    public boolean isGamePaused() {
        return net.minecraft.client.MinecraftClient.getInstance().isPaused();
    }

    public int getFPS() {
        return net.minecraft.client.MinecraftClient.getCurrentFps();
    }

    public Map<String, String> getSessionInfo() {
        final Session session = net.minecraft.client.MinecraftClient.getInstance().getSession();

        Map<String, String> sessionInfo = Maps.newHashMap();
        sessionInfo.put("X-Minecraft-Username", session.getUsername());
        sessionInfo.put("X-Minecraft-UUID", session.getProfile().getId().toString());
        sessionInfo.put("X-Minecraft-Version", getVersion().version);
        return sessionInfo;
    }
}
