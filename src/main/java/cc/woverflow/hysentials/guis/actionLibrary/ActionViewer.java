package cc.woverflow.hysentials.guis.actionLibrary;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.guis.container.Container;
import cc.woverflow.hysentials.guis.container.GuiItem;
import cc.woverflow.hysentials.util.Material;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.MouseEvent;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ActionViewer extends Container {
    JSONObject action;
    JSONArray actions;

    public ActionViewer(JSONObject action, JSONArray actions) {
        super("Viewing: " + action.getString("id"), 6);
        this.action = action;
        this.actions = actions;
    }

    @Override
    public void setItems() {
        JSONObject actionData = action.getJSONObject("action");
        JSONObject codespace = actionData.getJSONObject("codespace");
        setItem(13, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.PAPER, "&a" + actionData.getString("name"), codespace.getInt("functions"), 0,
                "&7Creator: &b" + actionData.getString("creator"),
                "&7Rating: &6" + ((action.getInt("rating") > 0 ? "+" + action.getInt("rating") : action.getInt("rating")) + "✭ &a(+" + action.getInt("ratingsPositive") + "▲) &c(-" + action.getInt("ratingsNegative") + "▼)"),
                "&7Codespace Required:",
                "&8 ▪ &a" + codespace.getInt("functions") + " Function Slots",
                "   &8+" + codespace.getInt("conditions") + " Conditionals",
                "   &8+" + codespace.getInt("actions") + " Total Actions",
                "",
                "&7Function Description:",
                "&f" + actionData.getString("description")
            )
        ));

        setItem(32, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.STAINED_CLAY, "&cDownvote", 1, 14,
                "&7Click to down vote this action.",
                "&7Rating: &6" + ((action.getInt("rating") > 0 ? "+" + action.getInt("rating") : action.getInt("rating")) + "✭ &a(+" + action.getInt("ratingsPositive") + "▲) &c(-" + action.getInt("ratingsNegative") + "▼)"),
                "",
                "&eLeft-Click to down vote."
            )
        ));

        setItem(30, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.STAINED_CLAY, "&aUpvote", 1, 5,
                "&7Click to up vote this action.",
                "&7Rating: &6" + ((action.getInt("rating") > 0 ? "+" + action.getInt("rating") : action.getInt("rating")) + "✭ &a(+" + action.getInt("ratingsPositive") + "▲) &c(-" + action.getInt("ratingsNegative") + "▼)"),
                "",
                "&eLeft-Click to up vote."
            )
        ));

        setItem(48, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.CHEST, "&aCopy Action ID", 1, 0,
                "&7Click to copy the action ID.",
                "",
                "&eLeft-Click to copy."
            )
        ));
        setItem(49, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.BARRIER, "&aGo Back", 1, 0,
                "&7To Action Library"
            )
        ));
        setItem(50, GuiItem.fromStack(
            GuiItem.makeColorfulItem(Material.NETHER_STAR, "&0&9&4&aBookmark Function", 1, 0,
                "&7Click to bookmark this action.",
                "",
                "&eLeft-Click to bookmark."
            )
        ));
        if (toList(actions).indexOf(action) != 0) {
            setItem(47, GuiItem.fromStack(
                GuiItem.makeColorfulItem(Material.ARROW, "&aPrevious Action", 1, 0,
                    "&7Click to view the previous action.",
                    "",
                    "&eLeft-Click to view."
                )
            ));
        }

        if (toList(actions).indexOf(action) != actions.length() - 1) {
            setItem(51, GuiItem.fromStack(
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

        setAction(30, (event) -> {
            event.getEvent().cancel();
            action.put("ratingsPositive", action.getInt("ratingsPositive") + 1);
            try (InputStreamReader input = new InputStreamReader(Hysentials.post("https://hysentials.redstone.llc/api/action?id=" + action.getString("id") + "&upvote=" + Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString(), new JSONObject()), StandardCharsets.UTF_8)) {
                String s = IOUtils.toString(input);
                JSONObject response = new JSONObject(s);
                if (response.getBoolean("success")) {
                    update();
                } else {
                    UChat.chat("&cYou have already upvoted this action.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        setAction(32, (event) -> {
            event.getEvent().cancel();
            action.put("ratingsNegative", action.getInt("ratingsNegative") + 1);
            try (InputStreamReader input = new InputStreamReader(Hysentials.post("https://hysentials.redstone.llc/api/action?id=" + action.getString("id") + "&downvote=" + Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString(), new JSONObject()), StandardCharsets.UTF_8)) {
                String s = IOUtils.toString(input);
                JSONObject response = new JSONObject(s);
                if (response.getBoolean("success")) {
                    update();
                } else {
                    UChat.chat("&cYou have already downvoted this action.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        setAction(48, (event) -> {
            event.getEvent().cancel();
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(action.getString("id")), null);
            UChat.chat("&aCopied action ID to clipboard.");
        });

        setAction(49, (event) -> {
            event.getEvent().cancel();
            Minecraft.getMinecraft().thePlayer.closeScreen();
            new ActionLibrary().open(Minecraft.getMinecraft().thePlayer);
        });

        setAction(50, (event -> {
            UChat.chat("&cThis feature is not yet implemented.");
        }));

        setAction(47, (event) -> {
            event.getEvent().cancel();
            Minecraft.getMinecraft().thePlayer.closeScreen();
            if (toList(actions).indexOf(action) == 0) return;
            float mouseX = Mouse.getX();
            float mouseY = Mouse.getY();
            new ActionViewer(actions.getJSONObject(toList(actions).indexOf(action) - 1), actions).open(Minecraft.getMinecraft().thePlayer);
            Multithreading.schedule(() -> {
                Mouse.setCursorPosition((int) mouseX, (int) mouseY);
            }, 50, TimeUnit.MILLISECONDS);
        });

        setAction(51, (event) -> {
            event.getEvent().cancel();
            if (toList(actions).indexOf(action) == actions.length() - 1) return;
            Minecraft.getMinecraft().thePlayer.closeScreen();
            float mouseX = Mouse.getX();
            float mouseY = Mouse.getY();
            new ActionViewer(actions.getJSONObject(toList(actions).indexOf(action) + 1), actions).open(Minecraft.getMinecraft().thePlayer);
            Multithreading.schedule(() -> {
                Mouse.setCursorPosition((int) mouseX, (int) mouseY);
            }, 1, TimeUnit.MILLISECONDS);
        });
    }

    public static List<Object> toList(JSONArray array) {
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
}
