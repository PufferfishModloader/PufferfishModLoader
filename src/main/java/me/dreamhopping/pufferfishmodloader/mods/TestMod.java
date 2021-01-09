package me.dreamhopping.pufferfishmodloader.mods;

import me.dreamhopping.pufferfishmodloader.events.EventBus;
import me.dreamhopping.pufferfishmodloader.events.InvokeEvent;
import me.dreamhopping.pufferfishmodloader.events.core.mod.ModInitEvent;
import me.dreamhopping.pufferfishmodloader.mods.core.Mod;

@Mod("bruh")
public class TestMod {
    public TestMod() {
        EventBus.INSTANCE.register(this);
    }

    @InvokeEvent
    public void onInit(ModInitEvent event) {
    }
}
