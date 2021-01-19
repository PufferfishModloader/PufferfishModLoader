package me.dreamhopping.pml.mods.loader.util.mc;

public interface RuntimeTransformer {
    boolean willTransform(String name);

    default String mapName(String original) {
        return original;
    }

    default String unmapName(String original) {
        return original;
    }

    byte[] transform(String name, byte[] data);
}
