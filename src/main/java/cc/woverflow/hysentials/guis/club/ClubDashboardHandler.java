package cc.woverflow.hysentials.guis.club;

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.woverflow.hysentials.event.events.GuiMouseClickEvent;
import cc.woverflow.hysentials.handlers.htsl.Navigator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static cc.woverflow.hysentials.guis.club.ClubDashboard.clubhouseSelect;
import static cc.woverflow.hysentials.guis.club.ClubDashboard.instance;
import static cc.woverflow.hysentials.guis.container.GuiItem.getLore;
import static cc.woverflow.hysentials.guis.container.GuiItem.setLore;

public class ClubDashboardHandler {
    @SubscribeEvent
    public void onMouseClick(GuiMouseClickEvent event) {
        if (!clubhouseSelect) return;
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
                String nbt = itemStack.serializeNBT().toString();
                JSONObject jsonObject = new JSONObject();
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("name", ChatColor.Companion.stripControlCodes(name));
                jsonObject1.put("username", username);
                jsonObject1.put("nbt", nbt);

                jsonObject.put("houses", jsonObject1);
                Multithreading.runAsync(() -> {
                    ClubDashboard.update(jsonObject);
                    clubhouseSelect = false;
                    new ClubDashboard().open();
                });
            }
        }
    }
}
