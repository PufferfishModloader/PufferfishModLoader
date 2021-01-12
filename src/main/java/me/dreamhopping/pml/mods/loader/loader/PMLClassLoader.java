package me.dreamhopping.pml.mods.loader.loader;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PMLClassLoader extends URLClassLoader {
    private final List<ClassLoader> parents;

    public PMLClassLoader(URL[] urls, List<ClassLoader> parents) {
        super(urls, null);
        this.parents = parents;
    }

    public PMLClassLoader(URL[] urls, List<ClassLoader> parents, URLStreamHandlerFactory factory) {
        super(urls, null, factory);
        this.parents = parents;
    }

    public PMLClassLoader(URL[] urls) {
        super(urls);
        this.parents = new ArrayList<>();
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        System.out.println("Finding class " + name);
        Class<?> clazz = null;

        try {
            clazz = super.findClass(name);
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to find class " + name);

            boolean foundClass = false;
            int searchedParents = 0;

            while (!foundClass && searchedParents < this.parents.size()) {
                System.out.println("Searching parents to find class " + name);

                // Failed to load class, search parents
                for (ClassLoader classLoader : this.parents) {
                    System.out.println("Searching in " + classLoader.hashCode() + " for class " + name);

                    try {
                        clazz = classLoader.loadClass(name);

                        if (clazz != null) {
                            foundClass = true;
                            break;
                        }
                    } catch (ClassNotFoundException ignored) {}

                    searchedParents++;
                }
            }
        }

        if (clazz == null) {
            System.out.println("No parents could find class " + name);
            throw new ClassNotFoundException(name);
        }

        return clazz;
    }

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        System.out.println("Loading class " + name);
        Class<?> clazz = null;

        try {
            clazz = super.loadClass(name);
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load class " + name);

            boolean foundClass = false;
            int searchedParents = 0;

            while (!foundClass && searchedParents < this.parents.size()) {
                System.out.println("Iterating over parents to load class " + name);

                // Failed to load class, search parents
                for (ClassLoader classLoader : this.parents) {
                    System.out.println("Attempting to load" + name + " in " + classLoader.hashCode());

                    try {
                        clazz = classLoader.loadClass(name);

                        if (clazz != null) {
                            foundClass = true;
                            break;
                        }
                    } catch (ClassNotFoundException ignored) {}

                    searchedParents++;
                }
            }
        }

        if (clazz == null) {
            System.out.println("No parents could load class " + name);
            throw new ClassNotFoundException(name);
        }

        return clazz;
    }

    public URL findResource(String name) {
        return super.findResource(name);
    }

    public Enumeration<URL> findResources(String name) throws IOException {
        return super.findResources(name);
    }
}
