package me.dreamhopping.pml.mods.loader.util;

import me.dreamhopping.pml.util.Pair;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.SecureClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class PMLClassLoader extends SecureClassLoader implements Closeable {
    private final List<ResourceLoader> loaders;
    private final List<ClassLoader> parents;

    public PMLClassLoader(List<ResourceLoader> loaders, List<ClassLoader> parents) {
        super(null);
        this.loaders = loaders;
        this.parents = parents;
    }

    // Parent handling

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            Class<?> c = findLoadedClass(name);
            if (c != null) return c;

            for (ClassLoader parent : parents) {
                try {
                    return parent.loadClass(name);
                } catch (ClassNotFoundException ignored) {

                }
            }

            return findClass(name);
        }
    }

    @Override
    public URL getResource(String name) {
        for (ClassLoader loader : parents) {
            URL u = loader.getResource(name);
            if (u != null) {
                return u;
            }
        }

        return findResource(name);
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        @SuppressWarnings("unchecked") Enumeration<URL>[] enumerations = new Enumeration[parents.size() + 1];

        for (int i = 0; i < parents.size(); i++) {
            enumerations[i] = parents.get(i).getResources(name);
        }

        enumerations[parents.size()] = findResources(name);

        return new CompoundEnumeration<>(enumerations);
    }

    // Basic ClassLoader method implementations

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String path = name.replace('.', '/') + ".class";
        Pair<ResourceLoader, URL> info = getResourceInfo(path);
        if (info == null) throw new ClassNotFoundException(name);
        byte[] data;
        try (InputStream stream = info.getSecond().openStream()) {
            data = toByteArray(stream);
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        }
        CodeSource source;
        try {
            source = createCodeSource(info.getFirst(), name, path, info.getSecond());
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        }
        return defineClass(name, data, 0, data.length, source);
    }

    protected CodeSource createCodeSource(ResourceLoader loader, String name, String fileName, URL url) throws IOException {
        int packageSplitIndex = name.lastIndexOf('.');
        String packageName = packageSplitIndex == -1 ? "" : name.substring(0, packageSplitIndex);

        URLConnection connection = url.openConnection();

        CodeSigner[] signers = null;

        if (packageSplitIndex >= 0 && !name.startsWith("net.minecraft.")) {
            if (connection instanceof JarURLConnection) {
                JarFile file = ((JarURLConnection) connection).getJarFile();

                if (file != null && file.getManifest() != null) {
                    Manifest manifest = file.getManifest();
                    JarEntry entry = file.getJarEntry(fileName);

                    signers = entry.getCodeSigners();
                    if (getPackage(packageName) == null) {
                        defineJarPackage(packageName, manifest, ((JarURLConnection) connection).getJarFileURL());
                    }
                }
            } else {
                if (getPackage(packageName) == null) {
                    definePackage(packageName, null, null, null, null, null, null, null);
                }
            }
        }

        return new CodeSource(loader.getBaseUrl(), signers);
    }

    private void defineJarPackage(String name, Manifest manifest, URL codeSourceUrl) {
        String specTitle = null, specVersion = null, specVendor = null;
        String implTitle = null, implVersion = null, implVendor = null;
        String sealed = null;
        URL sealBase = null;

        Attributes attr = manifest.getMainAttributes();
        if (attr != null) {
            specTitle = attr.getValue(Attributes.Name.SPECIFICATION_TITLE);
            specVersion = attr.getValue(Attributes.Name.SPECIFICATION_VERSION);
            specVendor = attr.getValue(Attributes.Name.SPECIFICATION_VENDOR);
            implTitle = attr.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
            implVersion = attr.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
            implVendor = attr.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
            sealed = attr.getValue(Attributes.Name.SEALED);
        }
        if ("true".equalsIgnoreCase(sealed)) {
            sealBase = codeSourceUrl;
        }
        definePackage(name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
    }

    @Override
    protected URL findResource(String name) {
        Pair<ResourceLoader, URL> info = getResourceInfo(name);
        return info == null ? null : info.getSecond();
    }

    @Override
    protected Enumeration<URL> findResources(String name) {
        URL url = findResource(name);
        if (url != null) return new ArrayEnumeration<>(new URL[]{url});
        return new ArrayEnumeration<>(new URL[0]);
    }

    protected static byte[] toByteArray(InputStream stream) throws IOException {
        byte[] result = new byte[0];
        byte[] buffer = new byte[4096];
        while (true) {
            int bytesRead = stream.read(buffer, 0, buffer.length);
            if (bytesRead < 0) break;
            int newLength = result.length + bytesRead;
            byte[] newResult = new byte[newLength];
            System.arraycopy(result, 0, newResult, 0, result.length);
            System.arraycopy(buffer, 0, newResult, result.length, bytesRead);
            result = newResult;
        }
        return result;
    }

    @Override
    public void close() throws IOException {
        for (ResourceLoader loader : loaders) {
            loader.close();
        }
    }

    public List<ClassLoader> getParents() {
        return parents;
    }

    protected Pair<ResourceLoader, URL> getResourceInfo(String name) {
        for (ResourceLoader loader : loaders) {
            try {
                URL l = loader.loadResource(name);
                if (l != null) return new Pair<>(loader, l);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<ResourceLoader> getLoaders() {
        return loaders;
    }

    private static class ArrayEnumeration<T> implements Enumeration<T> {
        private final T[] values;
        private int index;

        public ArrayEnumeration(T[] values) {
            this.values = values;
        }

        @Override
        public boolean hasMoreElements() {
            return values.length > index;
        }

        @Override
        public T nextElement() {
            if (index >= values.length) throw new NoSuchElementException();
            return values[index++];
        }
    }

    private static class CompoundEnumeration<T> implements Enumeration<T> {
        private final Enumeration<T>[] enums;
        private int index = 0;

        private CompoundEnumeration(Enumeration<T>[] enums) {
            this.enums = enums;
        }

        @Override
        public boolean hasMoreElements() {
            return selectNext();
        }

        @Override
        public T nextElement() {
            if (!selectNext()) throw new NoSuchElementException();
            return enums[index].nextElement();
        }

        private boolean selectNext() {
            while (index < enums.length) {
                if (enums[index].hasMoreElements()) return true;
                index++;
            }
            return false;
        }
    }
}
