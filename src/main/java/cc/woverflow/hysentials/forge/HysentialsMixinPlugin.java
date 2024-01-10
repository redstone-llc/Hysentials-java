package cc.woverflow.hysentials.forge;


import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.lib.tree.*;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;

public class HysentialsMixinPlugin implements IMixinConfigPlugin {

    private boolean isOptiFine = false;
    private boolean hasApplied = false;

    @Override
    public void onLoad(String mixinPackage) {
//        try {
//            Class.forName("net.optifine.render.RenderEnv");
//            System.out.println("OptiFine detected, applying OptiFine compat mixin.");
//            isOptiFine = true;
//        } catch (ClassNotFoundException e) {
//            System.out.println("OptiFine not detected, not applying OptiFine compat mixin.");
//            isOptiFine = false;
//        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
//        if (mixinClassName.endsWith("_OptiFine")) {
//            return isOptiFine;
//        } else if (mixinClassName.endsWith("_NoOptiFine")) {
//            return !isOptiFine;
//        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, org.spongepowered.asm.lib.tree.ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, org.spongepowered.asm.lib.tree.ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
//        if (!hasApplied && targetClass != null && Objects.equals(targetClassName, "net.minecraft.client.gui.GuiPlayerTabOverlay")) {
//            for (MethodNode method : targetClass.methods) {
//                final String methodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(targetClass.name, method.name, method.desc);
//                final ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();
//            }
//        }
    }

    private String mapMethodNameFromNode(AbstractInsnNode node) {
        MethodInsnNode methodInsnNode = (MethodInsnNode) node;
        return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(methodInsnNode.owner, methodInsnNode.name, methodInsnNode.desc);
    }
}
