package dev.cbyrne.pufferfishmodloader.mods;

import com.sun.org.apache.xpath.internal.operations.Mod;
import dev.cbyrne.pufferfishmodloader.PufferfishModLoader;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public class ModLoader {
    public static final ModLoader INSTANCE = new ModLoader();
    public final File modDir = new File(PufferfishModLoader.INSTANCE.gameDir, "pmlmods");
    private final ArrayList<File> availableMods = new ArrayList<>();
    private final ArrayList<Mod> loadedMods = new ArrayList<>();
    private final ArrayList<File> invalidMods = new ArrayList<>();

    public void discoverMods() {
        PufferfishModLoader.INSTANCE.logger.info("Discovering mods...");

        if (modDir.exists()) {
            availableMods.addAll(Arrays.asList(Objects.requireNonNull(modDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".jar")))));
        } else {
            boolean success = modDir.mkdir();
            if (success) {
                PufferfishModLoader.INSTANCE.logger.info("Created pmlmods Directory");
            } else {
                PufferfishModLoader.INSTANCE.logger.error("Failed to create pmlmods directory");
            }
        }

        PufferfishModLoader.INSTANCE.logger.info("PufferfishModLoader found {} mods to load!", availableMods.size());
    }

    public void loadMods() {
        if (availableMods.size() >= 1) {
            for (File file : availableMods) {
                PufferfishModLoader.INSTANCE.logger.info("Loading {}", file.getName());
                try {
                    URLClassLoader modClassLoader = new URLClassLoader(new URL[]{file.toURI().toURL()});

                    ConfigurationBuilder config = new ConfigurationBuilder();
                    config.setClassLoaders(new ClassLoader[]{modClassLoader});
                    config.setUrls(modClassLoader.getURLs());

                    Set<Class<?>> annotated = new Reflections(config).getTypesAnnotatedWith(dev.cbyrne.pufferfishmodloader.mods.core.Mod.class);

                    for (Class<?> clazz : annotated) {
                        PufferfishModLoader.INSTANCE.logger.info("Found class: {}", clazz.getCanonicalName());

                        Method method = clazz.getMethod("start");
                        method.setAccessible(true);
                        method.invoke(clazz.newInstance());
                    }
                } catch (MalformedURLException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                    break;
                }
            }
        } else {
            PufferfishModLoader.INSTANCE.logger.warn("There is no mods to load!");
        }
    }
}
