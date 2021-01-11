package me.dreamhopping.pml.impl;

import com.google.common.collect.Maps;
import me.dreamhopping.pml.api.client.Minecraft;
import me.dreamhopping.pml.mods.core.MCVersion;
import net.minecraft.util.Session;

import java.util.Map;

public class MinecraftImpl extends Minecraft {
    public MCVersion getVersion() {
        return MCVersion.V1_13_2;
    }

    public boolean is64Bit() {
        return net.minecraft.client.Minecraft.getInstance().isJava64bit();
    }

    public boolean isGamePaused() {
        return net.minecraft.client.Minecraft.getInstance().isGamePaused();
    }

    public int getFPS() {
        return net.minecraft.client.Minecraft.getDebugFPS();
    }

    public Map<String, String> getSessionInfo() {
        final Session session = net.minecraft.client.Minecraft.getInstance().getSession();

        Map<String, String> sessionInfo = Maps.newHashMap();
        sessionInfo.put("X-Minecraft-Username", session.getUsername());
        sessionInfo.put("X-Minecraft-UUID", session.getPlayerID());
        sessionInfo.put("X-Minecraft-Version", getVersion().version);
        return sessionInfo;
    }
}
