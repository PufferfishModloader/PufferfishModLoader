package me.dreamhopping.pml.mods.loader.util.impl;

import me.dreamhopping.pml.mods.loader.util.ResourceLoader;

import java.net.URL;
import java.util.Collections;
import java.util.List;

public class NoopResourceLoader implements ResourceLoader {
    public static final NoopResourceLoader INSTANCE = new NoopResourceLoader();

    private NoopResourceLoader() {

    }

    @Override
    public URL loadResource(String path) {
        return null;
    }

    @Override
    public URL getBaseUrl() {
        return null;
    }

    @Override
    public List<String> getFiles() {
        return Collections.emptyList();
    }

    @Override
    public void close() {

    }
}
