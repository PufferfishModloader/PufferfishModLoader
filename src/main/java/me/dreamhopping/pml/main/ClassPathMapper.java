package me.dreamhopping.pml.main;

import java.io.*;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

public class ClassPathMapper {
    public static ClassPathMapper INSTANCE = new ClassPathMapper();

    private ClassPathMapper() {

    }

    public ClassPathData scanClassPath(String entryPoint) throws IOException {
        List<String> hashes = getMcHashes();
        List<File> base = new ArrayList<>();
        List<File> extra = new ArrayList<>();

        String[] entries = System.getProperty("java.class.path").split(File.pathSeparator);

        for (String s : entries) {
            if (s.startsWith(System.getProperty("java.home") + File.separator)) continue;
            File file = new File(s).getAbsoluteFile();
            if (!file.exists()) continue;

            EntryType type = getEntryType(file, hashes, entryPoint);
            switch (type) {
                case MINECRAFT:
                    base.add(file);
                    break;
                case EXTRA:
                    extra.add(file);
                    break;
            }
        }

        return new ClassPathData(base, extra);
    }

    private EntryType getEntryType(File file, List<String> hashes, String entryPoint) throws IOException {
        if (hashes == null) return EntryType.MINECRAFT;
        try {
            File self = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            if (self.getAbsoluteFile().equals(file)) return EntryType.MINECRAFT;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        if (file.getName().endsWith(".jar")) {
            try (ZipFile f = new ZipFile(file)) {
                // the only class that is always deobfuscated in both client and server
                if (f.getEntry("net/minecraft/server/MinecraftServer.class") != null
                        || f.getEntry("log4j2.xml") != null
                        || f.getEntry("pml.properties") != null
                        || f.getEntry(entryPoint.replace('.', '/') + ".class") != null) {
                    return EntryType.MINECRAFT;
                }
            }
        }
        if (file.isDirectory()) {
            File pmlProperties = new File(file, "pml.properties");
            if (pmlProperties.exists()) return EntryType.MINECRAFT;
            File entryPointClass = new File(file, entryPoint.replace('.', '/') + ".class");
            if (entryPointClass.exists()) return EntryType.MINECRAFT;
            return EntryType.EXTRA;
        }
        String hash = sha1(file);
        for (String s : hashes) {
            if (hash.equals(s)) return EntryType.MINECRAFT;
        }
        return EntryType.EXTRA;
    }

    private List<String> getMcHashes() throws IOException {
        InputStream stream = ClassPathMapper.class.getResourceAsStream("/pml-classpath.txt");
        if (stream == null) {
            System.out.println("[WARN] Could not find class path data - assuming we are in a PML development environment.");
            System.out.println("[WARN] Adding all class path entries to the Minecraft class path");
            return null;
        }
        List<String> rv = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                rv.add(line);
            }
        }
        return rv;
    }

    private String sha1(File file) throws IOException {
        try (InputStream stream = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");

            byte[] buffer = new byte[4096];
            while (true) {
                int bytesRead = stream.read(buffer, 0, buffer.length);
                if (bytesRead < 0) break;
                digest.update(buffer, 0, bytesRead);
            }

            return toHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Should be impossible unless you have a broken JRE", e);
        }
    }

    private String toHexString(byte[] buf) {
        StringBuilder builder = new StringBuilder();

        for (byte b : buf) {
            builder.append(String.format("%02x", b));
        }

        return builder.toString();
    }

    private enum EntryType {
        MINECRAFT,
        EXTRA
    }
}
