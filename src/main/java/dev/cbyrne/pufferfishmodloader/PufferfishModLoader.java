package dev.cbyrne.pufferfishmodloader;

import dev.cbyrne.pufferfishmodloader.mods.loader.ModLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

public class PufferfishModLoader {
    public static final PufferfishModLoader INSTANCE = new PufferfishModLoader();
    public final Logger logger = LogManager.getLogger("PufferfishModLoader");
    public File gameDir;

    public void preInit() {
        logger.info("PML Started!");

        ModLoader.INSTANCE.addModDirectory(new File(gameDir, "pmlmods"));
        try {
            ModLoader.INSTANCE.discoverMods();
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
