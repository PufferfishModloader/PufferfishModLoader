package me.dreamhopping.pufferfishmodloader.launch;

import me.dreamhopping.pufferfishmodloader.PufferfishModLoader;
import me.dreamhopping.pufferfishmodloader.api.Minecraft;
import me.dreamhopping.pufferfishmodloader.events.EventBus;
import me.dreamhopping.pufferfishmodloader.events.core.GameStartEvent;
import me.dreamhopping.pufferfishmodloader.impl.MinecraftImpl;
import me.dreamhopping.pufferfishmodloader.mods.launch.loader.TransformingClassLoader;
import me.dreamhopping.pufferfishmodloader.mods.launch.loader.RuntimeTransformer;
import net.minecraft.client.main.Main;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.util.Arrays;

public class PMLEntryPoint {
    public static void start(String[] args, boolean server) { // Called by PMLClientMain and PMLServerMain via reflection
        TransformingClassLoader classLoader = (TransformingClassLoader) PMLEntryPoint.class.getClassLoader();
        classLoader.registerTransformer(new RuntimeTransformer() {
            @Override
            public boolean willTransform(String name) {
                return name.equals("net/minecraft/client/main/Main");
            }

            @Override
            public ClassNode transform(ClassNode node) {
                for (MethodNode method : node.methods) {
                    if (method.name.equals("main") && method.desc.equals("([Ljava/lang/String;)V")) {
                        method.instructions.clear();
                        method.tryCatchBlocks.clear();
                        method.localVariables.clear();
                        method.instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                        method.instructions.add(new LdcInsnNode("hello from ASM"));
                        method.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V"));
                        method.instructions.add(new InsnNode(Opcodes.RETURN));
                    }
                }
                return node;
            }
        });
        Minecraft.setInstance(new MinecraftImpl());

        PufferfishModLoader.INSTANCE.logger.info("PML starting...");
        PufferfishModLoader.INSTANCE.gameDir = new File(args[Arrays.asList(args).indexOf("--gameDir") + 1]);

        EventBus.INSTANCE.register(PufferfishModLoader.INSTANCE);
        EventBus.INSTANCE.post(new GameStartEvent());

        Main.main(args);
    }
}
