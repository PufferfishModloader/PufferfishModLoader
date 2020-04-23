package dev.cbyrne.pufferfishmodloader.mods.test;

import dev.cbyrne.pufferfishmodloader.mods.core.Mod;

@Mod(modid = "mymod")
public class MyMod {
    void initialize() {
        System.out.println("Hello World!");
    }
}
