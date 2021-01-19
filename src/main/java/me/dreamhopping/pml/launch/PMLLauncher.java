package me.dreamhopping.pml.launch;

import me.dreamhopping.pml.mods.loader.util.PMLClassLoader;
import me.dreamhopping.pml.mods.loader.util.ResourceLoader;
import me.dreamhopping.pml.mods.loader.util.impl.DirectoryResourceLoader;
import me.dreamhopping.pml.mods.loader.util.impl.NoopResourceLoader;
import me.dreamhopping.pml.mods.loader.util.impl.JarResourceLoader;
import me.dreamhopping.pml.mods.loader.util.mc.TransformingClassLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PMLLauncher {
    private static final String ENTRY_POINT_CLASS_NAME = PMLLauncher.class.getPackage().getName() + ".PMLEntryPoint";

    public static void start(String[] args, boolean client) throws Throwable {
        ClassPathData data = ClassPathMapper.INSTANCE.scanClassPath();

        ClassLoader gameLoader = buildMinecraftClassLoader(data);

        Thread.currentThread().setContextClassLoader(gameLoader);

        Class<?> cl = Class.forName(ENTRY_POINT_CLASS_NAME, true, gameLoader);
        Method m = cl.getMethod("start", String[].class, boolean.class, ClassPathData.class);

        try {
            m.invoke(null, args, !client, data);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    private static ClassLoader buildMinecraftClassLoader(ClassPathData data) throws IOException {
        List<ResourceLoader> loaders = new ArrayList<>();

        for (File library : data.getBaseClassPath()) {
            loaders.add(createResourceLoader(library));
        }

        TransformingClassLoader loader
                = new TransformingClassLoader(loaders, Collections.singletonList(ClassLoader.getSystemClassLoader().getParent()));
        loader.loadWithSystemLoader(
                PMLLauncher.class.getName(),
                ClassPathData.class.getName(),
                ResourceLoader.class.getName(),
                PMLClassLoader.class.getName()
        );
        return loader;
    }

    public static ResourceLoader createResourceLoader(File file) throws IOException {
        if (file == null) return NoopResourceLoader.INSTANCE;
        if (file.isDirectory()) {
            return new DirectoryResourceLoader(file);
        } else if (file.getName().endsWith(".jar")) {
            return new JarResourceLoader(file);
        } else {
            throw new IllegalArgumentException("Cannot create resource loader for " + file.getName());
        }
    }
}
