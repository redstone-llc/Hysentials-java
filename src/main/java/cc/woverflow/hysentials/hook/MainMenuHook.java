package cc.woverflow.hysentials.hook;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

public interface MainMenuHook {
    GuiScreen getScreen();
    void drawPanoramA(int p_73970_1_, int p_73970_2_, float p_73970_3_);

}
