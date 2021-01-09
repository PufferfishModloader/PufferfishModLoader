package me.dreamhopping.pml.mods;

import me.dreamhopping.pml.events.EventBus;
import me.dreamhopping.pml.events.InvokeEvent;
import me.dreamhopping.pml.events.core.mod.ModInitEvent;
import me.dreamhopping.pml.mods.core.Mod;

@Mod("bruh")
public class TestMod {
    public TestMod() {
        EventBus.INSTANCE.register(this);
    }

    @InvokeEvent
    public void onInit(ModInitEvent event) {
    }
}
