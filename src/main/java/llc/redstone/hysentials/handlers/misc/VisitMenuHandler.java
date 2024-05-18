package llc.redstone.hysentials.handlers.misc;

import kotlin.Pair;
import llc.redstone.hysentials.config.hysentialMods.HousingConfig;
import llc.redstone.hysentials.event.events.GuiLoadedEvent;
import llc.redstone.hysentials.event.events.RenderItemInGuiEvent;
import llc.redstone.hysentials.polyui.ui.VisitHouseScreen;
import llc.redstone.hysentials.util.C;
import llc.redstone.hysentials.utils.GuiUtilsKt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static llc.redstone.hysentials.handlers.htsl.Navigator.*;

public class VisitMenuHandler {
    @SubscribeEvent
    public void onGuiOpen(GuiLoadedEvent event) {
        if (event.name.endsWith("'s Houses") && event.gui instanceof GuiContainer) {
            if (HousingConfig.fancyVisitMenu) {
                List<ItemStack> items = Minecraft.getMinecraft().thePlayer.openContainer.inventorySlots.stream().map(Slot::getStack).collect(Collectors.toList());
                items = items.subList(0, items.size() - 36 - 9);
                List<Pair<String, Integer>> houses = new ArrayList<>();
                for (int i = 0; i < items.size(); i++) {
                    ItemStack item = items.get(i);
                    if (item == null) continue;
                    int playing = 0;
                    String[] split = C.removeColor(GuiUtilsKt.getLore(item).get(4)).split(": ");
                    if (split.length == 2) {
                        playing = Integer.parseInt(split[1]);
                    }
                    houses.add(new Pair<>(item.getDisplayName().replace("§r", ""), playing));
                }
                String name = event.name.replace("'s Houses", "");
                Minecraft.getMinecraft().thePlayer.closeScreen();
                new VisitHouseScreen().open(name, houses);
            }
        }
    }
}
