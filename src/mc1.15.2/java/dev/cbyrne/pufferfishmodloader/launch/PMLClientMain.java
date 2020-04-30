package dev.cbyrne.pufferfishmodloader.launch;

import dev.cbyrne.pufferfishmodloader.PufferfishModLoader;
import dev.cbyrne.pufferfishmodloader.mods.loader.ModLoader;
import net.minecraft.client.main.Main;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;

public class PMLClientMain {
    public static void main(String... args) throws MalformedURLException, URISyntaxException {
        PufferfishModLoader.INSTANCE.gameDir = new File(args[Arrays.asList(args).indexOf("--gameDir") + 1]);
        PufferfishModLoader.INSTANCE.preInit();

        ModLoader.main(args);
        // Main.main(args);
    }
}
