package dev.cbyrne.pufferfishmodloader;

import dev.cbyrne.pufferfishmodloader.events.EventBus;
import dev.cbyrne.pufferfishmodloader.events.InvokeEvent;
import dev.cbyrne.pufferfishmodloader.events.core.mod.ModInitEvent;
import dev.cbyrne.pufferfishmodloader.mods.core.Mod;
import org.apache.logging.log4j.LogManager;

@Mod("bruh")
public class TestMod {
    public TestMod() {
        EventBus.INSTANCE.register(this);
    }

    @InvokeEvent
    public void onInit(ModInitEvent event) {
        LogManager.getLogger("TestMod").info("TestMod started!");
    }
}
