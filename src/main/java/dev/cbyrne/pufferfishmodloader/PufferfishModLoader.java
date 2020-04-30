package dev.cbyrne.pufferfishmodloader;

import dev.cbyrne.pufferfishmodloader.mods.core.MCVersion;
import dev.cbyrne.pufferfishmodloader.mods.loader.ModLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class PufferfishModLoader {
    public static final PufferfishModLoader INSTANCE = new PufferfishModLoader();
    public final Logger logger = LogManager.getLogger("PufferfishModLoader");
    public File gameDir;
    public MCVersion mcVersion;

    public void preInit() {
        logger.info("PML Started!");

        ModLoader.INSTANCE.addModDirectory(new File(gameDir, "pmlmods"));
        ModLoader.INSTANCE.addModDirectory(new File(gameDir, "pmlmods" + File.pathSeparator + mcVersion.name()));

        try {
            ModLoader.INSTANCE.discoverMods();
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
