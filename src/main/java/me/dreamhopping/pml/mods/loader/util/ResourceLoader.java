package me.dreamhopping.pml.mods.loader.util;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public interface ResourceLoader extends Closeable {
    URL loadResource(String path) throws IOException;

    URL getBaseUrl();

    List<String> getFiles();
}
