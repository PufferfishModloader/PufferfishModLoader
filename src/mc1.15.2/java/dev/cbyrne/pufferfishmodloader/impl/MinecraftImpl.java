package dev.cbyrne.pufferfishmodloader.impl;

import dev.cbyrne.pufferfishmodloader.api.Minecraft;
import dev.cbyrne.pufferfishmodloader.mods.core.MCVersion;

public class MinecraftImpl extends Minecraft {
    @Override
    public MCVersion getVersion() {
        return MCVersion.V1_15_2;
    }
}
