package me.dreamhopping.pml.launch;

import me.dreamhopping.pml.PufferfishModLoader;
import me.dreamhopping.pml.events.EventBus;
import me.dreamhopping.pml.events.impl.GameStartEvent;
import net.minecraft.client.main.Main;

import java.io.File;
import java.util.Arrays;

public class PMLEntryPoint {
    public static void start(String[] args, boolean server) { // Called by PMLClientMain and PMLServerMain via reflection
        PufferfishModLoader.INSTANCE.logger.info("PML starting...");
        PufferfishModLoader.INSTANCE.gameDir = new File(args[Arrays.asList(args).indexOf("--gameDir") + 1]);
        PufferfishModLoader.INSTANCE.loadMods();

        EventBus.INSTANCE.register(PufferfishModLoader.INSTANCE);
        EventBus.INSTANCE.post(new GameStartEvent());

        Main.main(args);
    }
}
