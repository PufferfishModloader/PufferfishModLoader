package me.dreamhopping.pml.mods.loader.util.impl;

import me.dreamhopping.pml.mods.loader.util.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class JarResourceLoader implements ResourceLoader {
    private final JarFile file;
    private final URL base;

    public JarResourceLoader(File file) throws IOException {
        this.file = new JarFile(file);
        this.base = new URL("jar", "", -1, file.toURI().toURL() + "!/", null);
    }

    @Override
    public URL loadResource(String path) throws IOException {
        ZipEntry entry = file.getEntry(path);
        if (entry == null) return null;
        return new URL(base, entry.getName());
    }

    @Override
    public URL getBaseUrl() {
        try {
            return new File(file.getName()).toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getFiles() {
        List<String> list = new ArrayList<>();

        Enumeration<? extends ZipEntry> entries = file.entries();
        while (entries.hasMoreElements()) {
            list.add(entries.nextElement().getName());
        }

        return list;
    }

    @Override
    public void close() throws IOException {
        file.close();
    }
}
