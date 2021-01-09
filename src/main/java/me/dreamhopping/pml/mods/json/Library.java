package me.dreamhopping.pml.mods.json;

import java.net.URL;

public class Library {
    private final URL url;
    private final String sha256;

    public Library(URL url, String sha256) {
        this.url = url;
        this.sha256 = sha256;
    }

    public URL getUrl() {
        return url;
    }

    public String getSha256() {
        return sha256;
    }
}
