package llc.redstone.hysentials.handlers.htsl;

import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import llc.redstone.hysentials.handlers.redworks.BwRanks;
import llc.redstone.hysentials.htsl.compiler.ExportAction;
import llc.redstone.hysentials.event.events.GuiMouseClickEvent;
import llc.redstone.hysentials.guis.container.GuiItem;
import llc.redstone.hysentials.util.C;
import llc.redstone.hysentials.util.Input;
import llc.redstone.hysentials.util.Material;
import llc.redstone.hysentials.util.Renderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static llc.redstone.hysentials.guis.sbBoxes.SBBoxesEditor.drawRect;

public class FunctionsGUIHandler {
    Field guiTopField;
    Field guiLeftField;
    Field xSizeField;
    HashMap<Integer, Slot> selectedSlots = new HashMap<>();
    boolean showChoose = false;
    Input.Button clipboard;
    Input.Button file;
    Input.Button library;
    boolean isSelecting = false;

    public FunctionsGUIHandler() {
        try {
            clipboard = new Input.Button(0, 0, 0, 20, "Clipboard");
            file = new Input.Button(0, 0, 0, 20, "File");
            library = new Input.Button(0, 0, 0, 20, "Action Library");

            guiTopField = ReflectionHelper.findField(GuiContainer.class, "guiTop", "field_147009_r");
            guiTopField.setAccessible(true);
            guiLeftField = ReflectionHelper.findField(GuiContainer.class, "guiLeft", "field_147003_i");
            guiLeftField.setAccessible(true);
            xSizeField = ReflectionHelper.findField(GuiContainer.class, "xSize", "field_146999_f");
            xSizeField.setAccessible(true);
        } catch (ReflectionHelper.UnableToAccessFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isInFunctionsGui() {
        return Minecraft.getMinecraft().currentScreen instanceof GuiChest && Navigator.getContainerName() != null && Navigator.getContainerName().equals("Functions");
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onGuiRender(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (UMinecraft.getPlayer() == null || UMinecraft.getPlayer().openContainer == null)
            return;
        if (!isInFunctionsGui()) return;
        if (Navigator.getContainerSize() != 54) return;
        GlStateManager.pushMatrix();
        Slot slot = UMinecraft.getPlayer().openContainer.getSlot(48);

        if (!slot.getHasStack()) {
            ItemStack item = GuiItem.makeColorfulItem(Material.STORAGE_MINECART, "&aUpload to Action Library", 1, 0, "&7Uploads a selection of", "&7functions to your desired", "&7destination.", "", "&7Once you are done", "&7choosing your functions,", "&7click this again to", "&7start the export", "", "&eClick to toggle selecting!");
            GuiItem.hideFlag(item, 1);
            slot.putStack(item);
        }

        int chestGuiTop;
        int chestWidth;
        int chestGuiLeft;
        try {
            chestGuiTop = (int) guiTopField.get(Minecraft.getMinecraft().currentScreen);
            chestGuiLeft = (int) guiLeftField.get(Minecraft.getMinecraft().currentScreen);
            chestWidth = (int) xSizeField.get(Minecraft.getMinecraft().currentScreen);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        int rightMost = chestGuiLeft + chestWidth;


        if (isSelecting) {
            for (Map.Entry<Integer, Slot> entry : selectedSlots.entrySet()) {
                int slotIndex = entry.getKey();
                Slot s = entry.getValue();
                if (!s.getHasStack() || !s.getStack().hasDisplayName()) continue;
                String name = C.removeColor(s.getStack().getDisplayName());
                int x = rightMost + 10;
                int y = chestGuiTop + 1 + 25 * slotIndex - 5;
                drawRect(x, y, 50, 20, (int) Renderer.color(0, 0, 0, 150));
            }
        }

        if (showChoose) {

            clipboard.setWidth(chestWidth / 2 - 10);
            clipboard.xPosition = rightMost + 10;
            clipboard.yPosition = chestGuiTop + 1 + 25 - 5;

            file.setWidth(chestWidth / 2 - 10);
            file.xPosition = rightMost + 10;
            file.yPosition = chestGuiTop + 1 + 50 - 5;

            library.setWidth(chestWidth / 2 - 10);
            library.xPosition = rightMost + 10;
            library.yPosition = chestGuiTop + 1 + 100 - 5;

            clipboard.drawButton(Minecraft.getMinecraft(), event.getMouseX(), event.getMouseY());
            file.drawButton(Minecraft.getMinecraft(), event.getMouseX(), event.getMouseY());
            library.drawButton(Minecraft.getMinecraft(), event.getMouseX(), event.getMouseY());
        }
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void mouseClick(GuiMouseClickEvent event) {
        if (UMinecraft.getPlayer() == null || UMinecraft.getPlayer().openContainer == null)
            return;
        if (Navigator.getContainerName() == null || !Navigator.getContainerName().equals("Functions")) return;
        if (!showChoose) return;

        if (isClickOnButton(clipboard, event)) {
            performClickAction("clipboard");
        }
        if (isClickOnButton(file, event)) {
            performClickAction("file");
        }
        if (isClickOnButton(library, event)) {
            performClickAction("library");
        }
    }

    private boolean isClickOnButton(GuiButton button, GuiMouseClickEvent event) {
        return event.getX() > button.xPosition && event.getX() < button.xPosition + button.width &&
            event.getY() > button.yPosition && event.getY() < button.yPosition + button.height;
    }

    String exportName = null;

    private void performClickAction(String exportMethod) {
        EntityPlayerSP player = UMinecraft.getPlayer();
        Minecraft.getMinecraft().theWorld.playSound(player.posX, player.posY, player.posZ, "random.click", 1, 1, false);
        ExportAction.setExportMethod(exportMethod);
        showChoose = false;
        if (exportName != null) {
            ExportAction.Companion.exportAction(exportName);
            exportName = null;
        } else {
            ExportAction.Companion.exportAction(BwRanks.randomString(5));
        }
    }

    @SubscribeEvent
    public void onGuiSlotClick(GuiMouseClickEvent event) {
        if (UMinecraft.getPlayer() == null || Minecraft.getMinecraft().thePlayer.openContainer == null)
            return;
        if (Navigator.getContainerName() == null || !Navigator.getContainerName().equals("Functions")) return;
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (screen == null) return;
        if (screen instanceof GuiChest) {
            Slot slot = ((GuiChest) screen).getSlotUnderMouse();
            if (slot == null) return;
            if (slot.getStack() == null || !slot.getStack().hasDisplayName()) return;
            if (slot.getStack().getDisplayName().equals("§aUpload to Action Library")) {
                event.getCi().cancel();
                isSelecting = !isSelecting;
                selectedSlots.clear();
                return;
            }

            if (isSelecting) {
                event.getCi().cancel();
                if (slot.getHasStack() && slot.getStack().hasDisplayName()) {
                    if (selectedSlots.containsKey(slot.getSlotIndex())) {
                        selectedSlots.remove(slot.getSlotIndex());
                    } else {
                        selectedSlots.put(slot.getSlotIndex(), slot);
                    }
                }
            }
        }
    }
}
