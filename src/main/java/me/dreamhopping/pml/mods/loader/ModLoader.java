package me.dreamhopping.pml.mods.loader;

import me.dreamhopping.pml.launch.ClassPathData;
import me.dreamhopping.pml.launch.PMLLauncher;
import me.dreamhopping.pml.mods.core.Mod;
import me.dreamhopping.pml.mods.loader.util.PMLClassLoader;
import me.dreamhopping.pml.mods.loader.util.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

public class ModLoader {
    public static final ModLoader INSTANCE = new ModLoader();
    private final List<File> modDirectories = new ArrayList<>();
    private ModLoaderState state = ModLoaderState.INITIALIZING;

    private ModLoader() {
    }

    public void addModDirectories(File... directories) {
        assert state == ModLoaderState.INITIALIZING;
        modDirectories.addAll(Arrays.asList(directories));
    }

    public void discoverMods(ClassPathData classPathData) {
        synchronized (this) {
            assert state == ModLoaderState.INITIALIZING;
            state = ModLoaderState.DISCOVERING;

            List<File> modFiles = new ArrayList<>();

            for (File dir : modDirectories) {
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isDirectory() || file.getName().endsWith(".jar")) {
                            modFiles.add(file);
                        }
                    }
                }
            }

            modFiles.addAll(classPathData.getExtra());

            for (File file : modFiles) {
                ResourceLoader loader = null;
                boolean hasMods = false;
                try {
                    loader = PMLLauncher.createResourceLoader(file);
                    PMLClassLoader classLoader = null;

                    for (String path : loader.getFiles()) {
                        if (path.endsWith(".class")) {
                            if (classLoader == null) {
                                classLoader = new PMLClassLoader(loader, new ArrayList<>(1));
                                classLoader.getParents().add(getClass().getClassLoader());

                                try {
                                    URL resource = loader.loadResource("mods.json");
                                    if (resource == null) {
                                        addExtraLibsToClassLoader(classLoader, classPathData);
                                    }
                                } catch (IOException e) {
                                    addExtraLibsToClassLoader(classLoader, classPathData);
                                }
                            }

                            try {
                                Class<?> cl = Class.forName(getClassNameFromPath(path), false, classLoader);

                                if (cl != Mod.class && Mod.class.isAssignableFrom(cl)) {
                                    hasMods = true;
                                    try {
                                        Mod m = (Mod) cl.getConstructor().newInstance();
                                        m.load(); // TODO: Add to mod list and build dependency graph instead of loading immediately
                                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                        System.err.println("Failed to instantiate mod " + cl.getName());
                                        e.printStackTrace();
                                    }
                                }
                            } catch (ClassNotFoundException ignored) {

                            }
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Failed to discover mods in " + file);
                    e.printStackTrace();
                } finally {
                    if (loader != null && !hasMods) {
                        try {
                            loader.close();
                        } catch (IOException e) {
                            System.err.println("Failed to close loader for " + file);
                            e.printStackTrace();
                        }
                    }
                }
            }
            state = ModLoaderState.DISCOVERED;
        }
    }

    private static void addExtraLibsToClassLoader(PMLClassLoader target, ClassPathData data) throws IOException {
        for (File file : data.getExtra()) {
            PMLClassLoader loader = new PMLClassLoader(PMLLauncher.createResourceLoader(file), Collections.singletonList(ModLoader.class.getClassLoader()));
            target.getParents().add(loader);
        }
    }

    private String getClassNameFromPath(String path) {
        return path.replace('/', '.')
                .replace('\\', '.') // For some reason paths can include both / and \ on windows; we need to deal with that
                .substring(0, path.length() - ".class".length());
    }
}
