package llc.redstone.hysentials.handlers.htsl;

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import llc.redstone.hysentials.HysentialsUtilsKt;
import llc.redstone.hysentials.util.MUtils;
import cc.polyfrost.oneconfig.libs.universal.UGraphics;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import llc.redstone.hysentials.event.events.GuiKeyboardEvent;
import llc.redstone.hysentials.event.events.GuiMouseClickEvent;
import llc.redstone.hysentials.guis.ResolutionUtil;
import llc.redstone.hysentials.guis.actionLibrary.ActionLibrary;
import llc.redstone.hysentials.guis.club.ClubDashboard;
import llc.redstone.hysentials.guis.container.GuiItem;
import llc.redstone.hysentials.handlers.redworks.BwRanks;
import llc.redstone.hysentials.htsl.compiler.Compiler;
import llc.redstone.hysentials.util.Input;
import llc.redstone.hysentials.util.Material;
import llc.redstone.hysentials.util.Renderer;
import com.google.gson.JsonObject;
import llc.redstone.hysentials.guis.ResolutionUtil;
import llc.redstone.hysentials.guis.club.ClubDashboard;
import llc.redstone.hysentials.guis.container.GuiItem;
import llc.redstone.hysentials.handlers.redworks.BwRanks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
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
import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.lang.reflect.Field;

import static llc.redstone.hysentials.handlers.guis.GameMenuOpen.field_lowerChestInventory;

public class ActionGUIHandler {
    public static Field guiTopField;
    public static Field guiLeftField;
    public static Field xSizeField;

    Input.Button button;
    Input input;
    boolean showChoose;
    Input.Button clipboard;
    Input.Button file;
    Input.Button club;
    Input.Button library;

    public ActionGUIHandler() {
        try {
            button = new Input.Button(0, 0, 0, 20, "Import HTSL");
            input = new Input(0, 0, 0, 18);
            input.setEnabled(false);
            input.setText("Enter File Name");
            input.setMaxStringLength(24);
            clipboard = new Input.Button(0, 0, 0, 20, "Clipboard");
            file = new Input.Button(0, 0, 0, 20, "File");
            club = new Input.Button(0, 0, 0, 20, "Club");
            library = new Input.Button(0, 0, 0, 20, "Action Library");

            guiTopField = GuiContainer.class.getDeclaredField("field_147009_r");
            guiTopField.setAccessible(true);
            guiLeftField = GuiContainer.class.getDeclaredField("field_147003_i");
            guiLeftField.setAccessible(true);
            xSizeField = GuiContainer.class.getDeclaredField("field_146999_f");
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

            club.setWidth(chestWidth / 2 - 10);
            club.xPosition = rightMost + 10;
            club.yPosition = chestGuiTop + 1 + 75 - 5;

            library.setWidth(chestWidth / 2 - 10);
            library.xPosition = rightMost + 10;
            library.yPosition = chestGuiTop + 1 + 100 - 5;

            clipboard.drawButton(Minecraft.getMinecraft(), event.getMouseX(), event.getMouseY());
            file.drawButton(Minecraft.getMinecraft(), event.getMouseX(), event.getMouseY());
            club.drawButton(Minecraft.getMinecraft(), event.getMouseX(), event.getMouseY());
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

        input.width = chestWidth / 2 + sizeDifference - margin;
        input.xPosition = ResolutionUtil.current().getScaledWidth() / 2 - input.width + sizeDifference - margin;
        input.yPosition = chestGuiTop - input.height - margin;

        button.drawButton(Minecraft.getMinecraft(), event.getMouseX(), event.getMouseY());
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
                if (!file.exists()) {
                    File defaultFile = new File("./config/hysentials/htsl/" + input.getText() + ".txt");
                    if (defaultFile.exists()) {
                        file = defaultFile;
                        codeToBeCompiled = FileUtils.readFileToString(file);
                    } else {
                        try {
                            JsonObject club = ClubDashboard.getClub();
                            String otherCode = NetworkUtils.getString(HysentialsUtilsKt.getHYSENTIALS_API() + "/club/action?clubID=" + (club != null ? club.get("id").getAsString() : null) + "&id=" + input.getText());
                            JSONObject otherJson = new JSONObject(otherCode);
                            if (otherJson.has("action")) {
                                codeToBeCompiled = otherJson.getJSONObject("action").getJSONObject("action").getString("code");
                            } else {
                                String code = NetworkUtils.getString(HysentialsUtilsKt.getHYSENTIALS_API() + "/action?id=" + input.getText());
                                JSONObject json = new JSONObject(code);
                                if (json.has("action")) {
                                    codeToBeCompiled = json.getJSONObject("action").getJSONObject("action").getString("code");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    codeToBeCompiled = FileUtils.readFileToString(file);
                }
                if (codeToBeCompiled != null) {
                    new Compiler(codeToBeCompiled);
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

        if (event.getX() > clipboard.xPosition && event.getX() < clipboard.xPosition + clipboard.width && event.getY() > clipboard.yPosition && event.getY() < clipboard.yPosition + clipboard.height) {
            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            Minecraft.getMinecraft().theWorld.playSound(player.posX, player.posY, player.posZ, "random.click", 1, 1, false);
            Exporter.export = "clipboard";
            showChoose = false;
        }
        if (event.getX() > file.xPosition && event.getX() < file.xPosition + file.width && event.getY() > file.yPosition && event.getY() < file.yPosition + file.height) {
            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            Minecraft.getMinecraft().theWorld.playSound(player.posX, player.posY, player.posZ, "random.click", 1, 1, false);
            Exporter.export = "file";
            showChoose = false;
        }
        if (event.getX() > club.xPosition && event.getX() < club.xPosition + club.width && event.getY() > club.yPosition && event.getY() < club.yPosition + club.height) {
            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            Minecraft.getMinecraft().theWorld.playSound(player.posX, player.posY, player.posZ, "random.click", 1, 1, false);
            if (ClubDashboard.getClub() == null) {
                UChat.chat("&cYou are not in a club!");
                return;
            }
            Exporter.export = "club";
            showChoose = false;
        }
        if (event.getX() > library.xPosition && event.getX() < library.xPosition + library.width && event.getY() > library.yPosition && event.getY() < library.yPosition + library.height) {
            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            Minecraft.getMinecraft().theWorld.playSound(player.posX, player.posY, player.posZ, "random.click", 1, 1, false);
            Exporter.export = "library";
            showChoose = false;
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
                    Exporter.names.add(Navigator.getContainerName().split("Actions: ")[1]);
                    Exporter.type = "function";
                    showChoose = true;
                } else {
                    Exporter.names.add(BwRanks.randomString(5));
                    Exporter.type = getType((GuiChest) screen);
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


}
