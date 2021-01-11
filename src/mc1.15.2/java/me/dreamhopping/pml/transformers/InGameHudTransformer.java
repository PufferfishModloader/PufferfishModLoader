package me.dreamhopping.pml.transformers;

import me.dreamhopping.pml.mods.launch.loader.RuntimeTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class InGameHudTransformer implements RuntimeTransformer {
    public boolean willTransform(String name) {
        return name.equals("net/minecraft/client/gui/hud/InGameHud");
    }

    public ClassNode transform(ClassNode classNode) {
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals("addChatMessage")) {
                InsnList list = new InsnList();
                list.add(new VarInsnNode(Opcodes.ALOAD, 2));
                list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, getImplementationClass("InGameHud"), "addChatMessage", "(Lnet/minecraft/text/Text;)V"));

                methodNode.instructions.insert(list);
                break;
            } else if (methodNode.name.equals("render")) {
                for (AbstractInsnNode node = methodNode.instructions.getLast(); node != null; node = node.getPrevious()) {
                    if (node.getOpcode() == Opcodes.RETURN) {
                        MethodInsnNode renderGameInsn = new MethodInsnNode(Opcodes.INVOKESTATIC, getImplementationClass("InGameHud"), "render", "()V");
                        methodNode.instructions.insertBefore(node, renderGameInsn);

                        break;
                    }
                }
            }
        }

        return classNode;
    }
}
