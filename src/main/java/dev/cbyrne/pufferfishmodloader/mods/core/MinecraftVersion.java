package dev.cbyrne.pufferfishmodloader.mods.core;

public enum MinecraftVersion {
    v_1_15_2("1.15.2"),
    allVersions("ALL");

    private final String version;
    MinecraftVersion(String version) {
        this.version = version;
    }
}
