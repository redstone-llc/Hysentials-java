package llc.redstone.hysentials.config.hysentialMods;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Button;
import cc.polyfrost.oneconfig.config.annotations.CustomOption;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.ConfigUtils;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.elements.BasicOption;
import cc.polyfrost.oneconfig.config.elements.OptionPage;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.config.hysentialMods.rank.RankAnnotation;
import llc.redstone.hysentials.config.hysentialMods.rank.RankOption;
import llc.redstone.hysentials.config.hysentialMods.rank.RankStuff;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.hysentialMods.rank.RankAnnotation;
import llc.redstone.hysentials.config.hysentialMods.rank.RankOption;
import llc.redstone.hysentials.config.hysentialMods.rank.RankStuff;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;

public class FormattingConfig extends Config {
    @Switch(
        name = "Fancy Ranks",
        category = "General",
        subcategory = "Fancy Formatting",
        description = "Enable futuristic ranks. This will allow you to see an image as a users rank, aswell as other things."
    )
    public static boolean futuristicRanks = true;

    @Switch(
        name = "Hex Colors",
        category = "General",
        subcategory = "Fancy Formatting",
        description = "Enable hex colors. This will allow you to see hex colors in chat."
    )
    public static boolean hexColors = true;

    @Switch(
        name = "Show Fancy Rank in Tab",
        category = "General",
        subcategory = "Fancy Formatting",
        description = "Show the fancy rank in the tab menu."
    )
    public static boolean fancyRankInTab = true;

    @Switch(
        name = "Show Fancy Rank in Chat",
        category = "General",
        subcategory = "Fancy Formatting",
        description = "Show the fancy rank in chat."
    )
    public static boolean fancyRankInChat = true;

    @Switch(
        name = "Simplistic Rank in Tab",
        category = "General",
        subcategory = "Fancy Formatting",
        description = "Show the simplistic rank in tab. These are only for hysentials ranks and custom ranks."
    )
    public static boolean simplisticRankInTab = false;

    @Switch(
        name = "Simplistic Rank in Chat",
        category = "General",
        subcategory = "Fancy Formatting",
        description = "Show the simplistic rank in chat. These are only for hysentials ranks and custom ranks."
    )
    public static boolean simplisticRankInChat = false;

    @Button(
        name = "Rank Image Config",
        category = "General",
        subcategory = "Fancy Formatting",
        description = "Opens the rank image config folder.",
        text = "Open Folder")
    public void openRankImageConfig() {
        Desktop desktop = Desktop.getDesktop();
        File directory = new File("./config/hysentials/imageicons");
        try {
            desktop.open(directory);
        } catch (Exception e) {
            UChat.chat("&cError opening folder!");
            e.printStackTrace();
        }
    }

    @Button(
        name = "Hex Color Config",
        category = "General",
        subcategory = "Fancy Formatting",
        description = "Opens the rank hex color config file.",
        text = "Open File")
    public void openRankHexConfig() {
        Desktop desktop = Desktop.getDesktop();
        File directory = new File("./config/hysentials/colors.json");
        try {
            desktop.open(directory);
        } catch (Exception e) {
            UChat.chat("&cError opening file!");
            e.printStackTrace();
        }
    }


    @RankAnnotation(
        name = "Default Rank",
        category = "Rank",
        subcategory = "Hypixel Ranks",
        defaultNametagColor = "7d838e",
        defaultChatMessageColor = "cbd3e0"
    )
    public static RankStuff defaultRank = new RankStuff(new OneColor("7d838e"), new OneColor("cbd3e0"));

    @RankAnnotation(
        name = "VIP Ranks",
        category = "Rank",
        subcategory = "Hypixel Ranks",
        defaultNametagColor = "80c415",
        defaultChatMessageColor = "e8f8c8"
    )
    public static RankStuff vipRank = new RankStuff(new OneColor("80c415"), new OneColor("e8f8c8"));

    @RankAnnotation(
        name = "Blue MVP Ranks",
        category = "Rank",
        subcategory = "Hypixel Ranks",
        defaultNametagColor = "0fa2e5",
        defaultChatMessageColor = "dceefb"
    )
    public static RankStuff blueMvpRank = new RankStuff(new OneColor("0fa2e5"), new OneColor("dceefb"));

    @RankAnnotation(
        name = "Gold MVP Ranks",
        category = "Rank",
        subcategory = "Hypixel Ranks",
        defaultNametagColor = "e4b108",
        defaultChatMessageColor = "edebba"
    )
    public static RankStuff goldMvpRank = new RankStuff(new OneColor("e4b108"), new OneColor("edebba"));

    @RankAnnotation(
        name = "Youtube Rank",
        category = "Rank",
        subcategory = "Hypixel Ranks",
        defaultNametagColor = "ff2f2e",
        defaultChatMessageColor = "fff7f7"
    )
    public static RankStuff youtubeRank = new RankStuff(new OneColor("ff2f2e"), new OneColor("fff7f7"));

