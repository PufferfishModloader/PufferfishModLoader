package me.dreamhopping.pml.main;

import java.io.File;
import java.util.List;

public class ClassPathData {
    private final List<File> baseClassPath;
    private final List<File> extra;

    public ClassPathData(List<File> baseClassPath, List<File> extra) {
        this.baseClassPath = baseClassPath;
        this.extra = extra;
    }

    public List<File> getBaseClassPath() {
        return baseClassPath;
    }

    public List<File> getExtra() {
        return extra;
    }

}
