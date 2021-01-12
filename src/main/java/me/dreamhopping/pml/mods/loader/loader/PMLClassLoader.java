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
        Class<?> clazz = null;

        try {
            clazz = super.findClass(name);
        } catch (NoClassDefFoundError e) {
            boolean foundClass = false;
            int searchedParents = 0;

            while (!foundClass && searchedParents < this.parents.size()) {
                // Failed to load class, search parents
                for (ClassLoader classLoader : this.parents) {
                    try {
                        clazz = classLoader.loadClass(name);

                        if (clazz != null) {
                            foundClass = true;
                            break;
                        }
                    } catch (Exception ignored) {
                    }

                    searchedParents++;
                }
            }
        } catch (Error | Exception e) {
            return null;
        }


        if (clazz == null) {
            throw new ClassNotFoundException(name);
        }

        return clazz;
    }

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> clazz = null;

        try {
            clazz = super.loadClass(name);
        } catch (ClassNotFoundException e) {
            boolean foundClass = false;
            int searchedParents = 0;

            while (!foundClass && searchedParents < this.parents.size()) {
                // Failed to load class, search parents
                for (ClassLoader classLoader : this.parents) {
                    try {
                        clazz = classLoader.loadClass(name);

                        if (clazz != null) {
                            foundClass = true;
                            break;
                        }
                    } catch (Exception ignored) {
                    }

                    searchedParents++;
                }
            }
        } catch (Error | Exception e) {
            return null;
        }

        if (clazz == null) {
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
