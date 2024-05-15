package llc.redstone.hysentials.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.annotations.Button;
import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.core.ConfigUtils;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.elements.BasicOption;
import cc.polyfrost.oneconfig.config.elements.OptionPage;
import cc.polyfrost.oneconfig.gui.OneConfigGui;
import cc.polyfrost.oneconfig.hud.BasicHud;
import cc.polyfrost.oneconfig.hud.SingleTextHud;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.UKeyboard;
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack;
import cc.polyfrost.oneconfig.renderer.NanoVGHelper;
import cc.polyfrost.oneconfig.utils.InputHandler;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.polyfrost.oneconfig.utils.gui.GuiUtils;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.hysentialMods.*;
import llc.redstone.hysentials.config.hysentialMods.page.PageAnnotation;
import llc.redstone.hysentials.config.hysentialMods.page.PageOption;
import llc.redstone.hysentials.config.hysentialMods.rank.RankAnnotation;
import llc.redstone.hysentials.config.hysentialMods.rank.RankOption;
import llc.redstone.hysentials.cosmetic.CosmeticGui;
import llc.redstone.hysentials.guis.sbBoxes.huds.ActionBarHUD;
import llc.redstone.hysentials.guis.sbBoxes.huds.BossbarSbBoxHud;
import llc.redstone.hysentials.guis.sbBoxes.huds.HeldItemTooltipHUD;
import llc.redstone.hysentials.macrowheel.overlay.MacroWheelHudConfigThing;
import llc.redstone.hysentials.macrowheel.overlay.MacroWheelOverlay;
import llc.redstone.hysentials.updateGui.UpdateChecker;

import llc.redstone.hysentials.util.ImageIconRenderer;
import llc.redstone.hysentials.util.Renderer;
import llc.redstone.hysentials.utils.RedstoneRepo;
import llc.redstone.hysentials.utils.UpdateNotes;
import cc.woverflow.hytils.HytilsReborn;
import net.minecraft.client.Minecraft;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static llc.redstone.hysentials.guis.actionLibrary.ActionViewer.toList;

public class HysentialsConfig extends Config {

    @PageAnnotation(
        name = "Chat, Formatting, and Housing",
        group = true,
        category = "Hysentials Mods",
        subcategory = "Hysentials Mods",
        description = "Chat related settings."
    )
    public transient List<Config> hysentialMods = Arrays.asList(
        new ChatConfig(),
        new FormattingConfig(),
        new HousingConfig()
    );
    @PageAnnotation(
        name = "Lobby, Scoreboards, and Cosmetics",
        group = true,
        category = "Hysentials Mods",
        subcategory = "Hysentials Mods",
        description = "Lobby related settings."
    )
    public transient List<Config> hysentialMods2 = Arrays.asList(
        new LobbyConfig(),
        new ScorebarsConfig(),
        new CosmeticConfig()
    );
    @PageAnnotation(
        name = "Icons and Replace",
        category = "Hysentials Mods",
        subcategory = "Hysentials Mods",
        description = "Icon and replace related settings.",
        group = true
    )
    public transient List<Config> hysentialMods3 = Arrays.asList(
        new IconsConfig(),
        new ReplaceConfig()
    );

    public transient IconsConfig iconsConfig = (IconsConfig) hysentialMods3.get(0);
    public transient ReplaceConfig replaceConfig = (ReplaceConfig) hysentialMods3.get(1);
    public transient ChatConfig chatConfig = (ChatConfig) hysentialMods.get(0);
    public transient FormattingConfig formattingConfig = (FormattingConfig) hysentialMods.get(1);
    public transient HousingConfig housingConfig = (HousingConfig) hysentialMods.get(2);
    public transient LobbyConfig lobbyConfig = (LobbyConfig) hysentialMods2.get(0);
    public transient ScorebarsConfig scorebarsConfig = (ScorebarsConfig) hysentialMods2.get(1);
    public transient CosmeticConfig cosmeticConfig = (CosmeticConfig) hysentialMods2.get(2);

    @KeyBind(
        name = "Command Wheel",
        category = "General",
        subcategory = "Keybinds",
        description = "The keybind to open the macro wheel."
    )
    public static OneKeyBind macroWheelKeyBind = new OneKeyBind(UKeyboard.KEY_G);

    @HUD(
        name = "Command Wheel HUD",
        category = "HUD",
        subcategory = "Command Wheel"
    )
    public MacroWheelHudConfigThing macroWheelHud = new MacroWheelHudConfigThing(true, 0, 0,
        5f, true, new OneColor(119, 119, 119, 150));

    @HUD(
        name = "Actionbar HUD",
        category = "HUD",
        subcategory = "Actionbar"
    )
    public static ActionBarHUD actionBarHUD = new ActionBarHUD();

    @HUD(
        name = "Boss Bar HUD",
        category = "HUD",
        subcategory = "Boss Bar"
    )
    public static BossbarSbBoxHud bossBarHUD = new BossbarSbBoxHud();

    @HUD(
        name = "Held Item Tooltip HUD",
        category = "HUD",
        subcategory = "Held Item Tooltip"
    )
    public static HeldItemTooltipHUD heldItemTooltipHUD = new HeldItemTooltipHUD();

