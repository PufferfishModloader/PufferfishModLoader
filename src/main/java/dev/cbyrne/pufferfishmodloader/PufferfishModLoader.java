package dev.cbyrne.pufferfishmodloader;

import dev.cbyrne.pufferfishmodloader.mods.ModLoader;

import java.io.File;

public class PufferfishModLoader {
    public static final PufferfishModLoader INSTANCE = new PufferfishModLoader();
    public File modsDir = new File("pmlmods");

    public static void main(String... args) {
        ModLoader.INSTANCE.start();
    }
}
