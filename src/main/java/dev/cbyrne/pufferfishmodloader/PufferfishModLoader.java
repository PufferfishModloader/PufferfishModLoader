package dev.cbyrne.pufferfishmodloader;

import dev.cbyrne.pufferfishmodloader.api.Minecraft;
import dev.cbyrne.pufferfishmodloader.events.InvokeEvent;
import dev.cbyrne.pufferfishmodloader.events.core.GameStartEvent;
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
    public MCVersion mcVersion = Minecraft.getInstance().getVersion();

    @InvokeEvent
    public void onStart(GameStartEvent event) {
        logger.info("PML Started!");

        ModLoader.INSTANCE.addModDirectory(new File(gameDir, "pmlmods"));
        ModLoader.INSTANCE.addModDirectory(new File(gameDir, "pmlmods/" + mcVersion.version));

        try {
            ModLoader.INSTANCE.discoverMods();
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
