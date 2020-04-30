package dev.cbyrne.pufferfishmodloader.launch;

import dev.cbyrne.pufferfishmodloader.PufferfishModLoader;
import net.minecraft.client.main.Main;

import java.io.File;
import java.util.Arrays;

public class PMLClientMain {
    public static void main(String... args) {
        PufferfishModLoader.INSTANCE.gameDir = new File(args[Arrays.asList(args).indexOf("--gameDir") + 1]);
        PufferfishModLoader.INSTANCE.preInit();

        Main.main(args);
    }
}
