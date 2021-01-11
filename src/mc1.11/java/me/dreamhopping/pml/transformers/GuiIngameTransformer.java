package me.dreamhopping.pml.transformers;

import me.dreamhopping.pml.mods.launch.loader.RuntimeTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class GuiIngameTransformer implements RuntimeTransformer {
    public boolean willTransform(String name) {
        return name.equals("net/minecraft/client/gui/GuiIngame");
    }

    public ClassNode transform(ClassNode classNode) {
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals("renderGameOverlay")) {
                for (AbstractInsnNode node = methodNode.instructions.getLast(); node != null; node = node.getPrevious()) {
                    if (node.getOpcode() == Opcodes.RETURN) {
                        MethodInsnNode renderGameInsn = new MethodInsnNode(Opcodes.INVOKESTATIC, getImplementationClass("GuiIngame"), "renderGameOverlay", "()V");
                        methodNode.instructions.insertBefore(node, renderGameInsn);

                        break;
                    }
                }
            }
        }

        return classNode;
    }
}
