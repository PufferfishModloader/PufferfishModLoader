package me.dreamhopping.pml.impl;

import me.dreamhopping.pml.api.Minecraft;
import me.dreamhopping.pml.mods.core.MCVersion;

public class MinecraftImpl extends Minecraft {
    @Override
    public MCVersion getVersion() {
        return MCVersion.V1_9_4;
    }
}
