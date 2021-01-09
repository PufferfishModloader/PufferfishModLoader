package me.dreamhopping.pufferfishmodloader.impl;

import me.dreamhopping.pufferfishmodloader.api.Minecraft;
import me.dreamhopping.pufferfishmodloader.mods.core.MCVersion;

public class MinecraftImpl extends Minecraft {
    @Override
    public MCVersion getVersion() {
        return MCVersion.V1_16_4;
    }
}
