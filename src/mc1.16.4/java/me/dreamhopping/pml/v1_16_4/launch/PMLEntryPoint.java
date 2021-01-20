package me.dreamhopping.pml.v1_16_4.launch;

import me.dreamhopping.pml.PufferfishModLoader;
import me.dreamhopping.pml.main.ClassPathData;
import net.minecraft.client.main.Main;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class PMLEntryPoint {
    public static void start(String[] args, boolean server, ClassPathData data) throws IOException {
        File workDir;
        if (server) {
            workDir = new File(".").getCanonicalFile();
        } else {
            workDir = new File(args[Arrays.asList(args).indexOf("--gameDir") + 1]);
        }
        PufferfishModLoader.INSTANCE.initialize(workDir, data);

        Main.main(args);
    }
}
