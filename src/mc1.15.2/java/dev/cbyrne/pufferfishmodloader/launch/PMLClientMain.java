package dev.cbyrne.pufferfishmodloader.launch;

import dev.cbyrne.pufferfishmodloader.PufferfishModLoader;
import dev.cbyrne.pufferfishmodloader.mods.core.MCVersion;
import net.minecraft.MinecraftVersion;
import net.minecraft.client.main.Main;

import java.io.File;
import java.util.Arrays;

public class PMLClientMain {
    public static void main(String... args) {
        PufferfishModLoader.INSTANCE.gameDir = new File(args[Arrays.asList(args).indexOf("--gameDir") + 1]);
        PufferfishModLoader.INSTANCE.mcVersion = MCVersion.valueOf(MinecraftVersion.create().getName());
        PufferfishModLoader.INSTANCE.preInit();

        Main.main(args);
    }
}
