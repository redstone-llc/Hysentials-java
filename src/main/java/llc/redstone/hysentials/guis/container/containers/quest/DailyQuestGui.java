package llc.redstone.hysentials.guis.container.containers.quest;

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import llc.redstone.hysentials.guis.container.Container;
import llc.redstone.hysentials.guis.container.GuiItem;
import llc.redstone.hysentials.handlers.misc.QuestHandler;
import llc.redstone.hysentials.quest.Quest;
import llc.redstone.hysentials.util.Material;
import llc.redstone.hysentials.websocket.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DailyQuestGui extends Container {
    public DailyQuestGui() {
        super("Daily Quests", 6);
    }

    @Override
    public void setItems() {
        border(GuiItem.fromStack(this.BLACK_STAINED_GLASS_PANE));
        int i = 0;
        boolean isActive = false;
        for (Quest quest : Quest.questInstances) {
            if (!quest.isDaily()) continue;
            List<String> lore = new ArrayList<>();
            if (!quest.inRotation) continue;
            List<String> description = GuiItem.stringToLore(quest.getDescription(), 30, ChatColor.GRAY);
            if (!quest.isActive && !quest.isCompleted) {
                lore.addAll(description);
                lore.add("&8(&c" + quest.progress + "&8/&a" + quest.getGoal() + "&8)");
                lore.add("");
                lore.add("&6&lREWARDS");
                lore.addAll(quest.rewards);
                lore.add("");
                lore.add("&eClick to Start Quest!");
            } else if (quest.isActive && !quest.isCompleted) {
                lore.addAll(description);
                lore.add("&8(&e" + quest.progress + "&8/&a" + quest.getGoal() + "&8)");
                lore.add("");
                lore.add("&6&lREWARDS");
                lore.addAll(quest.rewards);
                lore.add("");
                lore.add("&aQuest Active!");
                lore.add("&eClick to Cancel Quest!");
            } else if (quest.isCompleted) {
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
            if (quest.isActive) {
                item.addEnchantment(Enchantment.lure, 10);
                GuiItem.hideFlag(item, 1);
            }
            addItem(GuiItem.fromStack(item));
        }
        setItem(48, GuiItem.fromStack(GuiItem.makeColorfulItem(Material.ARROW, "&aGo Back", 1, 0, "&7To the Main Quest Menu!")));
        setItem(49, GuiItem.fromStack(GuiItem.makeColorfulItem(Material.BARRIER, "&cClose", 1, 0)));
        setItem(50, GuiItem.fromStack(GuiItem.makeColorfulSkullItem("&dRe-Roll Quests", "http://textures.minecraft.net/texture/7881cc2747ba72cbcb06c3cc331742cd9de271a5bbffd0ecb14f1c6a8b69bc9e", 1,
            Arrays.asList("&7Replaces your current quests", "&7with brand-new ones!", "",
                "&7Cost:", "&a200 Emeralds", "", "&eClick to Re-Roll!")
        )));
    }

    @Override
    public void handleMenu(MouseEvent event) {

    }

    @Override
    public void setClickActions() {
        setDefaultAction(event -> {
            event.getEvent().cancel();
            if (event.getSlot() == 49 || event.getSlot() == 50 || event.getSlot() == 48) return;
            if (guiItems.get(event.getSlot()).getItemStack().hasDisplayName()) {
                for (Quest quest : Quest.questInstances) {
                    if (!quest.isDaily()) continue;
                    if (!quest.inRotation || !guiItems.get(event.getSlot()).getItemStack().getDisplayName().equals("§a" + quest.getName())) {
                        continue;
                    }
                    if (!quest.isCompleted) {
                        quest.isActive = !quest.isActive;
                        QuestHandler.enableQuest(quest);
                        Multithreading.schedule(() -> {
                            Minecraft.getMinecraft().thePlayer.closeScreen();
                            new DailyQuestGui().open();
                        }, 1000, TimeUnit.MILLISECONDS);
                        UChat.chat("§aToggled quest " + quest.getName() + " to " + (quest.isActive ? "§aenabled" : "§cdisabled"));
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

        setAction(50, (event) -> {
            event.getEvent().cancel();
            int emeralds = (Socket.cachedUser == null) ? 0 : Socket.cachedUser.getEmeralds();
            if (emeralds >= 200) {
                UChat.chat("§aRe-rolling quests...");
                QuestHandler.rerollQuest();
                Multithreading.schedule(() -> {
                    Minecraft.getMinecraft().thePlayer.closeScreen();
                    new DailyQuestGui().open();
                }, 1000, TimeUnit.MILLISECONDS);
            } else {
                UChat.chat("§cYou do not have enough emeralds to re-roll your quests!");
            }
        });
    }
}
