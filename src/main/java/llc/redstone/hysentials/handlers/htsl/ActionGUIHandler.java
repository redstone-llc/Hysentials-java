package llc.redstone.hysentials.handlers.htsl;

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import llc.redstone.hysentials.handlers.sbb.SbbRenderer;
import llc.redstone.hysentials.htsl.compiler.CommitSystemKt;
import llc.redstone.hysentials.htsl.compiler.CompileKt;
import llc.redstone.hysentials.event.events.GuiKeyboardEvent;
import llc.redstone.hysentials.event.events.GuiMouseClickEvent;
import llc.redstone.hysentials.guis.ResolutionUtil;
import llc.redstone.hysentials.guis.container.GuiItem;
import llc.redstone.hysentials.handlers.redworks.BwRanks;
import llc.redstone.hysentials.htsl.compiler.ExportAction;
import llc.redstone.hysentials.util.BUtils;
import llc.redstone.hysentials.util.Input;
import llc.redstone.hysentials.util.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.lang.reflect.Field;

import static llc.redstone.hysentials.handlers.guis.GameMenuOpen.field_lowerChestInventory;

public class ActionGUIHandler {
    public static Field guiTopField;
    public static Field guiLeftField;
    public static Field xSizeField;

    Input.Button button;
    Input.Button patch;
    Input input;
    boolean showChoose;
    Input.Button clipboard;
    Input.Button file;
    Input.Button library;