    @RankAnnotation(
        name = "Npc Rank",
        category = "Rank",
        subcategory = "Hypixel Ranks",
        defaultNametagColor = "555555",
        defaultChatMessageColor = "555555"
    )
    public static RankStuff npcRank = new RankStuff(new OneColor("555555"), new OneColor("555555"));


    @RankAnnotation(
        name = "Creator Rank",
        category = "Rank",
        subcategory = "Hysentials Ranks",
        defaultNametagColor = "13b9a7",
        defaultChatMessageColor = "baf5ee"
    )
    public static RankStuff creatorRank = new RankStuff(new OneColor("13b9a7"), new OneColor("baf5ee"));

    @RankAnnotation(
        name = "Helper Rank",
        category = "Rank",
        subcategory = "Hysentials Ranks",
        defaultNametagColor = "108cff",
        defaultChatMessageColor = "bfd9ff"
    )
    public static RankStuff helperRank = new RankStuff(new OneColor("108cff"), new OneColor("bfd9ff"));

    @RankAnnotation(
        name = "Moderator Rank",
        category = "Rank",
        subcategory = "Hysentials Ranks",
        defaultNametagColor = "00AA00",
        defaultChatMessageColor = "d4ffdf"
    )
    public static RankStuff modRank = new RankStuff(new OneColor("00AA00"), new OneColor("d4ffdf"));

    @RankAnnotation(
        name = "Team Rank",
        category = "Rank",
        subcategory = "Hysentials Ranks",
        defaultNametagColor = "ec6f19",
        defaultChatMessageColor = "fff7f0"
    )
    public static RankStuff teamRank = new RankStuff(new OneColor("ec6f19"), new OneColor("fff7f0"));

    @RankAnnotation(
        name = "Admin Rank",
        category = "Rank",
        subcategory = "Hysentials Ranks",
        defaultNametagColor = "ea323c",
        defaultChatMessageColor = "f5c0c0"
    )
    public static RankStuff adminRank = new RankStuff(new OneColor("ea323c"), new OneColor("f5c0c0"));


    public FormattingConfig() {
        super(new Mod("Formatting", ModType.UTIL_QOL, "/assets/hysentials/mods/formats.png", 244, 80), "hysentials-formatting.json");
        initialize();
        addDependency("fancyRankInTab", "futuristicRanks");
        addDependency("fancyRankInChat", "futuristicRanks");

        addListener("futuristicRanks", () -> {
            if (FormattingConfig.fancyRendering()) {
                Minecraft.getMinecraft().fontRendererObj = Hysentials.INSTANCE.imageIconRenderer;
            } else {
                Minecraft.getMinecraft().fontRendererObj = Hysentials.minecraftFont;
            }
        });

        addListener("hexColors", () -> {
            if (FormattingConfig.fancyRendering()) {
                Minecraft.getMinecraft().fontRendererObj = Hysentials.INSTANCE.imageIconRenderer;
            } else {
                Minecraft.getMinecraft().fontRendererObj = Hysentials.minecraftFont;
            }
        });
    }

    public void initialize() {
        boolean migrate = false;
        File profileFile = ConfigUtils.getProfileFile(configFile);
        if (profileFile.exists()) load();
        if (!profileFile.exists()) {
            if (mod.migrator != null) migrate = true;
            else save();
        }
        mod.config = this;
        generateOptionList(this, mod.defaultPage, mod, migrate);
        if (migrate) save();
    }

    @Override
    protected BasicOption getCustomOption(Field field, CustomOption annotation, OptionPage page, Mod mod, boolean migrate) {
        BasicOption option = null;
        switch (annotation.id()) {
            case "rank":
                RankAnnotation myOption =  ConfigUtils.findAnnotation(field, RankAnnotation.class);
                option = RankOption.create(field, this);
                ConfigUtils.getSubCategory(page, myOption.category(), myOption.subcategory()).options.add(option);
                break;
        }
        return option;
    }

    public static RankStuff getRank(String type) {
        for (Field field : FormattingConfig.class.getDeclaredFields()) {
            RankAnnotation myOption =  ConfigUtils.findAnnotation(field, RankAnnotation.class);
            if (myOption != null && field.getName().replace("Rank", "").toLowerCase().equals(type)) {
                try {
                    return (RankStuff) field.get(null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return defaultRank;
    }

    public static boolean fancyRendering() {
        return (futuristicRanks || hexColors) && Hysentials.INSTANCE.getConfig().formattingConfig.enabled;
    }

    public static boolean hexRendering() {
        return hexColors && Hysentials.INSTANCE.getConfig().formattingConfig.enabled;
    }
}
