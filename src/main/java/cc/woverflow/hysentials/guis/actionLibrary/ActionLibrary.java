package cc.woverflow.hysentials.guis.actionLibrary;

import cc.polyfrost.oneconfig.libs.checker.units.qual.A;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.woverflow.hysentials.HysentialsUtilsKt;
import cc.woverflow.hysentials.schema.HysentialsSchema;
import cc.woverflow.hysentials.util.MUtils;
import cc.woverflow.hysentials.guis.container.Container;
import cc.woverflow.hysentials.guis.container.GuiItem;
import cc.woverflow.hysentials.util.Material;
import cc.woverflow.hysentials.util.NetworkUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
    List<HysentialsSchema.Action> actions = null;
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

        JsonElement a = NetworkUtils.getJsonElement(HysentialsUtilsKt.getHYSENTIALS_API() + "/actions", true);
        if (a == null || !a.isJsonObject()) {
            UChat.chat("&cFailed to load actions. Please try again later.");
            return;
        }
        JsonObject json = a.getAsJsonObject();
        List<HysentialsSchema.Action> actions = new ArrayList<>();
        json.getAsJsonArray("actions").forEach(action -> {
            HysentialsSchema.Action a1 = HysentialsSchema.Action.Companion.deserialize(action.getAsJsonObject());
            actions.add(a1);
        });
        this.actions = actions;
        for (int i = 0; i < Math.min(actions.size(), 32); i++) {
            HysentialsSchema.Action action = actions.get(i);
            HysentialsSchema.ActionData actionData = action.getAction();
            HysentialsSchema.ActionCodespace codespace = actionData.getCodespace();
            addItem(GuiItem.fromStack(
                GuiItem.makeColorfulItem(Material.PAPER, "&a" + actionData.getName(), codespace.getFunctions(), 0,
                    "&7Creator: &b" + actionData.getCreator(),
                    "&7Rating: &6" + ((action.getRating() > 0 ? "+" + action.getRating() : action.getRating()) + "✭ &a(+" + action.getRatingsPositive() + "▲) &c(-" + action.getRatingsNegative() + "▼)"),
                    "&7Codespace Required:",
                    "&8 ▪ &a" + codespace.getFunctions() + " Function Slots",
                    "   &8+" + codespace.getConditions() + " Conditionals",
                    "   &8+" + codespace.getActions() + " Total Actions",
                    "",
                    "&7Function Description:",
                    "&f" + actionData.getDescription(),
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
                    HysentialsSchema.Action action = actions.get(index);
                    if (event.getButton() == 0) {
                        getMinecraft().thePlayer.closeScreen();
                        new ActionViewer(action, actions).open(getMinecraft().thePlayer);
                    } else if (event.getButton() == 1) {
                        StringSelection stringSelection = new StringSelection(action.getId());
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