    public ActionGUIHandler() {
        try {
            button = new Input.Button(0, 0, 0, 20, "Import HTSL");
            patch = new Input.Button(0, 0, 0, 20, "Patch");
            input = new Input(0, 0, 0, 18);
            input.setEnabled(false);
            input.setText("Enter File Name");
            input.setMaxStringLength(24);
            clipboard = new Input.Button(0, 0, 0, 20, "Clipboard");
            file = new Input.Button(0, 0, 0, 20, "File");
            library = new Input.Button(0, 0, 0, 20, "Action Library");

            try {
                guiTopField = GuiContainer.class.getDeclaredField("field_147009_r");
            } catch (NoSuchFieldException e) {
                guiTopField = GuiContainer.class.getDeclaredField("guiTop");
            }
            guiTopField.setAccessible(true);
            try {
                guiLeftField = GuiContainer.class.getDeclaredField("field_147003_i");
            } catch (NoSuchFieldException e) {
                guiLeftField = GuiContainer.class.getDeclaredField("guiLeft");
            }
            guiLeftField.setAccessible(true);
            try {
                xSizeField = GuiContainer.class.getDeclaredField("field_146999_f");
            } catch (NoSuchFieldException e) {
                xSizeField = GuiContainer.class.getDeclaredField("xSize");
            }
            xSizeField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onGuiRender(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.openContainer == null)
            return;
        if (!isInActionGui()) return;
        GlStateManager.pushMatrix();

        File housingEditor = new File("./config/ChatTriggers/modules/HousingEditor");
//        File htsl = new File("./config/ChatTriggers/modules/HTSL");
//        if (htsl.exists()) return;
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

        if (showChoose) {
            int rightMost = chestGuiLeft + chestWidth;

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

        int margin = (housingEditor.exists()) ? 5 : 30;
        int sizeDifference = 10;

        Slot slot = Minecraft.getMinecraft().thePlayer.openContainer.getSlot(48);
        Slot slot2 = Minecraft.getMinecraft().thePlayer.openContainer.getSlot(51);
        if (!slot.getHasStack()) {
            ItemStack item = GuiItem.makeColorfulItem(Material.STORAGE_MINECART, "&aUpload to Action Library", 1, 0, "&7Uploads your current function project", "&7to the Action Library.", "", "&eLeft-Click to upload!", "&bRight-click to upload more functions!");
            slot.putStack(item);
        } else if (!slot2.getHasStack() && !slot.getStack().getDisplayName().equals("§aUpload to Action Library")) {
            ItemStack item = GuiItem.makeColorfulItem(Material.STORAGE_MINECART, "&aUpload to Action Library", 1, 0, "&7Uploads your current function project", "&7to the Action Library.", "", "&eLeft-Click to upload!", "&bRight-click to upload more functions!");
            slot2.putStack(item);
        }
        button.setWidth(chestWidth / 2 - sizeDifference);
        button.xPosition = ResolutionUtil.current().getScaledWidth() / 2 + sizeDifference;
        button.yPosition = chestGuiTop - button.height - margin + 1;

        patch.setWidth(chestWidth / 2 - sizeDifference);
        patch.xPosition = ResolutionUtil.current().getScaledWidth() / 2 + sizeDifference + button.width + sizeDifference;
        patch.yPosition = chestGuiTop - patch.height - margin + 1;

        input.width = chestWidth / 2 + sizeDifference - margin;
        input.xPosition = ResolutionUtil.current().getScaledWidth() / 2 - input.width + sizeDifference - margin;
        input.yPosition = chestGuiTop - input.height - margin;

        button.drawButton(Minecraft.getMinecraft(), event.getMouseX(), event.getMouseY());
        if (getFunctionName() != null) {
            patch.drawButton(Minecraft.getMinecraft(), event.getMouseX(), event.getMouseY());
        }
        input.drawTextBox();
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void guiKey(GuiKeyboardEvent event) {
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.openContainer == null)
            return;
        if (!isInActionGui()) return;
        File htsl = new File("./config/ChatTriggers/modules/HTSL");
        if (htsl.exists()) return;
        input.setFocused(true);
        if (input.isFocused()) {
            input.textboxKeyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());

            if (Keyboard.getEventKey() != 1) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void mouseClick(GuiMouseClickEvent event) {
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.openContainer == null)
            return;
        if (!isInActionGui()) return;
        File htsl = new File("./config/ChatTriggers/modules/HTSL");
        if (htsl.exists()) return;

        input.mouseClicked(event.getX(), event.getY(), event.getButton());
        if (event.getX() > input.xPosition && event.getX() < input.xPosition + input.getWidth() && event.getY() > input.yPosition && event.getY() < input.yPosition + input.height) {
            if (input.getText().equals("Enter File Name")) {
                input.setText("");
                input.setCursorPosition(0);
            }
            input.setEnabled(true);
        } else {
            input.setEnabled(false);
        }

        if (getFunctionName() != null) {
            if (event.getX() > patch.xPosition && event.getX() < patch.xPosition + patch.width && event.getY() > patch.yPosition && event.getY() < patch.yPosition + patch.height) {
                EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
                Minecraft.getMinecraft().theWorld.playSound(player.posX, player.posY, player.posZ, "random.click", 1, 1, false);
                patch.displayString = "Patching...";
                patch.enabled = true;

                if (input.getText().equals("Enter File Name") || input.getText().equals("")) {
                    input.setText(getFunctionName());
                }

                try {
                    String houseFile = SbbRenderer.housingScoreboard.getHousingName() + "-" + SbbRenderer.housingScoreboard.getHousingCreator();
                    File file = new File("./config/hysentials/htsl/house/" + houseFile + "/" + input.getText() + ".htsl"); // Recently imported one
                    File file2 = new File("./config/hysentials/htsl/" + input.getText() + ".htsl"); // New file
                    if (!file.exists()) {
                        UChat.chat("&3[HTSL] &cFile not found in house folder, please export the file first.");
                        return;
                    }
                    if (!file2.exists()) {
                        UChat.chat("&3[HTSL] &cFile not found in htsl folder, please create the file you want to patch first.");
                        return;
                    }

                    String response = CommitSystemKt.patch(file, file2).toString();
                    if (response.equals("success")) {
                        UChat.chat("&3[HTSL] &aSuccessfully patched " + input.getText() + ".htsl");
                    } else {
                        UChat.chat("&3[HTSL] &cFailed to patch " + input.getText() + ".htsl");
                        UChat.chat("&c- " + response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                patch.displayString = "Patch";
                patch.enabled = true;
            }
        }

        if (event.getX() > button.xPosition && event.getX() < button.xPosition + button.width && event.getY() > button.yPosition && event.getY() < button.yPosition + button.height) {
            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            Minecraft.getMinecraft().theWorld.playSound(player.posX, player.posY, player.posZ, "random.click", 1, 1, false);
            button.displayString = "Getting Data...";
            button.enabled = true;

            if (input.getText().equals("Enter File Name") || input.getText().equals("")) {
                input.setText("default");
            }
            try {
                String codeToBeCompiled = null;
                File file = new File("./config/hysentials/htsl/" + input.getText() + ".htsl");
                if (file.exists()) {
                    codeToBeCompiled = FileUtils.readFileToString(file);
                }

                if (codeToBeCompiled != null) {
                    CompileKt.compileFile(codeToBeCompiled, true);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            input.setSelectionPos(0);
            input.setCursorPosition(0);
            input.setFocused(false);
            input.setText("Enter File Name");

            button.displayString = "Import HTSL";
            button.enabled = true;
        }

        //over clipboard
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
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
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

    public static ItemStack lastItem = null;
    public static String lastContainer = null;

    @SubscribeEvent
    public void onGuiSlotClick(GuiMouseClickEvent event) {
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.openContainer == null)
            return;
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (screen == null) return;
        if (screen instanceof GuiChest) {
            Slot slot = ((GuiChest) screen).getSlotUnderMouse();
            if (slot == null) return;

            if (slot.getHasStack() && slot.getStack().hasDisplayName() && !isInActionGui()) {
                lastItem = slot.getStack();
                if (Navigator.getContainerName() != null) {
                    lastContainer = Navigator.getContainerName();
                }
            }

            if (!isInActionGui()) return;
            if (slot.getHasStack() && slot.getStack().hasDisplayName()
                && slot.getStack().getDisplayName().equals("§aUpload to Action Library")) {
                event.getCi().cancel();

                if (Navigator.getContainerName() != null && Navigator.getContainerName().startsWith("Actions: ")) {
                    exportName = Navigator.getContainerName().split("Actions: ")[1];
                    showChoose = true;
                } else {
                    exportName = BwRanks.randomString(5);
                    showChoose = true;
                }
            }
        }
    }

    public static String getType(GuiChest chest, boolean e) {
        if (Navigator.getContainerName() != null) {
            if (Navigator.getContainerName().equals("Edit NPC")) {
                return "npc";
            }
            Slot slot = chest.inventorySlots.getSlot(47);
            if (slot == null) return "function";
            if (slot.getHasStack() && slot.getStack().hasDisplayName()) {
                int id = Item.getIdFromItem(slot.getStack().getItem());
                if (id == 77 || id == 143) {
                    return "button";
                }
                if (id == 148 || id == 147 || id == 70 || id == 72) {
                    return "pad";
                }
            }
        }
        return "function";
    }

    public static String getType(GuiChest chest) {
        if (lastContainer != null && lastContainer.equals("Edit NPC")) {
            return "npc";
        }
        if (lastContainer != null && lastContainer.equals("Event Actions")) {
            return ChatColor.Companion.stripControlCodes(lastItem.getDisplayName()).replace(" ", "_").toLowerCase();
        }
        Slot slot = chest.inventorySlots.getSlot(47);
        if (slot == null) return "function";
        if (slot.getHasStack() && slot.getStack().hasDisplayName()) {
            int id = Item.getIdFromItem(slot.getStack().getItem());
            if (id == 77 || id == 143) {
                return "button";
            }
            if (id == 148 || id == 147 || id == 70 || id == 72) {
                return "pad";
            }
        }
        return "function";
    }

    public static boolean isInActionGui() {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChest) {
            GuiChest chest = (GuiChest) Minecraft.getMinecraft().currentScreen;
            try {
                if (field_lowerChestInventory.get(chest) instanceof IInventory) {
                    IInventory inventory = (IInventory) field_lowerChestInventory.get(chest);
                    if ((inventory.getName().equals("Edit Actions") || inventory.getName().startsWith("Actions: ")) && inventory.getSizeInventory() == 54) {
                        return true;
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public static String getFunctionName() {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChest) {
            GuiChest chest = (GuiChest) Minecraft.getMinecraft().currentScreen;
            try {
                if (field_lowerChestInventory.get(chest) instanceof IInventory) {
                    IInventory inventory = (IInventory) field_lowerChestInventory.get(chest);
                    if (inventory.getName().startsWith("Actions: ")) {
                        return inventory.getName().split("Actions: ")[1];
                    }
                }
            } catch (IllegalAccessException e) {
                return null;
            }
        }
        return null;
    }

    public static boolean isActionSettings() {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChest) {
            GuiChest chest = (GuiChest) Minecraft.getMinecraft().currentScreen;
            try {
                if (field_lowerChestInventory.get(chest) instanceof IInventory) {
                    IInventory inventory = (IInventory) field_lowerChestInventory.get(chest);
                    if (inventory.getName().equals("Action Settings") && inventory.getSizeInventory() == 36) {
                        return true;
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public static boolean isEditConditions() {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChest) {
            GuiChest chest = (GuiChest) Minecraft.getMinecraft().currentScreen;
            try {
                if (field_lowerChestInventory.get(chest) instanceof IInventory) {
                    IInventory inventory = (IInventory) field_lowerChestInventory.get(chest);
                    if (inventory.getName().equals("Edit Conditions") && inventory.getSizeInventory() == 54) {
                        return true;
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public static boolean isSettingsConditions() {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChest) {
            GuiChest chest = (GuiChest) Minecraft.getMinecraft().currentScreen;
            try {
                if (field_lowerChestInventory.get(chest) instanceof IInventory) {
                    IInventory inventory = (IInventory) field_lowerChestInventory.get(chest);
                    if (inventory.getName().equals("Settings") && inventory.getSizeInventory() == 36) {
                        return true;
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }


}
