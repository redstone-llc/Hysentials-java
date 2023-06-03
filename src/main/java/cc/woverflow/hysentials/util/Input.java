package cc.woverflow.hysentials.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

import java.lang.reflect.Field;

public class Input extends GuiTextField {

    public Input(int x, int y, int width, int height) {
        super(0, Minecraft.getMinecraft().fontRendererObj, x, y, width, height);
    }

    public static class Button extends GuiButton {
        public Button(int x, int y, int width, int height, String text) {
            super(0, x, y, width, height, text);
        }

        public void setText() {

        }
    }
}
