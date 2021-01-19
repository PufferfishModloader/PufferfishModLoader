package me.dreamhopping.pml.launch.transformer;

import me.dreamhopping.pml.mods.loader.util.mc.RuntimeTransformer;

/**
 * Transforms all package-private and protected classes, methods and fields to public.
 * This is necessary because Yarn (and possibly MCP) sometimes put classes
 * referencing package-private fields in different packages.
 *
 * Note that this uses a primitive custom class file parser as PML cannot use any external libraries. Reference:
 * https://docs.oracle.com/javase/specs/jvms/se15/html/jvms-4.html
 */
public class AccessFixer implements RuntimeTransformer {
    private static final int PUBLIC = 0x0001;
    private static final int PRIVATE = 0x0002;
    private static final int PROTECTED = 0x0004;
    private static final int ALL_ACCESS = (PUBLIC | PRIVATE | PROTECTED);
    private static final int[] CP_SIZES = {
            -1, // Undefined
            -1, // Utf8
            -1, // Undefined
            4, // Integer
            4, // Float
            8, // Long
            8, // Double
            2, // Class
            2, // String
            4, // FieldRef
            4, // MethodRef
            4, // InterfaceMethodRef
            4, // NameAndType
            -1, // Undefined
            -1, // Undefined
            3, // MethodHandle
            2, // MethodType
            4, // Dynamic
            4, // InvokeDynamic
            2, // Module
            2, // Package
    };

    public boolean willTransform(String name) {
        return true;
    }

    @Override
    public byte[] transform(String name, byte[] data) {
        if (readInt(data, 0) != 0xCAFEBABE) {
            throw new IllegalStateException("Invalid magic number");
        }

        int currentPos = 8;
        int cpCount = readShort(data, currentPos);
        currentPos += 2;

        for (int i = 1; i < cpCount; i++) {
            byte tag = data[currentPos++];
            if (tag == 1) {
                currentPos += readShort(data, currentPos) + 2;
            } else {
                int size = CP_SIZES[tag];
                if (size == -1)
                    throw new IllegalStateException("invalid size " + size + " for tag " + tag);
                currentPos += size;
                if (tag == 5 || tag == 6)
                    i++;
            }
        }

        int classAccess = readShort(data, currentPos);
        writeShort(data, currentPos, transformAccess(classAccess));
        currentPos += 6;
        int interfaceCount = readShort(data, currentPos);
        currentPos += 2 + 2 * interfaceCount;
        currentPos = readNodeList(data, currentPos); // fields
        readNodeList(data, currentPos); // methods
        return data;
    }

    private static int readNodeList(byte[] data, int currentPos) {
        int count = readShort(data, currentPos);
        currentPos += 2;
        for (int i = 0; i < count; i++) {
            int access = readShort(data, currentPos);
            writeShort(data, currentPos, transformAccess(access));
            currentPos += 6;
            int attributeCount = readShort(data, currentPos);
            currentPos += 2;
            for (int j = 0; j < attributeCount; j++) {
                currentPos += 6 + readInt(data, currentPos + 2);
            }
        }
        return currentPos;
    }

    private static int readShort(byte[] data, int offset) {
        int b0 = data[offset] & 0xff;
        int b1 = data[offset + 1] & 0xff;
        return (b0 << 8) | b1;
    }

    private static int readInt(byte[] data, int offset) {
        int b0 = data[offset] & 0xff;
        int b1 = data[offset + 1] & 0xff;
        int b2 = data[offset + 2] & 0xff;
        int b3 = data[offset + 3] & 0xff;
        return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
    }

    private static void writeShort(byte[] data, int offset, int value) {
        byte b1 = (byte) ((value >> 8) & 0xff);
        byte b2 = (byte) (value & 0xff);
        data[offset] = b1;
        data[offset + 1] = b2;
    }

    private static int transformAccess(int nodeAccess) {
        if ((nodeAccess & ALL_ACCESS) != PRIVATE) {
            return (nodeAccess & ~ALL_ACCESS) | PUBLIC;
        }

        return nodeAccess;
    }
}
