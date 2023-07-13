package cc.woverflow.hysentials.guis.cosmetic;

import cc.polyfrost.oneconfig.libs.universal.UMatrixStack;
import cc.polyfrost.oneconfig.libs.universal.UScreen;
import cc.polyfrost.oneconfig.renderer.NanoVGHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CosmeticGui extends UScreen {
    public ResourceLocation cosmeticBackground = new ResourceLocation("hysentials:gui/cosmeticBackground.png");
    public ResourceLocation firstPage = new ResourceLocation("hysentials:gui/first_page.png");
    public ResourceLocation inventorySot = new ResourceLocation("hysentials:gui/inventory_slot.png");
    public ResourceLocation LastPage = new ResourceLocation("hysentials:gui/last_page.png");

    int xSize = 176;
    int ySize = 125;
    protected int guiLeft;
    protected int guiTop;

    public int getxSize() {
        return xSize;
    }

    public int getySize() {
        return ySize;
    }

    public int getGuiLeft() {
        return guiLeft;
    }

    public int getGuiTop() {
        return guiTop;
    }
}
