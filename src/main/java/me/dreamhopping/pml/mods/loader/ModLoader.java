package me.dreamhopping.pml.mods.loader;

import me.dreamhopping.pml.mods.core.Mod;
import me.dreamhopping.pml.mods.json.ModJsonEntry;
import me.dreamhopping.pml.mods.loader.loader.PMLClassLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ModLoader {
    public static final ModLoader INSTANCE = new ModLoader();
    /*private static final String[] BLACKLISTED_PACKAGES = {
            "java/",
            "javax/",
            "com/sun/",
            "jdk/internal/",
            "sun/"
    };*/
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

    public void addModDirectory(File... directories) {
        assert state == ModLoaderState.INITIALIZING;
        modDirectories.addAll(Arrays.asList(directories));
        LOGGER.info("Added mod directories {}", modDirectories);
    }

    public void discoverMods() throws MalformedURLException {
        assert state == ModLoaderState.INITIALIZING;
        LOGGER.info("Discovering mods in classpath and directories: {}", modDirectories);
        state = ModLoaderState.DISCOVERING;

        ArrayList<URL> availableModsURL = new ArrayList<>();
        for (File directory : modDirectories) {
            if (!directory.exists()) {
                LOGGER.info("{} doesn't exist! Creating...", directory);
                boolean success = directory.mkdir();
                if (!success) {
                    LOGGER.warn("Failed to create {}, skipping!", directory);
                    break;
                }
            }

            if (directory.listFiles() == null) {
                LOGGER.warn("No files found in {}, skipping!", directory);
            } else {
                File[] validMods = Objects.requireNonNull(directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".jar")));
                for (File mod : validMods) {
                    availableModsURL.add(mod.toURI().toURL());
                }
            }
        }

        LOGGER.info("Found {} mod file(s) to load from directories", availableModsURL.size());

        String[] javaClasspath = System.getProperty("java.class.path").split(File.pathSeparator);
        for (String entry : javaClasspath) {
            if (!entry.startsWith(System.getProperty("java.home"))) {
                availableModsURL.add(new File(entry).toURI().toURL());
            }
        }

        URL[] availableModsURLArray = availableModsURL.toArray(new URL[0]);
        PMLClassLoader loader = new PMLClassLoader(availableModsURLArray, Collections.singletonList(this.getClass().getClassLoader()));
        List<Class<?>> modClasses = ModLoader.INSTANCE.discoverMods(loader, availableModsURLArray);

        LOGGER.info("Found {} mod classes in directories + classpath", modClasses.size());

        // Make a new instance of the classes
        for (Class<?> clazz : modClasses) {
            try {
                clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Class<?>> discoverMods(URLClassLoader loader, URL[] locations) {
        assert state == ModLoaderState.DISCOVERING;
        List<Class<?>> classes = new ArrayList<>();

        for (URL urlLocation : locations) {
            File location = null;
            try {
                location = new File(urlLocation.toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            assert location != null;
            if (location.isFile() && (location.getName().endsWith(".jar") || location.getName().endsWith(".zip"))) {
                try (ZipFile file = new ZipFile(location)) {
                    if (file.getEntry("mods.json") == null) continue;

                    Enumeration<? extends ZipEntry> entries = file.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry entry = entries.nextElement();

                        if (entry.getName().endsWith(".class")) {
                            try {
                                Class<?> clazz;

                                if (loader != Mod.class.getClassLoader()) {
                                    clazz = Class.forName(
                                            getClassNameFromPath(entry.getName()),
                                            false,
                                            Mod.class.getClassLoader()
                                    );
                                } else {
                                    clazz = Class.forName(
                                            getClassNameFromPath(entry.getName()),
                                            false,
                                            loader
                                    );
                                }

                                // LOGGER.info("Checking " + clazz);
                                if (clazz.getAnnotation(Mod.class) != null) {
                                    classes.add(clazz);
                                }
                            } catch (Exception | Error e) {
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

                                        Class<?> clazz;
                                        if (loader != Mod.class.getClassLoader()) {
                                            clazz = Class.forName(
                                                    getClassNameFromPath(relativePath),
                                                    false,
                                                    Mod.class.getClassLoader()
                                            );
                                        } else {
                                            clazz = Class.forName(
                                                    getClassNameFromPath(relativePath),
                                                    false,
                                                    loader
                                            );
                                        }

                                        // LOGGER.info("Checking " + clazz);
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
        }

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
}
