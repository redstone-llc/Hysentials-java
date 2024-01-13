package llc.redstone.hysentials.guis.quest;

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import llc.redstone.hysentials.guis.container.Container;
import llc.redstone.hysentials.guis.container.GuiItem;
import llc.redstone.hysentials.handlers.misc.QuestHandler;
import llc.redstone.hysentials.quest.Quest;
import llc.redstone.hysentials.util.Material;
import llc.redstone.hysentials.guis.container.Container;
import llc.redstone.hysentials.guis.container.GuiItem;
import llc.redstone.hysentials.handlers.misc.QuestHandler;
import llc.redstone.hysentials.quest.Quest;
import llc.redstone.hysentials.util.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuestGui extends Container {
    public QuestGui() {
        super("Bucket List", 6);
    }

    @Override
    public void setItems() {
        border(GuiItem.fromStack(this.BLACK_STAINED_GLASS_PANE));
        int i = 0;
        for (Quest quest : Quest.questInstances) {
            if (quest.isDaily()) continue;
            List<String> lore = new ArrayList<>();
            List<String> description = GuiItem.stringToLore(quest.getDescription(), 30, ChatColor.GRAY);
            if (!quest.isCompleted) {
                lore.addAll(description);
                lore.add("&8(&c" + quest.progress + "&8/&a" + quest.getGoal() + "&8)");
                lore.add("");
                lore.add("&6&lREWARDS");
                lore.addAll(quest.rewards);
                lore.add("");
                lore.add("&eClick to Check Progress!");
            } else {
                lore.addAll(description);
                lore.add("&8(&a" + quest.getGoal() + "&8/&a" + quest.getGoal() + "&8)");
                lore.add("");
                lore.add("&6&lREWARDS");
                lore.addAll(quest.rewards);
                lore.add("");
                lore.add("&aQuest Completed!");
            }
            i++;
            ItemStack item = GuiItem.makeColorfulItem(Material.BOOK, "&a" + quest.getName(), 1, 0, lore);
            if (quest.isCompleted) {
                item.addEnchantment(Enchantment.lure, 10);
                GuiItem.hideFlag(item, 1);
            }
            addItem(GuiItem.fromStack(item));
        }
        setItem(48, GuiItem.fromStack(GuiItem.makeColorfulItem(Material.ARROW, "&aGo Back", 1, 0, "&7To the Main Quest Menu!")));
        setItem(49, GuiItem.fromStack(GuiItem.makeColorfulItem(Material.BARRIER, "&cClose", 1, 0)));
    }

    @Override
    public void handleMenu(MouseEvent event) {

    }

    @Override
    public void setClickActions() {
        setDefaultAction(event -> {
            event.getEvent().cancel();
            if (event.getSlot() == 49 || event.getSlot() == 48) return;
            if (guiItems.get(event.getSlot()).getItemStack().hasDisplayName()) {
                for (Quest quest : Quest.questInstances) {
                    if (quest.isDaily()) continue;
                    if (!guiItems.get(event.getSlot()).getItemStack().getDisplayName().equals("§a" + quest.getName())) {
                        continue;
                    }
                    if (!quest.isCompleted) {
                        QuestHandler.checkQuest(quest);
                        Multithreading.schedule(() -> {
                            Minecraft.getMinecraft().thePlayer.closeScreen();
                            new QuestGui().open();
                        }, 1000, TimeUnit.MILLISECONDS);
                        UChat.chat("§aChecking Quest Progress...");
                    } else {
                        UChat.chat("§cYou have already completed this quest!");
                    }
                }
            }
        });

        setAction(48, (event) -> {
            event.getEvent().cancel();
            Minecraft.getMinecraft().thePlayer.closeScreen();
            new QuestMainGui().open();
        });

        setAction(49, (event) -> {
            event.getEvent().cancel();
            Minecraft.getMinecraft().thePlayer.closeScreen();
        });
    }
}
