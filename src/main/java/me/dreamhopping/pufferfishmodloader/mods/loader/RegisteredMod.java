package me.dreamhopping.pufferfishmodloader.mods.loader;

import me.dreamhopping.pufferfishmodloader.mods.json.ModJsonEntry;

public class RegisteredMod {
    private final Object implementation;
    private ModJsonEntry definition;
    private ModState state = ModState.LOADING;

    public RegisteredMod(ModJsonEntry definition, Object implementation) {
        this.definition = definition;
        this.implementation = implementation;
    }

    public ModJsonEntry getDefinition() {
        return definition;
    }

    public void setDefinition(ModJsonEntry definition) {
        this.definition = definition;
    }

    public Object getImplementation() {
        return implementation;
    }

    public ModState getState() {
        return state;
    }

    public void setState(ModState state) {
        this.state = state;
    }
}
