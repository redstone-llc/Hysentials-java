package llc.redstone.hysentials.handlers.chat.modules.misc;

import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import llc.redstone.hysentials.config.hysentialmods.ChatConfig;
import llc.redstone.hysentials.util.BUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;

public class Limit256 {
    public static Field guichatDefaultInputFieldText;
    public static Field c01MessageField;

    public Limit256() {
        guichatDefaultInputFieldText = ReflectionHelper.findField(GuiChat.class, "defaultInputFieldText", "field_146409_v");
        guichatDefaultInputFieldText.setAccessible(true);

        c01MessageField = ReflectionHelper.findField(C01PacketChatMessage.class, "message", "field_149440_a");
        c01MessageField.setAccessible(true);
    }

    @SubscribeEvent
    public void onRenderGameOverlay(GuiOpenEvent e) {
        try {
            if (!UMinecraft.getPlayer().worldObj.isRemote)
                return;
        } catch (NullPointerException exc) {
            return;
        }

        if (e.gui instanceof GuiChat && BUtils.isHypixelOrSBX()) {
            if (!ChatConfig.chatLimit256) return;
            e.setCanceled(true);
            GuiScreen old = Minecraft.getMinecraft().currentScreen;
            GuiScreen guiScreenIn = e.gui;
            if (old != null && guiScreenIn != old) {
                old.onGuiClosed();
            }

            String defaultText = "";

            try {
                defaultText = (String) guichatDefaultInputFieldText.get(guiScreenIn);
            } catch (IllegalAccessException err) {
                err.printStackTrace();
            }

            GuiChat256 newGui = new GuiChat256(defaultText.replace("§", "&"));

            Minecraft.getMinecraft().currentScreen = (GuiScreen) newGui;

            Minecraft.getMinecraft().setIngameNotInFocus();
            ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            ((GuiScreen) newGui).setWorldAndResolution(Minecraft.getMinecraft(), i, j);
            Minecraft.getMinecraft().skipRenderWorld = false;
        }
    }

}
