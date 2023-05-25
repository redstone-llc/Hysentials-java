package cc.woverflow.hysentials.guis.gameMenu;

import cc.woverflow.hysentials.guis.HysentialsGui;
import cc.woverflow.hysentials.util.HysentialsFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RevampedGameMenu extends HysentialsGui {
    static HysentialsFontRenderer title;
    static HysentialsFontRenderer subtitle;
    private int scrollPos = 0;
    List<ItemStack> items = new ArrayList<>();

    public RevampedGameMenu(List<ItemStack> items) {
        this.items = items;
    }

    public static void initGUI() {
        title = new HysentialsFontRenderer("Minecraft Ten", 40);
        subtitle = new HysentialsFontRenderer("Minecraft Ten", 20);
    }

    @Override
    protected void pack() {

    }

    int tickCounter = 0;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int yg = (height / 10);  // Y grid
        int xg = (width / 11);

        int bottom = yg * 9;

        drawRect(xg + 20, yg + 20, xg * 10 - 60, yg * 2 + 20, new Color(0, 0, 0, 200).getRGB());
        drawRect(xg + 20, yg * 2 + 20, xg * 10 - 60, yg * 9, new Color(0, 0, 0, 200).getRGB());

        //draw bar at top
        drawRect(xg + 20, yg + 20, xg * 10 - 60, yg + 25, new Color(255, 213, 0, 208).getRGB());

        //draw text above the bar
        title.drawStringWithShadow("Hypixel Games", xg + 25, yg, new Color(255, 255, 255, 255).getRGB());

        int scrollableHeight = yg * 7; // The total height of the scrollable area
        int numRows = (int) Math.ceil((double) items.size() / 6); // The number of rows needed to display all items
        int contentHeight = numRows * (bottom / 3); // The total height of the content
        int barHeight = (int) ((double) scrollableHeight / contentHeight * scrollableHeight); // The height of the scroll bar
        int barY = yg * 2 + (scrollPos * (scrollableHeight - barHeight)) / (contentHeight - scrollableHeight); // The y-coordinate of the top of the scroll bar

        drawRect(xg * 10 - 70, yg * 2, xg * 10 - 65, yg * 9 - 10, new Color(98, 94, 94, 200).getRGB()); // The background of the scroll bar
        drawRect(xg * 10 - 70, barY, xg * 10 - 65, barY + barHeight, new Color(255, 255, 255, 200).getRGB()); // The scroll bar itself

        super.drawScreen(mouseX, mouseY, partialTicks);

        int col = 0;
        int row = 0;

        for (ItemStack item : items) {
            if (item == null)
                continue;


            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }
    }

    //drawSquareWithCornersCutOut((int) (xg * 1.5f), (int) (yg * 1.5f + 20), (int) (xg * 9.5f - 60), (int) (yg * 2.8f), 3, new Color(0, 0, 0, 200).getRGB());
    //        GL11.glPushMatrix();
    //        GL11.glScalef(1.5f, 1.5f, 1.5f);
    //        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§bWhere would you like to go? §7Choose from our selection of games or pinned", (int) (xg + 10), (int) (yg + 20), new Color(255, 255, 255, 255).getRGB());
    //        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("§7housing mini games.", (int) (xg + 10), (int) (yg + 20 + 10), new Color(255, 255, 255, 255).getRGB());
    //        GL11.glPopMatrix();

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int yg = (height / 10);  // Y grid
        int scrollableHeight = yg * 7; // The total height of the scrollable area
        int numRows = (int) Math.ceil((double) items.size() / 6); // The number of rows needed to display all items
        int bottom = yg * 9;
        int contentHeight = numRows * (bottom / 3); // The total height of the content
        int barHeight = (int) ((double) scrollableHeight / contentHeight * scrollableHeight); // The height of the scroll bar

        int scroll = Mouse.getEventDWheel();
        if (scroll != 0) {
            //check if the scroll position is outside of the bar height
            int newPos = scrollPos - (scroll / 120) * 10;
            // Adjust the scroll position by 10 pixels per scroll step
            if (newPos < 0)
                scrollPos = 0;
            else scrollPos = Math.min(newPos, contentHeight - scrollableHeight - 40);
        }
    }

    public void drawSquareWithCornersCutOut(int startX, int startY, int endX, int endY, int cutSize, int color) {
        // Draw the top and bottom sides
        drawRect(startX + cutSize, startY, endX - cutSize, startY + cutSize, color);
        drawRect(startX + cutSize, endY - cutSize, endX - cutSize, endY, color);

        // Draw the left and right sides
        drawRect(startX, startY + cutSize, startX + cutSize, endY - cutSize, color);
        drawRect(endX - cutSize, startY + cutSize, endX, endY - cutSize, color);

        // Fill in the center of the square
        drawRect(startX + cutSize, startY + cutSize, endX - cutSize, endY - cutSize, color);
    }

    private void renderItem(ItemStack itemStack, int x, int y, int size) {
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0.0F);
        GL11.glScalef((float) size / 16.0F, (float) size / 16.0F, 1.0F);
        FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.renderItemIntoGUI(itemStack, 0, 0);
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }


    @Override
    public void onGuiClosed() {
        super.onGuiClosed();

    }
}
