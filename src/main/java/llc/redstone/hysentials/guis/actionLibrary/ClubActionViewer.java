package llc.redstone.hysentials.guis.actionLibrary;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import llc.redstone.hysentials.schema.HysentialsSchema;
import llc.redstone.hysentials.guis.container.Container;
import llc.redstone.hysentials.guis.container.GuiItem;
import llc.redstone.hysentials.util.Material;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import llc.redstone.hysentials.schema.HysentialsSchema;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.MouseEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;

public class ClubActionViewer extends Container {
    HysentialsSchema.Action action;
    ArrayList<HysentialsSchema.Action> actions;
    HysentialsSchema.Club club;

    public ClubActionViewer(HysentialsSchema.Action action, ArrayList<HysentialsSchema.Action> actions, HysentialsSchema.Club club) {
        super("Viewing: " + action.getId(), 4);
        this.action = action;
        this.actions = actions;
        this.club = club;
    }

    @Override
    public void setItems() {
        HysentialsSchema.ActionData actionData = action.getAction();
        HysentialsSchema.ActionCodespace codespace = actionData.getCodespace();
        setItem(13, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.PAPER, "&a" + actionData.getName(), codespace.getFunctions(), 0,
                "&7Creator: &b" + actionData.getCreator(),
                "&7Codespace Required:",
                "&8 ▪ &a" + codespace.getFunctions() + " Function Slots",
                "   &8+" + codespace.getConditions() + " Conditionals",
                "   &8+" + codespace.getActions() + " Total Actions",
                "",
                "&7Function Description:",
                "&f" + actionData.getDescription()
            )
        ));

        setItem(48-18, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.CHEST, "&aCopy Action ID", 1, 0,
                "&7Click to copy the action ID.",
                "",
                "&eLeft-Click to copy."
            )
        ));
        setItem(49-18, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.BARRIER, "&aGo Back", 1, 0,
                "&7To Action Library"
            )
        ));
        setItem(50-18, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.NETHER_STAR, "&0&9&4&aBookmark Function", 1, 0,
                "&7Click to bookmark this action.",
                "",
                "&eLeft-Click to bookmark."
            )
        ));
        if (actions.indexOf(action) != 0) {
            setItem(47 - 18, GuiItem.fromStack(
                GuiItem.makeColorfulItem(Material.ARROW, "&aPrevious Action", 1, 0,
                    "&7Click to view the previous action.",
                    "",
                    "&eLeft-Click to view."
                )
            ));
        }

        if (actions.indexOf(action) != actions.size() - 1) {
            setItem(51 - 18, GuiItem.fromStack(
                GuiItem.makeColorfulItem(Material.ARROW, "&aNext Action", 1, 0,
                    "&7Click to view the next action.",
                    "",
                    "&eLeft-Click to view."
                )
            ));
        }

        for (int i = 0; i < rows * 9; i++) {
            if (guiItems.containsKey(i)) continue;
            setItem(i, GuiItem.fromStack(
                GuiItem.makeColorfulItem(Material.STAINED_GLASS_PANE, "&1&1&5&0", 1, 15)
            ));
        }

    }

    @Override
    public void handleMenu(MouseEvent event) {

    }

    @Override
    public void setClickActions() {
        setDefaultAction((event) -> {
            event.getEvent().cancel();
        });

        setAction(48 - 18, (event) -> {
            event.getEvent().cancel();
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(action.getId()), null);
            UChat.chat("&aCopied action ID to clipboard.");
        });

        setAction(49 - 18, (event) -> {
            event.getEvent().cancel();
            Minecraft.getMinecraft().thePlayer.closeScreen();
            new SharedCode(club).open(Minecraft.getMinecraft().thePlayer);
        });

        setAction(50 - 18, (event -> {
            UChat.chat("&cThis feature is not yet implemented.");
        }));

        setAction(47 - 18, (event) -> {
            event.getEvent().cancel();
            Minecraft.getMinecraft().thePlayer.closeScreen();
            if (actions.indexOf(action) == 0) return;
            float mouseX = Mouse.getX();
            float mouseY = Mouse.getY();
            action = actions.get(actions.indexOf(action) - 1);
            update();
//            new ClubActionViewer(actions.getJSONObject(toList(actions).indexOf(action) - 1), actions).open(Minecraft.getMinecraft().thePlayer);
//            Multithreading.schedule(() -> {
//                Mouse.setCursorPosition((int) mouseX, (int) mouseY);
//            }, 50, TimeUnit.MILLISECONDS);
        });

        setAction(51 - 18, (event) -> {
            event.getEvent().cancel();
            if (actions.indexOf(action) == actions.size() - 1) return;
            Minecraft.getMinecraft().thePlayer.closeScreen();
            float mouseX = Mouse.getX();
            float mouseY = Mouse.getY();
            action = actions.get(actions.indexOf(action) + 1);
            update();
//            new ClubActionViewer(actions.getJSONObject(toList(actions).indexOf(action) + 1), actions).open(Minecraft.getMinecraft().thePlayer);
//            Multithreading.schedule(() -> {
//                Mouse.setCursorPosition((int) mouseX, (int) mouseY);
//            }, 1, TimeUnit.MILLISECONDS);
        });
    }
    public static ArrayList<Object> toList(JSONArray array) {
        ArrayList<Object> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            if (array.get(i) instanceof JSONArray) {
                list.add(toList(array.getJSONArray(i)));
                continue;
            }
            if (array.get(i) instanceof JSONObject) {
                list.add(array.getJSONObject(i));
                continue;
            }
            list.add(array.get(i));
        }
        return list;
    }

    public static ArrayList<Object> toList(JsonArray array) {
        ArrayList<Object> list = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) instanceof JsonArray) {
                list.add(toList(array.get(i).getAsJsonArray()));
                continue;
            }
            if (array.get(i) instanceof JsonObject) {
                list.add(array.get(i).getAsJsonObject());
                continue;
            }
            list.add(array.get(i));
        }
        return list;
    }

}
