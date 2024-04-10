//package llc.redstone.hysentials.mixin.fontrender;
//
//import llc.redstone.hysentials.Hysentials;
//import llc.redstone.hysentials.handlers.redworks.BwRanks;
//import llc.redstone.hysentials.util.DuoVariable;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.FontRenderer;
//import net.minecraft.client.gui.GuiChat;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.ModifyArg;
//import org.spongepowered.asm.mixin.injection.ModifyVariable;
//import org.spongepowered.asm.mixin.injection.Redirect;
//
//import java.util.*;
//
//@Mixin(value = FontRenderer.class, priority = Integer.MIN_VALUE)
//public class TextReplacementRenderer {
//
//    @Unique
//    private List<String> seen = new ArrayList<>();
//
//    @ModifyVariable(method = "renderStringAtPos", at = @At("HEAD"), name = {"text"}, argsOnly = true)
//    private String renderStringAtPos(String text) {
//        if (!seen.contains(text)) {
//            System.out.println("Text: " + text);
//            seen.add(text);
//        }
//        String old = text.replace("§r", "");
//        UUID uuid = null;
//        if (text.startsWith("§aHymojis: \n")) {
//            uuid = UUID.fromString("ad80d7cf-8115-4e2a-b15d-e5cc0bf6a9a2"); // Sin_ender
//        }
//        if (Minecraft.getMinecraft().currentScreen instanceof GuiChat) { // Should only really display when typing in chat
//            uuid = (Minecraft.getMinecraft().thePlayer == null) ? null : Minecraft.getMinecraft().thePlayer.getUniqueID();
//        }
//        try {
//            if (BwRanks.replacementMap.size() > 0) {
//                String finalText = text.replace("§r", "");
//                for (Map.Entry<String, DuoVariable<UUID, String>> entry : BwRanks.replacementMap.entrySet()) {
//                    if (finalText.startsWith(entry.getKey())) {
//                        text = text.replace(entry.getKey(), entry.getValue().getSecond());
//                        uuid = entry.getValue().getFirst();
//                    }
//                }
//            }
//
//            HashMap<String, String> allActiveReplacements = Hysentials.INSTANCE.getConfig().replaceConfig.getAllActiveReplacements();
//            for (String key : allActiveReplacements.keySet()) {
//                String finalText = text.replace("§r", "");
//                String value = allActiveReplacements.get(key);
//                if (value == null) {
//                    continue;
//                }
//                if (value.isEmpty() || key.isEmpty()) {
//                    continue;
//                }
//                value = value.replace("&", "§");
//                key = key.replace("&", "§");
//                if (Hysentials.INSTANCE.getConfig().replaceConfig.isRegexEnabled()) {
//                    text = finalText.replaceAll(key, value);
//                } else {
//                    text = finalText.replace(key, value);
//                }
//            }
//
//        } catch (Exception ignored) {
//        }
//        if (!text.equals(old)) {
//            System.out.println("Replaced: " + old + " with " + text);
//        }
//        return text + (uuid == null ? "" : "‎‎‎" + uuid.toString());
//        // This is going to be used to get the UUID of the player that sent the message
//    }
//
//}
