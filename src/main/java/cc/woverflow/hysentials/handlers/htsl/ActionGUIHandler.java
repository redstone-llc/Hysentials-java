package cc.woverflow.hysentials.handlers.htsl;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.UGraphics;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.woverflow.hysentials.event.events.GuiKeyboardEvent;
import cc.woverflow.hysentials.event.events.GuiMouseClickEvent;
import cc.woverflow.hysentials.guis.ResolutionUtil;
import cc.woverflow.hysentials.guis.actionLibrary.ActionLibrary;
import cc.woverflow.hysentials.guis.actionLibrary.ChooseOutput;
import cc.woverflow.hysentials.guis.club.ClubDashboard;
import cc.woverflow.hysentials.guis.container.GuiItem;
import cc.woverflow.hysentials.htsl.compiler.Compiler;
import cc.woverflow.hysentials.util.Input;
import cc.woverflow.hysentials.util.Material;
import cc.woverflow.hysentials.util.Renderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
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

import static cc.woverflow.hysentials.handlers.guis.GameMenuOpen.field_lowerChestInventory;

public class ActionGUIHandler {
    Field guiTopField;
    Field xSizeField;

    Input.Button button;
    Input input;

    public ActionGUIHandler() {
        try {
            button = new Input.Button(0, 0, 0, 20, "Import HTSL");
            input = new Input(0, 0, 0, 18);
            input.setEnabled(false);
            input.setText("Enter File Name");
            input.setMaxStringLength(24);
            guiTopField = GuiContainer.class.getDeclaredField("field_147009_r");
            guiTopField.setAccessible(true);
            xSizeField = GuiContainer.class.getDeclaredField("field_146999_f");
            xSizeField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onGuiRender(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.openContainer == null) return;
        if (!isInActionGui()) return;

        File housingEditor = new File("./config/ChatTriggers/modules/HousingEditor");
//        File htsl = new File("./config/ChatTriggers/modules/HTSL");
//        if (htsl.exists()) return;
        int chestGuiTop;
        int chestWidth;
        try {
            chestGuiTop = (int) guiTopField.get(Minecraft.getMinecraft().currentScreen);
            chestWidth = (int) xSizeField.get(Minecraft.getMinecraft().currentScreen);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        int margin = (housingEditor.exists()) ? 5 : 30;
        int sizeDifference = 10;

        GL11.glPopMatrix();
        Slot slot = Minecraft.getMinecraft().thePlayer.openContainer.getSlot(48);
        if (!slot.getHasStack()) {
            ItemStack item = GuiItem.makeColorfulItem(Material.STORAGE_MINECART, "&aUpload to Action Library", 1, 0, "&7Uploads your current function project", "&7to the Action Library.", "", "&eLeft-Click to upload!", "&bRight-click to upload more functions!");
            slot.putStack(item);
        }
        button.setWidth(chestWidth / 2 - sizeDifference);
        button.xPosition = ResolutionUtil.current().getScaledWidth() / 2 + sizeDifference;
        button.yPosition = chestGuiTop - button.height - margin + 1;

        input.width = chestWidth / 2 + sizeDifference - margin;
        input.xPosition = ResolutionUtil.current().getScaledWidth() / 2 - input.width + sizeDifference - margin;
        input.yPosition = chestGuiTop - input.height - margin;

        button.drawButton(Minecraft.getMinecraft(), event.mouseX, event.mouseY);
        input.drawTextBox();
        GL11.glPushMatrix();
    }

    @SubscribeEvent
    public void guiKey(GuiKeyboardEvent event) {
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.openContainer == null) return;
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
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.openContainer == null) return;
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
                            JSONObject club = ClubDashboard.getClub();
                            String otherCode = NetworkUtils.getString("https://hysentials.redstone.llc/api/club/action?clubID=" + (club != null ? club.getString("id") : null) + "&id=" + input.getText());
                            JSONObject otherJson = new JSONObject(otherCode);
                            if (otherJson.has("action")) {
                                codeToBeCompiled = otherJson.getJSONObject("action").getJSONObject("action").getString("code");
                            } else {
                                String code = NetworkUtils.getString("https://hysentials.redstone.llc/api/action?id=" + input.getText());
                                JSONObject json = new JSONObject(code);
                                codeToBeCompiled = json.getJSONObject("action").getJSONObject("action").getString("code");
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

    }

    @SubscribeEvent
    public void onGuiSlotClick(GuiMouseClickEvent event) {
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.openContainer == null) return;
        if (!isInActionGui()) return;
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (screen == null) return;
        if (screen instanceof GuiChest) {
            Slot slot = ((GuiChest) screen).getSlotUnderMouse();
            if (slot == null) return;
            if (slot.getSlotIndex() == 48) {
                event.getCi().cancel();

                if (Navigator.getContainerName() != null && Navigator.getContainerName().startsWith("Actions: ")) {
                    if (ClubDashboard.getClub() != null) {
                        Exporter.name = Navigator.getContainerName().split("Actions: ")[1];
                        Minecraft.getMinecraft().thePlayer.closeScreen();
                        new ChooseOutput(Minecraft.getMinecraft().currentScreen).open(Minecraft.getMinecraft().thePlayer);
                    }
                } else {
                    UChat.chat("&cThis has not been implemented yet!");
                }
            }
        }
    }

    public static boolean isInActionGui() {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChest) {
            GuiChest chest = (GuiChest) Minecraft.getMinecraft().currentScreen;
            try {
                if (field_lowerChestInventory.get(chest) instanceof IInventory) {
                    IInventory inventory = (IInventory) field_lowerChestInventory.get(chest);
                    if (inventory.getName().equals("Edit Actions") || inventory.getName().startsWith("Actions: ")) {
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
