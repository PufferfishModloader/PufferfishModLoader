package me.dreamhopping.pml.mods.loader.util.impl;

import me.dreamhopping.pml.mods.loader.util.ResourceLoader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class DirectoryResourceLoader implements ResourceLoader {
    private final File directory;

    public DirectoryResourceLoader(File directory) {
        this.directory = directory;
    }

    @Override
    public URL loadResource(String path) throws MalformedURLException {
        File file = new File(directory, path);
        if (!file.exists()) return null;
        return file.toURI().toURL();
    }

    @Override
    public URL getBaseUrl() {
        try {
            return directory.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getFiles() {
        return getFileList(directory);
    }

    @Override
    public void close() throws IOException {

    }

    private static List<String> getFileList(File directory) {
        List<String> rv = new ArrayList<>();
        Stack<File> stack = new Stack<>();
        stack.add(directory);

        int baseLen = directory.getPath().length() + File.pathSeparator.length();

        while (!stack.isEmpty()) {
            File dir = stack.pop();
            File[] list = dir.listFiles();
            if (list != null) {
                for (File file : list) {
                    if (file.isDirectory()) {
                        stack.push(file);
                    } else {
                        rv.add(file.getPath().substring(baseLen));
                    }
                }
            }
        }

        return rv;
    }
}
