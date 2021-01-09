package me.dreamhopping.pml.impl;

import me.dreamhopping.pml.mods.core.MCVersion;
import me.dreamhopping.pml.api.Minecraft;

public class MinecraftImpl extends Minecraft {
    @Override
    public MCVersion getVersion() {
        return MCVersion.V1_14_4;
    }
}
