package me.dreamhopping.pml.mods.loader.util.mc;

import me.dreamhopping.pml.main.transformer.AccessFixer;
import me.dreamhopping.pml.mods.loader.util.PMLClassLoader;
import me.dreamhopping.pml.mods.loader.util.ResourceLoader;
import me.dreamhopping.pml.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.*;

public class TransformingClassLoader extends PMLClassLoader {
    private final List<RuntimeTransformer> transformers = new ArrayList<>();
    private final List<String> loadWithSystemLoader = new ArrayList<>();

    public TransformingClassLoader(List<ResourceLoader> loaders, List<ClassLoader> parents) {
        super(loaders, parents);

        registerTransformer(new AccessFixer());
    }

    public void loadWithSystemLoader(String... classNames) {
        loadWithSystemLoader.addAll(Arrays.asList(classNames));
    }

    public void registerTransformer(RuntimeTransformer transformer) {
        transformers.add(transformer);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (loadWithSystemLoader.contains(name)) return ClassLoader.getSystemClassLoader().loadClass(name);

        String path = name.replace('.', '/');
        boolean isMinecraft = !path.contains("/") || path.startsWith("net/minecraft");
        String unmappedName = isMinecraft ? unmap(path) : path;
        String n = unmappedName + ".class";
        Pair<ResourceLoader, URL> resourceInfo = getResourceInfo(n);
        if (resourceInfo == null) throw new ClassNotFoundException(name);
        byte[] data;
        try (InputStream stream = resourceInfo.getSecond().openStream()) {
            data = toByteArray(stream);
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        }
        CodeSource source;
        try {
            source = createCodeSource(resourceInfo.getFirst(), name, n, resourceInfo.getSecond());
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        }
        String mappedName = isMinecraft ? map(path) : path;
        if (isMinecraft) data = process(mappedName, data);
        return defineClass(mappedName.replace('/', '.'), data, 0, data.length, source);
    }

    private byte[] process(String name, byte[] bytes) throws ClassNotFoundException {
        if (bytes == null) throw new ClassNotFoundException(name);
        for (RuntimeTransformer transformer : transformers) {
            if (transformer.willTransform(name)) {
                bytes = transformer.transform(name, bytes);
            }
        }
        return bytes;
    }

    private String map(String name) {
        for (RuntimeTransformer transformer : transformers) {
            name = transformer.mapName(name);
        }
        return name;
    }

    private String unmap(String name) {
        for (RuntimeTransformer transformer : transformers) {
            name = transformer.unmapName(name);
        }
        return name;
    }

    @Override
    protected URL findResource(String name) {
        if (name.equals("log4j2.xml")) return findResource("pml.log4j2.xml");
        return super.findResource(name);
    }
}
