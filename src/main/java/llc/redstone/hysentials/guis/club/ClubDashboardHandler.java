package llc.redstone.hysentials.guis.club;

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.utils.Multithreading;
import com.google.gson.JsonParser;
import llc.redstone.hysentials.event.events.GuiMouseClickEvent;
import llc.redstone.hysentials.handlers.htsl.Navigator;
import com.google.gson.JsonObject;
import llc.redstone.hysentials.util.BUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static llc.redstone.hysentials.guis.club.ClubDashboard.clubhouseSelect;
import static llc.redstone.hysentials.guis.club.ClubDashboard.instance;
import static llc.redstone.hysentials.guis.container.GuiItem.getLore;
import static llc.redstone.hysentials.guis.container.GuiItem.setLore;

public class ClubDashboardHandler {
    @SubscribeEvent
    public void onMouseClick(GuiMouseClickEvent event) {
        if (!ClubDashboard.clubhouseSelect) return;
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.openContainer == null) return;
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChest) {
            if (Navigator.getContainerName() == null || !Navigator.getContainerName().endsWith("Houses")) return;
            GuiChest chest = (GuiChest) Minecraft.getMinecraft().currentScreen;
            if (chest.getSlotUnderMouse().getHasStack()) {
                event.getCi().cancel();
                String name = chest.getSlotUnderMouse().getStack().getDisplayName();
                List<String> lore = new ArrayList<>(getLore(chest.getSlotUnderMouse().getStack()));
                List<String> newLore = new ArrayList<>();
                for (String line : lore) {
                    line = line.replace("§r", "");
                    if (line.startsWith("§7Cookies: ") || line.startsWith("§7Players: ")
                        || line.equals("§eRight Click to Manage!") || line.startsWith("§7Created: ")) {
                        continue;
                    }
                    newLore.add(line);
                }
                ItemStack itemStack = chest.getSlotUnderMouse().getStack();
                setLore(itemStack, newLore);
                String username = Minecraft.getMinecraft().thePlayer.getName();
                if (Navigator.getContainerName().split("'").length == 2) {
                    username = Navigator.getContainerName().split("'")[0];
                }

                JSONObject nbt = BUtils.nbtCompoundToJson(!itemStack.hasTagCompound() ? new NBTTagCompound() : itemStack.serializeNBT());
                JSONObject jsonObject = new JSONObject();
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("name", ChatColor.Companion.stripControlCodes(name));
                jsonObject1.put("username", username);
                jsonObject1.put("nbt", nbt);

                jsonObject.put("houses", jsonObject1);
                Multithreading.runAsync(() -> {
                    ClubDashboard.update(jsonObject);
                    ClubDashboard.clubhouseSelect = false;
                    JsonObject newClubData = ClubDashboard.getClub();
                    if (newClubData == null) return;
                    new ClubDashboard(newClubData).open();
                });
            }
        }
    }
}
