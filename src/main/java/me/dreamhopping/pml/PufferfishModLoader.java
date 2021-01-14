package me.dreamhopping.pml;

import me.dreamhopping.pml.events.EventBus;
import me.dreamhopping.pml.events.InvokeEvent;
import me.dreamhopping.pml.events.impl.GameStartEvent;
import me.dreamhopping.pml.events.impl.mod.ModInitEvent;
import me.dreamhopping.pml.mods.loader.ModLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;

public class PufferfishModLoader {
    public static final PufferfishModLoader INSTANCE = new PufferfishModLoader();
    public final Logger logger = LogManager.getLogger("PufferfishModLoader");
    public File gameDir;

    @InvokeEvent
    public void onStart(GameStartEvent event) {
        logger.info("PML Started!");

        // Call ModInitEvent to initialise all mods
        EventBus.INSTANCE.post(new ModInitEvent());
    }

    public void loadMods() {
        ModLoader.INSTANCE.addModDirectory(new File(gameDir, "pmlmods"), new File(gameDir, "mods"));

        try {
            ModLoader.INSTANCE.discoverMods();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
