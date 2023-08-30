package cc.woverflow.hysentials.guis.misc;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.guis.container.Container;
import cc.woverflow.hysentials.guis.container.GuiItem;
import cc.woverflow.hysentials.util.Material;
import cc.woverflow.hysentials.utils.StringUtilsKt;
import cc.woverflow.hysentials.websocket.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HysentialsLevel extends Container {
    int page;

    public HysentialsLevel(int page) {
        super("Hysentials Level", 6);
        this.page = page;
    }

    public Integer[] slots = new Integer[]{9, 18, 27, 28, 29, 20, 11, 2, 3, 4, 13, 22, 31, 32, 33, 24, 15, 6, 7, 8, 17, 26, 35, 44, 53};

    @Override
    public void setItems() {
        fill(GuiItem.fromStack(BLACK_STAINED_GLASS_PANE));
        setItem(0, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.BLAZE_ROD, "&aHysentials Level", 1, 0, "&7Level: &e" + ((int) getLevel()), "&7Progress to Level " + ((int) getLevel() + 1) + ": &e" + (getProgressString()) + "%", getProgressBar() + " &e" + getExpStart() + "&6/&e" + getExpForLevel((int) getLevel() + 1)))
        );
        for (int i = 0; i < slots.length; i++) {
            int slot = slots[i];
            int level = page * slots.length + i + 1;
            if (level <= ((int) getLevel())) {
                setItem(slot, GuiItem.fromStack(getUnlockedPane(level)));
            } else if (level == ((int) getLevel()) + 1) {
                setItem(slot, GuiItem.fromStack(getWorkingPane(level)));
            } else {
                setItem(slot, GuiItem.fromStack(getLockedPane(level)));
            }
        }
        if (page != 0) {
            setItem(48, GuiItem.fromStack(GuiItem.makeColorfulItem(Material.ARROW, "&aPrevious Page", 1, 0)));
        }
        setItem(49, GuiItem.fromStack(GuiItem.makeColorfulItem(Material.BARRIER, "&cClose", 1, 0)));
        if (((int) getLevel()) >= page * slots.length + slots.length) {
            setItem(50, GuiItem.fromStack(GuiItem.makeColorfulItem(Material.ARROW, "&aNext Page", 1, 0)));
        }
        //refresh exp button may take a few minutes to update data from server
        setItem(40, GuiItem.fromStack(GuiItem.makeColorfulItem(Material.BOOK, "&aRefresh Exp", 1, 0, "&7Click to refresh your exp.", "&7This may take a few minutes to update.", "", "&eClick to refresh.")));
    }

    public float getProgress() {
        return (float) getExpStart() / (float) getExpForLevel(((int) getLevel()) + 1) * 100;
    }


    //round to 2 decimal places
    public String getProgressString() {
        return String.format("%.2f", getProgress());
    }

    public static String getRewards(int level) {
        StringBuilder rewards = new StringBuilder();
        for (Map.Entry<String, Object> entry : Socket.cachedRewards.toMap().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key.equals(String.valueOf(level))) {
                JSONObject json = new JSONObject((Map<?, ?>) value);
                if (json.has("emeralds") && json.has("cosmetic")) {
                    rewards.append("     &8+&a").append(json.getInt("emeralds")).append(" Emeralds").append("\n");
                    rewards.append("     &8+&f").append(StringUtilsKt.toTitleCase(json.getString("cosmetic"))).append(" Cape");
                } else if (json.has("emeralds")) {
                    rewards.append("     &8+&a").append(json.getInt("emeralds")).append(" Emeralds");
                } else if (json.has("cosmetic")) {
                    rewards.append("     &8+&f").append(StringUtilsKt.toTitleCase(json.getString("cosmetic"))).append(" Cape");
                }
            }
        }
        if (rewards.toString().equals(""))
            rewards = new StringBuilder("     &8+&a25 Emeralds");
        return rewards.toString();
    }

    public ItemStack getUnlockedPane(int level) {
        List<String> lore = new ArrayList<>();
        lore.add("&a&lRewards");
        lore.addAll(Arrays.asList(getRewards(level).split("\n")));
        lore.add("");
        lore.add("&aUNLOCKED");
        return GuiItem.makeColorfulItem(
            Material.STAINED_GLASS_PANE,
            "&aHysentials Level " + level,
            1, 5,
            lore
        );
    }

    public ItemStack getWorkingPane(int level) {
        List<String> lore = new ArrayList<>();
        lore.add("&a&lRewards");
        lore.addAll(Arrays.asList(getRewards(level).split("\n")));
        lore.add("");
        lore.add("&7Progress: &e" + (getProgressString()) + "%");
        lore.add(getProgressBar() + " &e" + getExpStart() + "&6/&e" + getExpForLevel((int) getLevel() + 1));
        return GuiItem.makeColorfulItem(
            Material.STAINED_GLASS_PANE,
            "&eHysentials Level " + level,
            1, 4,
            lore
        );
    }

    public ItemStack getLockedPane(int level) {
        List<String> lore = new ArrayList<>();
        lore.add("&a&lRewards");
        lore.addAll(Arrays.asList(getRewards(level).split("\n")));
        return GuiItem.makeColorfulItem(
            Material.STAINED_GLASS_PANE,
            "&cHysentials Level " + level,
            1, 14,
            lore
        );
    }

    public static int getExp() {
        return Socket.cachedData.has("exp") ? Socket.cachedData.getInt("exp") : 0;
    }

    public static float getLevel() {
        float level = 0;
        int exp = getExp();
        while (exp >= getExpForLevel((int) level)) {
            level++;
            exp -= getExpForLevel((int) level);
        }
        level += exp / (float) getExpForLevel((int) level);
        return level;
    }

    public static int getExpStart() {
        float level = 0;
        int exp = getExp();
        while (exp >= getExpForLevel((int) level)) {
            level++;
            exp -= getExpForLevel((int) level);
        }
        return exp;
    }

    public String getProgressBar() {
        float progress = getProgress() / 100f;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            if (progress >= (i + 1) / 15f) {
                sb.append("&a&m");
            } else {
                sb.append("&f&m");
            }
            sb.append("-");
        }
        return sb + "&r";
    }

    /*
        ~ Base + 15% of Previous Requirement
        ~ Base = 1,000 XP. So Level 1 would be 1,000 to obtain and level 2 would be 1,000 + 15% of 1,000 (which is 150) added onto the next requirement for the next level (1,150 XP for Level 2).
     */
    public static int getExpForLevel(int level) {
        int previous = 1000;
        double amount = 0;
        double multiplier = 1.15;
        for (int i = 0; i < level; i++) {
            previous += amount;
            amount = (int) (150 * multiplier);
            multiplier += (i > 10 ? 0.1 : 0.15);
        }
        return previous;
    }

    @Override
    public void handleMenu(MouseEvent event) {

    }

    @Override
    public void setClickActions() {
        setDefaultAction(event -> {
            event.getEvent().cancel();
        });

        setAction(40, event -> {
            event.getEvent().cancel();
            Multithreading.runAsync(() -> {
                String s = NetworkUtils.getString("https://hysentials.redstone.llc/api/exp?username=" + Minecraft.getMinecraft().thePlayer.getGameProfile().getName());
                JSONObject json = new JSONObject(s);
                if (json.has("message") && json.getString("message").equals("You are being ratelimited")) {
                    UChat.chat(HysentialsConfig.chatPrefix + " &cDue to Hypixel API restrictions we are unable to update your level at this time. Please try again in a moment.");
                    return;
                }
                if (json.has("exp") && getExp() != json.getInt("exp")) {
                    checkLevel(json);

                    Minecraft.getMinecraft().thePlayer.closeScreen();
                    new HysentialsLevel(page).open();
                }
            });
        });

        setAction(48, event -> {
            event.getEvent().cancel();
            if (page == 0) return;
            page -= 1;
            update();
        });

        setAction(49, event -> {
            event.getEvent().cancel();
            Minecraft.getMinecraft().thePlayer.closeScreen();
        });

        setAction(50, event -> {
            event.getEvent().cancel();
            if (((int) getLevel()) < page * slots.length + slots.length) return;
            page += 1;
            update();
        });
    }


    public static void checkLevel(JSONObject json) {
        if (json.has("exp") && getExp() != json.getInt("exp")) {
            int previousExp = getExp();
            int currentExp = json.getInt("exp");
            int differenceExp = currentExp - previousExp;

            int previousEmeralds = Socket.cachedData.getInt("emeralds");
            int currentEmeralds = json.getInt("emeralds");
            int differenceEmeralds = currentEmeralds - previousEmeralds;

            int previousLevel = (int) getLevel();
            Socket.cachedData.put("exp", json.getInt("exp"));
            Socket.cachedData.put("emeralds", json.getInt("emeralds"));
            int currentLevel = (int) getLevel();
            int differenceLevel = currentLevel - previousLevel;

            UChat.chat("");
            UChat.chat("&a&lHysentials Level Update!");
            if (differenceEmeralds > 0) UChat.chat("&a+" + differenceEmeralds + "⏣ Emeralds");
            if (differenceExp > 0) UChat.chat("&6+" + differenceExp + " Hysentials XP");
            UChat.chat("");

            if (differenceLevel > 0) {
                for (int i = previousLevel + 1; i <= currentLevel; i++) {
                    UChat.chat("");
                    UChat.chat("&6&lHYSENTIALS LEVEL UP!");
                    UChat.chat("&7You are now &6Hysentials Level " + i + "&7.");
                    UChat.chat("&a &b &c &a&lRewards");
                    Arrays.asList(getRewards(i).split("\n")).forEach(UChat::chat);
                    UChat.chat("");
                }
            }
        }
    }
}
