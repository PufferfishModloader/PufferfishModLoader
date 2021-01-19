package me.dreamhopping.pml;

import me.dreamhopping.pml.launch.ClassPathData;
import me.dreamhopping.pml.mods.loader.ModLoader;

import java.io.File;

public class PufferfishModLoader {
    public static final PufferfishModLoader INSTANCE = new PufferfishModLoader();

    public void initialize(File gameDir, ClassPathData classPathData) {
        ModLoader.INSTANCE.addModDirectories(new File(gameDir, "pmlmods"), new File(gameDir, "mods"));
        ModLoader.INSTANCE.discoverMods(classPathData);
    }
}