    @Text(
        name = "Chat Prefix",
        category = "General",
        subcategory = "General",
        description = "The prefix of most Hysentials related messages, so you know the message is a result of Hysentials and not other mods."
    )
    public static String chatPrefix = "&b[HYSENTIALS]";

    @Dropdown(
        name = "Update Channel",
        category = "General",
        subcategory = "General",
        description = "The update channel you want to use.",
        options = {"Release", "Beta", "Dev"}
    )
    public static int updateChannel = 1;

    @Button(
        name = "Check for Updates",
        category = "General",
        subcategory = "General",
        description = "Check for updates for Hysentials.",
        text = "Check for Updates"
    )
    public void checkForUpdates() {
        UpdateChecker.Companion.checkUpdateAndOpenMenu();
    }

    @KeyBind(
        name = "Open Cosmetics",
        category = "General",
        subcategory = "Keybinds",
        description = "The keybind to open the cosmetics menu."
    )
    public static OneKeyBind keyBind = new OneKeyBind(UKeyboard.KEY_K);

    @KeyBind(
        name = "Open Online Players",
        category = "General",
        subcategory = "Keybinds",
        description = "The keybind to open the online players menu."
    )
    public static OneKeyBind onlinePlayersKeyBind = new OneKeyBind(UKeyboard.KEY_LCONTROL, UKeyboard.KEY_TAB);


    @Switch(
        name = "Global Chat Enabled",
        category = "General",
        subcategory = "Chat",
        description = "Enable global chat. This will allow you to chat with other players who are using Hysentials."
    )
    public static boolean globalChatEnabled = true;

    @Button(
        name = "Install Hytils Reborn",
        category = "General",
        subcategory = "Hytils",
        description = "Installs the Hytils Reborn mod for you.",
        text = "Install")
    public void installHytils() {
        if (!Hysentials.INSTANCE.isHytils) {
            Hysentials.INSTANCE.getLogger().info("Installing Hytils Reborn...");
            try {
                String request1 = NetworkUtils.getString("https://api.modrinth.com/v2/project/nF6YaBfO");
                JSONObject json1 = new JSONObject(request1);
                if (json1.isEmpty()) {
                    Hysentials.INSTANCE.getLogger().error("Error installing Hytils Reborn!");
                    return;
                }
                List<Object> versions = toList(json1.getJSONArray("versions"));
                String latestVersion = ((String) versions.get(versions.size() - 1));
                String request2 = NetworkUtils.getString("https://api.modrinth.com/v2/version/" + latestVersion);
                JSONObject json2 = new JSONObject(request2);
                if (json2.isEmpty()) {
                    Hysentials.INSTANCE.getLogger().error("Error installing Hytils Reborn!");
                    return;
                }
                JSONObject file = json2.getJSONArray("files").getJSONObject(0);
                RedstoneRepo repo = new RedstoneRepo(
                    file.getString("filename"),
                    "jar",
                    file.getInt("size"),
                    json2.getString("version_type"),
                    file.getString("url")
                );
                UpdateNotes notes = new UpdateNotes(
                    "Install Hytils Reborn",
                    json2.getString("name").replace("Hytils Reborn-1.8.9-", ""),
                    "https://cdn.modrinth.com/data/nF6YaBfO/5de4ce522bbc4af9229018cbaeefb117ec458648.png",
                    json2.getString("changelog") + "\n\n" +
                        "Hytils Reborn is not affiliated with Hysentials, and is a separate mod. \n" +
                        "However we do recommend using it for the best experience. \n" +
                        "Created by Polyfrost and their team."
                );

                UpdateChecker.Companion.installFromUrl(repo, notes);
            } catch (Exception e) {
                Hysentials.INSTANCE.getLogger().error("Error installing Hytils Reborn!");
                e.printStackTrace();
            }
        } else {
            Hysentials.INSTANCE.getLogger().error("Hytils Reborn is already installed!");
        }
    }

    @Button(
        name = "Additional Configs",
        category = "General",
        subcategory = "Hytils",
        description = "Opens the Hytils Reborn config page.",
        text = "OPEN")
    public void openHytilsConfig() {
        if (Hysentials.INSTANCE.isHytils) {
            HytilsReborn.INSTANCE.getConfig().openGui();
        } else {
            Hysentials.INSTANCE.getLogger().error("Hytils Reborn is not installed!");
        }
    }


    public static boolean wardrobeDarkMode = true; //dont touch this


    public HysentialsConfig() {
        super(new Mod("Hysentials", ModType.HYPIXEL), "hysentials.json");
        this.initialize();
        this.hideIf("openHytilsConfig", () -> !Hysentials.INSTANCE.isHytils);
        this.hideIf("installHytils", () -> Hysentials.INSTANCE.isHytils);
        this.registerKeyBind(keyBind, () -> {
            Minecraft.getMinecraft().thePlayer.closeScreen();
            Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(new CosmeticGui());
        });
    }



    @Override
    protected BasicOption getCustomOption(Field field, CustomOption annotation, OptionPage page, Mod mod, boolean migrate) {
        BasicOption option = null;
        switch (annotation.id()) {
            case "customPage":
                PageAnnotation myOption =  ConfigUtils.findAnnotation(field, PageAnnotation.class);
                option = PageOption.create(field, this);
                ConfigUtils.getSubCategory(page, myOption.category(), myOption.subcategory()).options.add(option);
                break;
        }
        return option;
    }
}
