package me.dreamhopping.pml.transformers;

import me.dreamhopping.pml.mods.launch.loader.RuntimeTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class ClientBrandRetrieverTransformer implements RuntimeTransformer {
    public boolean willTransform(String name) {
        return name.equals("net/minecraft/client/ClientBrandRetriever");
    }

    public ClassNode transform(ClassNode classNode) {
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals("getClientModName")) {
                InsnList list = new InsnList();
                list.add(new LdcInsnNode("pufferfishmodloader"));
                list.add(new InsnNode(Opcodes.ARETURN));

                methodNode.instructions.clear();
                methodNode.instructions.add(list);
                break;
            }
        }

        return classNode;
    }
}
