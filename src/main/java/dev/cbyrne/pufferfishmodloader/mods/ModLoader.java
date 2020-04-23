package dev.cbyrne.pufferfishmodloader.mods;

import dev.cbyrne.pufferfishmodloader.mods.core.Mod;

import java.io.File;
import java.util.ArrayList;

public class ModLoader {
    public static final ModLoader INSTANCE = new ModLoader();

    private final ArrayList<File> availableMods = new ArrayList<>();
    private final ArrayList<Mod> loadedMods = new ArrayList<>();
    private final ArrayList<File> invalidMods = new ArrayList<>();

    public void start() {
        System.out.println("Preparing to load mods...");
        System.out.println("Ready to load!");

        loadMods();
    }

    private void loadMods() {
        System.out.println("Loading mods...");
    }
}
