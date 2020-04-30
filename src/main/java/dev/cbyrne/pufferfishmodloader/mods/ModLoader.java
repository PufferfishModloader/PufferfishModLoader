package dev.cbyrne.pufferfishmodloader.mods;

import com.sun.org.apache.xpath.internal.operations.Mod;
import dev.cbyrne.pufferfishmodloader.PufferfishModLoader;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ModLoader {
    public static final ModLoader INSTANCE = new ModLoader();
    public final File modDir = new File(PufferfishModLoader.INSTANCE.gameDir, "pmlmods");
    private final ArrayList<File> availableMods = new ArrayList<>();
    private final ArrayList<Mod> loadedMods = new ArrayList<>();
    private final ArrayList<File> invalidMods = new ArrayList<>();

    public void discoverMods() {
        PufferfishModLoader.INSTANCE.logger.info("Discovering mods...");

        if(modDir.exists()) {
            availableMods.addAll(Arrays.asList(Objects.requireNonNull(modDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".jar")))));
        } else {
            boolean success = modDir.mkdir();
            if(success) {
                PufferfishModLoader.INSTANCE.logger.info("Created pmlmods Directory");
            } else {
                PufferfishModLoader.INSTANCE.logger.error("Failed to create pmlmods directory");
            }
        }

        PufferfishModLoader.INSTANCE.logger.info("PufferfishModLoader found {} mods to load!", availableMods.size());
    }

    public void loadMods() {
        if(availableMods.size() >= 1) {
            PufferfishModLoader.INSTANCE.logger.info("Loading {} mods", availableMods.size());
        } else {
            PufferfishModLoader.INSTANCE.logger.warn("There is no mods to load!");
        }
    }
}
