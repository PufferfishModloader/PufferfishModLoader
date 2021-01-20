package me.dreamhopping.pml.main;

import me.dreamhopping.pml.main.transformer.AccessFixer;
import me.dreamhopping.pml.mods.loader.util.PMLClassLoader;
import me.dreamhopping.pml.mods.loader.util.ResourceLoader;
import me.dreamhopping.pml.mods.loader.util.impl.DirectoryResourceLoader;
import me.dreamhopping.pml.mods.loader.util.impl.JarResourceLoader;
import me.dreamhopping.pml.mods.loader.util.impl.NoopResourceLoader;
import me.dreamhopping.pml.mods.loader.util.mc.RuntimeTransformer;
import me.dreamhopping.pml.mods.loader.util.mc.TransformingClassLoader;
import me.dreamhopping.pml.util.Pair;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class PMLLauncher {
    public static void start(String[] args, boolean client) throws Throwable {
        Properties properties = new Properties();
        try (InputStream stream = PMLLauncher.class.getResourceAsStream("/pml.properties")) {
            properties.load(stream);
        }

        String packageName = "me.dreamhopping.pml." + properties.getProperty("version-package-name");
        String entryPointName = packageName + ".launch.PMLEntryPoint";

        ClassPathData data = ClassPathMapper.INSTANCE.scanClassPath(entryPointName);

        ClassLoader gameLoader = buildMinecraftClassLoader(data);

        Thread.currentThread().setContextClassLoader(gameLoader);

        Class<?> cl = Class.forName(entryPointName, true, gameLoader);
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
                PMLClassLoader.class.getName(),
                RuntimeTransformer.class.getName(),
                TransformingClassLoader.class.getName()
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
