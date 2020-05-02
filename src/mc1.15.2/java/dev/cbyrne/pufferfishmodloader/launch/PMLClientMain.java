package dev.cbyrne.pufferfishmodloader.launch;

import dev.cbyrne.pufferfishmodloader.PufferfishModLoader;
import dev.cbyrne.pufferfishmodloader.api.Minecraft;
import dev.cbyrne.pufferfishmodloader.events.Event;
import dev.cbyrne.pufferfishmodloader.events.EventBus;
import dev.cbyrne.pufferfishmodloader.events.core.GameStartEvent;
import dev.cbyrne.pufferfishmodloader.impl.MinecraftImpl;
import dev.cbyrne.pufferfishmodloader.mods.core.MCVersion;
import net.minecraft.MinecraftVersion;
import net.minecraft.client.main.Main;

import java.io.File;
import java.util.Arrays;

public class PMLClientMain {
    public static void main(String... args) {
        Minecraft.setInstance(new MinecraftImpl());
        PufferfishModLoader.INSTANCE.gameDir = new File(args[Arrays.asList(args).indexOf("--gameDir") + 1]);
        EventBus.INSTANCE.register(PufferfishModLoader.INSTANCE);
        EventBus.INSTANCE.post(new GameStartEvent());

        Main.main(args);
    }
}
