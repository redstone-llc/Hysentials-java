package cc.woverflow.hysentials.guis.actionLibrary;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.woverflow.hysentials.util.MUtils;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.woverflow.hysentials.guis.container.Container;
import cc.woverflow.hysentials.guis.container.GuiItem;
import cc.woverflow.hysentials.util.Material;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraftforge.client.event.MouseEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.client.Minecraft.getMinecraft;

public class SharedCode extends Container {
    JsonObject clubData;
    public SharedCode(JsonObject clubData) {
        super("Club Code", 6);
        this.clubData = clubData;
    }
    List<Integer> slots = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 14, 15, 16, 17, 19, 20, 21, 22, 23, 24, 25, 26, 28, 29, 30, 31, 32, 33, 34, 35, 37, 38, 39, 40, 41, 42, 43, 44);
    JsonArray actions = null;
    @Override
    public void setItems() {
        setItem(0, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.ACTIVATOR_RAIL, "&aFunctions", 1, 0, "&7Edit and create functions that", "&7can be called from other action", "&7holders.", "", "&eClick to open.")
        ));
        setItem(9, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.STAINED_GLASS_PANE, "&cComing Soon", 1, 1, "&7This feature is not yet available.")
        ));
        setItem(18, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.STAINED_GLASS_PANE, "&cComing Soon", 1, 1, "&7This feature is not yet available.")
        ));
        setItem(27, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.STAINED_GLASS_PANE, "&cComing Soon", 1, 1, "&7This feature is not yet available.")
        ));
        setItem(36, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.STAINED_GLASS_PANE, "&cComing Soon", 1, 1, "&7This feature is not yet available.")
        ));
        setItem(45, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.STAINED_GLASS_PANE, "&cComing Soon", 1, 1, "&7This feature is not yet available.")
        ));
//        setItem(9, GuiItem.fromStack(
//            GuiItem.makeColorfulItem(Material.EMPTY_MAP, "&aGroup Presets", 1, 0, "&7Edit and create groups", "&7that can be saved and shared.", "", "&eClick to open.")
//        ));
//        setItem(18, GuiItem.fromStack(
//            GuiItem.makeColorfulItem(Material.STONE_AXE, "&aLayout Presets", 1, 0, "&7Edit and create layouts", "&7that can be saved and shared.", "", "&eClick to open.")
//        ));
//        setItem(27, GuiItem.fromStack(
//            GuiItem.makeColorfulItem(Material.REDSTONE, "&aConditional Presets", 1, 0, "&7Edit and create conditionals", "&7that can be saved and shared.", "", "&eClick to open.")
//        ));

        JsonArray actions = clubData.getAsJsonArray("actions");
        this.actions = actions;
        for (int i = 0; i < Math.min(actions.size(), 32); i++) {
            JsonObject action = actions.get(i).getAsJsonObject();
            JsonObject actionData = action.getAsJsonObject("action");
            JsonObject codespace = actionData.getAsJsonObject("codespace");
            addItem(GuiItem.fromStack(
                GuiItem.makeColorfulItem(Material.PAPER, "&a" + actionData.get("name").getAsString(), codespace.get("functions").getAsInt(), 0,
                    "&7Creator: &b" + actionData.get("creator").getAsString(),
                    "&7Rating: &6" + ((action.get("rating").getAsInt() > 0 ? "+" + action.get("rating").getAsInt() : action.get("rating").getAsInt()) + "✭ &a(+" + action.get("ratingsPositive").getAsInt() + "▲) &c(-" + action.get("ratingsNegative").getAsInt() + "▼)"),
                    "&7Codespace Required:",
                    "&8 ▪ &a" + codespace.get("functions").getAsInt() + " Function Slots",
                    "   &8+" + codespace.get("conditions").getAsInt() + " Conditionals",
                    "   &8+" + codespace.get("actions").getAsInt() + " Total Actions",
                    "",
                    "&7Function Description:",
                    "&f" + actionData.get("description").getAsString(),
                    "",
                    "&eLeft-Click to open.",
                    "&bRight-Click to copy action."
            )));
        }
    }

    @Override
    public void handleMenu(MouseEvent event) {

    }

    @Override
    public void setClickActions() {
        setDefaultAction((event) -> {
            event.getEvent().cancel();
            if (slots.contains(event.getSlot())) {
                int index = slots.indexOf(event.getSlot());
                if (index < actions.size()) {
                    JsonObject action = actions.get(index).getAsJsonObject();
                    if (event.getButton() == 0) {
                        getMinecraft().thePlayer.closeScreen();
                        new ClubActionViewer(action, actions, clubData).open(getMinecraft().thePlayer);
                    } else if (event.getButton() == 1) {
                        StringSelection stringSelection = new StringSelection(action.get("id").getAsString());
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(stringSelection, null);
                        UChat.chat("&aCopied action ID to clipboard.");
                    }
                }
            }
        });
        setAction(0, (event) -> {
            getMinecraft().thePlayer.closeScreen();
            getMinecraft().thePlayer.sendChatMessage("/function");
        });

    }
}
