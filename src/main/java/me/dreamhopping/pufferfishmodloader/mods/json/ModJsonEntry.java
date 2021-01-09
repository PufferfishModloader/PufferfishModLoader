package me.dreamhopping.pufferfishmodloader.mods.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModJsonEntry {
    private final String id;
    private final String version;
    private final List<String> dependencies;
    @SerializedName("optional_dependencies")
    private final List<String> optionalDependencies;
    @SerializedName("load_before")
    private final List<String> loadBefore;
    private final List<String> incompatibilities;
    private final List<Library> libraries;

    public ModJsonEntry(String id, String version, List<String> dependencies, List<String> optionalDependencies, List<String> loadBefore, List<String> incompatibilities, List<Library> libraries) {
        this.id = id;
        this.version = version;
        this.dependencies = dependencies;
        this.optionalDependencies = optionalDependencies;
        this.loadBefore = loadBefore;
        this.incompatibilities = incompatibilities;
        this.libraries = libraries;
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public List<String> getOptionalDependencies() {
        return optionalDependencies;
    }

    public List<String> getLoadBefore() {
        return loadBefore;
    }

    public List<String> getIncompatibilities() {
        return incompatibilities;
    }

    public List<Library> getLibraries() {
        return libraries;
    }
}
