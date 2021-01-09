package me.dreamhopping.pufferfishmodloader;

import me.dreamhopping.pufferfishmodloader.api.Minecraft;
import me.dreamhopping.pufferfishmodloader.events.InvokeEvent;
import me.dreamhopping.pufferfishmodloader.events.core.GameStartEvent;
import me.dreamhopping.pufferfishmodloader.mods.core.MCVersion;
import me.dreamhopping.pufferfishmodloader.mods.loader.ModLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;

public class PufferfishModLoader {
    public static final PufferfishModLoader INSTANCE = new PufferfishModLoader();
    public final Logger logger = LogManager.getLogger("PufferfishModLoader");
    public File gameDir;
    public MCVersion mcVersion = Minecraft.getInstance().getVersion();

    @InvokeEvent
    public void onStart(GameStartEvent event) {
        ModLoader.INSTANCE.addModDirectory(new File(gameDir, "pmlmods"), new File(gameDir, "pmlmods/" + mcVersion.version));

        try {
            ModLoader.INSTANCE.discoverMods();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        logger.info("PML Started!");
    }
}
