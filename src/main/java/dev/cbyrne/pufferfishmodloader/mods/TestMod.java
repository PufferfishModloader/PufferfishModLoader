package dev.cbyrne.pufferfishmodloader.mods;

import dev.cbyrne.pufferfishmodloader.events.EventBus;
import dev.cbyrne.pufferfishmodloader.events.InvokeEvent;
import dev.cbyrne.pufferfishmodloader.events.core.mod.ModInitEvent;
import dev.cbyrne.pufferfishmodloader.mods.core.Mod;

@Mod("bruh")
public class TestMod {
    public TestMod() {
        EventBus.INSTANCE.register(this);
    }

    @InvokeEvent
    public void onInit(ModInitEvent event) {
    }
}
