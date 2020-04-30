package dev.cbyrne.pufferfishmodloader.mods.loader;

import dev.cbyrne.pufferfishmodloader.mods.core.Mod;
import dev.cbyrne.pufferfishmodloader.mods.json.ModJsonEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ModLoader {
    public static final ModLoader INSTANCE = new ModLoader();
    private static final String[] BLACKLISTED_PACKAGES = {
            "java/",
            "javax/",
            "com/sun/",
            "jdk/internal/",
            "sun/"
    };
    private static final Logger LOGGER = LogManager.getLogger();
    private final List<File> modDirectories = new ArrayList<>();
    private final Map<String, RegisteredMod> mods = new HashMap<>();
    private ModLoaderState state = ModLoaderState.INITIALIZING;

    // This class shouldn't be initialized; only accessed using the INSTANCE field.
    private ModLoader() {
    }

    public RegisteredMod getModByClass(Class<?> clazz) {
        return null;
    }

    public void addModDirectory(File directory) {
        assert state == ModLoaderState.INITIALIZING;
        LOGGER.debug("Adding mod directory {}", directory);
        modDirectories.add(directory);
    }

    public void discoverMods() {
        assert state == ModLoaderState.INITIALIZING;
        LOGGER.debug("Discovering mods in classpath and directories {}", modDirectories);
        state = ModLoaderState.DISCOVERING;
    }

    private List<Class<?>> discoverMods(URLClassLoader loader, File location) {
        assert state == ModLoaderState.DISCOVERING;
        List<Class<?>> classes = new ArrayList<>();
        if (location.isFile() && (location.getName().endsWith(".jar") || location.getName().endsWith(".zip"))) {
            try (ZipFile file = new ZipFile(location)) {
                Enumeration<? extends ZipEntry> entries = file.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();

                    if (entry.getName().endsWith(".class")) {
                        try {
                            Class<?> clazz = Class.forName(getClassNameFromPath(entry.getName()), false, loader);
                            if (clazz.getAnnotation(Mod.class) != null) {
                                classes.add(clazz);
                            }
                        } catch (ClassNotFoundException e) {
                            LOGGER.error("Couldn't get class from class file at {}", entry.getName(), e);
                        }
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Couldn't read mod file at {}", location, e);
            }
        } else if (location.isDirectory()) {
            // Assume this mod isn't zipped; that's fine, we'll deal with that.
            Stack<File> stack = new Stack<>();
            stack.push(location);
            while (!stack.isEmpty()) {
                File current = stack.pop();
                File[] contents = current.listFiles();
                if (contents != null) {
                    for (File f : contents) {
                        if (f.isFile()) {
                            if (f.getName().endsWith(".class")) {
                                try {
                                    String relativePath = f.getPath().substring(location.getPath().length() + 1);
                                    Class<?> clazz = Class.forName(
                                            getClassNameFromPath(relativePath),
                                            false,
                                            loader
                                    );
                                    LOGGER.info("Checking " + clazz);
                                    if (clazz.getAnnotation(Mod.class) != null) {
                                        classes.add(clazz);
                                    }
                                } catch (ClassNotFoundException e) {
                                    LOGGER.error("Couldn't get class from class file at {}", f, e);
                                }
                            }
                        } else {
                            stack.push(f);
                        }
                    }
                }
            }
        } else {
            LOGGER.warn("Don't know how to discover mods in {}", location);
        }
        LOGGER.debug("Found {} mod classes in {}", classes.size(), location);
        return classes;
    }

    private String getClassNameFromPath(String path) {
        return path.replace('/', '.')
                .replace('\\', '.') // For some reason paths can include both / and \ on windows; we need to deal with that
                .substring(0, path.length() - ".class".length());
    }

    private ModJsonEntry createFallbackDefinition(String id) {
        return new ModJsonEntry(id,
                "dev",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    public static void main(String... args) throws URISyntaxException, MalformedURLException {
        URLClassLoader loader = new URLClassLoader(new URL[]{ModLoader.class.getProtectionDomain().getCodeSource().getLocation()});
        System.out.println(ModLoader.INSTANCE.discoverMods(loader, new File(loader.getURLs()[0].toURI())));
    }
}
