package me.dreamhopping.pufferfishmodloader.mods.launch.loader;

import org.objectweb.asm.tree.ClassNode;

public interface RuntimeTransformer {
    boolean willTransform(String name);

    default String transformName(String original) {
        return original;
    }

    ClassNode transform(ClassNode node);
}
