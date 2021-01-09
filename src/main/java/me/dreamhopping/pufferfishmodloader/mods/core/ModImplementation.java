package me.dreamhopping.pufferfishmodloader.mods.core;

import me.dreamhopping.pufferfishmodloader.mods.json.ModJsonEntry;
import me.dreamhopping.pufferfishmodloader.mods.loader.ModLoader;
import me.dreamhopping.pufferfishmodloader.mods.loader.RegisteredMod;

/**
 * Optional class mod implementations can extend to get easier access to utilities.
 */
public class ModImplementation {
    private RegisteredMod getMod() {
        return ModLoader.INSTANCE.getModByClass(getClass());
    }

    protected String getId() {
        return getDefinition().getId();
    }

    protected String getVersion() {
        return getDefinition().getVersion();
    }

    protected ModJsonEntry getDefinition() {
        return getMod().getDefinition();
    }
}
