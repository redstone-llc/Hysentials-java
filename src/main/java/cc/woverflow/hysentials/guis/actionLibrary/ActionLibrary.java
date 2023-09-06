package cc.woverflow.hysentials.guis.actionLibrary;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.woverflow.hysentials.util.MUtils;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.woverflow.hysentials.guis.container.Container;
import cc.woverflow.hysentials.guis.container.GuiItem;
import cc.woverflow.hysentials.util.Material;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.MouseEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.client.Minecraft.getMinecraft;

public class ActionLibrary extends Container {
    public ActionLibrary() {
        super("Action Library", 6);
    }
    List<Integer> slots = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 14, 15, 16, 17, 19, 20, 21, 22, 23, 24, 25, 26, 28, 29, 30, 31, 32, 33, 34, 35, 37, 38, 39, 40, 41, 42, 43, 44);
    JSONArray actions = null;
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

        String s = NetworkUtils.getString("http://127.0.0.1:8080/api/actions");
        JSONObject json = new JSONObject(s);
        JSONArray actions = json.getJSONArray("actions");
        this.actions = actions;
        for (int i = 0; i < Math.min(actions.length(), 32); i++) {
            JSONObject action = actions.getJSONObject(i);
            JSONObject actionData = action.getJSONObject("action");
            JSONObject codespace = actionData.getJSONObject("codespace");
            addItem(GuiItem.fromStack(
                GuiItem.makeColorfulItem(Material.PAPER, "&a" + actionData.getString("name"), codespace.getInt("functions"), 0,
                    "&7Creator: &b" + actionData.getString("creator"),
                    "&7Rating: &6" + ((action.getInt("rating") > 0 ? "+" + action.getInt("rating") : action.getInt("rating")) + "✭ &a(+" + action.getInt("ratingsPositive") + "▲) &c(-" + action.getInt("ratingsNegative") + "▼)"),
                    "&7Codespace Required:",
                    "&8 ▪ &a" + codespace.getInt("functions") + " Function Slots",
                    "   &8+" + codespace.getInt("conditions") + " Conditionals",
                    "   &8+" + codespace.getInt("actions") + " Total Actions",
                    "",
                    "&7Function Description:",
                    "&f" + actionData.getString("description"),
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
                if (index < actions.length()) {
                    JSONObject action = actions.getJSONObject(index);
                    if (event.getButton() == 0) {
                        getMinecraft().thePlayer.closeScreen();
                        new ActionViewer(action, actions).open(getMinecraft().thePlayer);
                    } else if (event.getButton() == 1) {
                        StringSelection stringSelection = new StringSelection(action.getString("id"));
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
