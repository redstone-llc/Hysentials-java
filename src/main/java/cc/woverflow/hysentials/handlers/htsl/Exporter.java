package cc.woverflow.hysentials.handlers.htsl;

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.polyfrost.oneconfig.utils.StringUtils;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.guis.club.ClubDashboard;
import cc.woverflow.hysentials.guis.container.GuiItem;
import cc.woverflow.hysentials.handlers.redworks.BwRanks;
import cc.woverflow.hysentials.handlers.redworks.BwRanksUtils;
import cc.woverflow.hysentials.htsl.Loader;
import cc.woverflow.hysentials.htsl.compiler.ConditionCompiler;
import cc.woverflow.hysentials.util.DuoVariable;
import cc.woverflow.hysentials.util.TriVariable;
import kotlinx.coroutines.AwaitKt;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.GameData;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.lwjgl.Sys;
import scala.Dynamic;
import scala.Int;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import tv.twitch.chat.Chat;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static cc.woverflow.hysentials.guis.container.GuiItem.getLore;
import static cc.woverflow.hysentials.handlers.htsl.Queue.greaterThan;
import static cc.woverflow.hysentials.htsl.compiler.ConditionCompiler.undoValidComparator;

public class Exporter {
    public static String name;
    public static String export;
    public static String type;
    public static List<TriVariable<String, Integer, Integer>> actions = new ArrayList<>();
    public static List<TriVariable<String, Integer, Integer>> ifActions = new ArrayList<>();
    public static List<TriVariable<String, Integer, Integer>> elseActions = new ArrayList<>();
    public static List<TriVariable<String, Integer, Integer>> conditions = new ArrayList<>();
    public static List<TriVariable<String, Integer, Integer>> randomActions = new ArrayList<>();
    public static List<String> conditionsExported = new ArrayList<>();
    public static List<String> ifExported = new ArrayList<>();
    public static List<String> elseExported = new ArrayList<>();
    public static List<String> randomActionsExported = new ArrayList<>();

    public static List<String> conditionsExportedTotal = new ArrayList<>();
    public static List<String> ifExportedTotal = new ArrayList<>();
    public static List<String> elseExportedTotal = new ArrayList<>();
    public static List<String> randomActionsExportedTotal = new ArrayList<>();
    public static List<String> actionsExportedTotal = new ArrayList<>();
    public static int stage = 0;
    public static TriVariable<String, Integer, Integer> currentAction = null;
    public static TriVariable<String, Integer, Integer> condition = null;
    public static TriVariable<String, Integer, Integer> ifAction = null;
    public static TriVariable<String, Integer, Integer> elseAction = null;
    public static TriVariable<String, Integer, Integer> randomAction = null;
    public static boolean orEnabled = false;

    public static int timeWithoutOperation = 0;
    public static List<String> fails = new ArrayList<>();

    public static String code = "";

    @SubscribeEvent()
    public void tick(TickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (event.type != TickEvent.Type.CLIENT) return;
        if (name == null || export == null) return;
        if (Queue.queue.size() != 0) return;
        timeWithoutOperation++;
        if ((greaterThan(timeWithoutOperation, HysentialsConfig.guiTimeout)) && !HysentialsConfig.htslSafeMode) {
            fails.add("&cOperation timed out. &f(too long without GUI click)");
            finish();
            return;
        }
        if (!Navigator.isReady) return;
        timeWithoutOperation = 0;
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.openContainer == null)
            return;
        if (Navigator.isReturning) {
            Navigator.returnToEditActions();
            return;
        }
        if (Navigator.isGoingPageOne) {
            boolean success = Navigator.pageOne();
            return;
        }
        if (Navigator.isSelecting) {
            if (Navigator.optionBeingSelected.split(":").length > 1) {
                boolean success = Navigator.selectOption(Integer.parseInt(Navigator.optionBeingSelected.split(":")[0]), Integer.parseInt(Navigator.optionBeingSelected.split(":")[1]));
            } else {
                boolean success = Navigator.selectOption(Navigator.optionBeingSelected);
            }
            return;
        }
        Container container = Minecraft.getMinecraft().thePlayer.openContainer;
        if (container.inventorySlots.size() == 0) return;
        String containerName = Navigator.getContainerName();
        if (containerName == null) return;
        if (stage == 0) {
            actions.addAll(getSlots(false, 2));
            actionsExportedTotal.addAll(getSlots(false, 2).stream().map(TriVariable::getFirst).collect(Collectors.toList()));

            ItemStack item = container.getSlot(53).getStack();
            if (item != null && GameData.getItemRegistry().getId(item.getItem()) == 262) {
                Navigator.click(53);
                return;
            } else {
                stage = 1;
                Navigator.pageOne();
            }
            return;
        }
        if (stage == 1) {
            if (actions.size() == 0) {
                finish();
                return;
            }

            currentAction = actions.get(0);
            if (!Loader.canExport(Loader.loaders.stream().filter(l -> l.name.equals(currentAction.getFirst())).findFirst().orElse(null))) {
                Navigator.setSelecting(currentAction.getSecond(), currentAction.getThird());
                stage = 2;
            } else {
                code += Loader.loaders.stream().filter(l -> l.name.equals(currentAction.getFirst())).findFirst().get().export(new ArrayList<>()) + "\n";
                currentAction = null;
            }
            actions.remove(0);
            return;
        }
        if (currentAction != null && currentAction.getFirst().equals("Random Action")) {
            if (stage == 2) {
                TriVariable<String, Integer, Integer> button = getSlots(false, 0).get(0);
                if (button.getFirst().equals("Actions") && button.getSecond() == 10) {
                    Navigator.click(10);
                    stage = 3;
                    return;
                }
                return;
            }
            if (stage == 3) {
                randomActions.addAll(getSlots(false, 0));
                if (randomActions.size() == 0) {
                    stage = 1;
                }
                ItemStack item = container.getSlot(53).getStack();
                if (item != null && GameData.getItemRegistry().getId(item.getItem()) == 262) {
                    Navigator.click(53);
                } else {
                    stage = 4;
                }
                return;
            }
            if (stage == 4) {
                if (randomActions.size() == 0) {
                    Navigator.goBack();
                    stage = 50;
                    return;
                }
                randomAction = randomActions.get(0);
                if (!Loader.canExport(Loader.loaders.stream().filter(l -> l.name.equals(randomAction.getFirst())).findFirst().orElse(null))) {
                    Navigator.setSelecting(randomAction.getSecond(), randomAction.getThird());
                    stage = 5;
                } else {
                    randomActionsExported.add(Loader.loaders.stream().filter(l -> l.name.equals(randomAction.getFirst())).findFirst().get().export(new ArrayList<>()));
                    randomAction = null;
                }
                randomActions.remove(0);
                return;
            }
            if (stage == 50) {
                Navigator.goBack();
                stage = 1;
                code += "random {\n" + String.join("\n", randomActionsExported) + "\n}\n";

                randomActionsExported.clear();
                randomActions.clear();
                randomAction = null;
                return;
            }
            if (stage == 5) {
                if (randomAction != null) {
                    List<String> args = getSlots(true, 2).stream()
                        .map(TriVariable::getFirst).collect(Collectors.toList());
                    if (args.size() == 0) return;
                    Loader.loaders.stream().filter(l -> l.name.equals(randomAction.getFirst())).findFirst()
                        .ifPresent(loader -> randomActionsExported.add(loader.export(args)));
                    randomAction = null;
                    stage = 4;
                    Navigator.goBack();
                    return;
                }
            }
            return;
        }
        if (currentAction != null && currentAction.getFirst().equals("Conditional")) {
            if (stage == 2) {
                if (getSlots(true, 5).size() == 0) return;
                orEnabled = Boolean.parseBoolean(undoValidOperator(getSlots(true, 5).get(0).getFirst()));
                try {
                    TriVariable<String, Integer, Integer> button = getSlots(false, 0).get(0);
                    if (button.getFirst().equals("Conditions") && button.getSecond() == 10) {
                        Navigator.click(10);
                        stage = 3;
                        return;
                    }
                } catch (Exception e) {
                }
                return;
            }
            if (stage == 3) {
                conditions.addAll(getSlots(false, 0));
                if (conditions.size() == 0) {
                    stage = 1;
                }
                ItemStack item = container.getSlot(53).getStack();
                if (item != null && GameData.getItemRegistry().getId(item.getItem()) == 262) {
                    Navigator.click(53);
                } else {
                    stage = 4;
                }
                return;
            }
            if (stage == 4) {
                if (conditions.size() == 0) {
                    condition = null;
                    Navigator.goBack();
                    stage = 6;
                    return;
                }
                condition = conditions.get(0);
                try {
                    String s = ConditionCompiler.export(condition.getFirst(), new ArrayList<>());
                    conditionsExported.add(s);
                    condition = null;
                } catch (Exception e) {
                    Navigator.setSelecting(condition.getSecond(), condition.getThird());
                    stage = 5;
                }
                conditions.remove(0);
                return;
            }
            if (stage == 5) {
                if (condition != null) {
                    try {
                        List<String> args = getSlots(true, 2).stream()
                            .map(TriVariable::getFirst).collect(Collectors.toList());
                        if (args.size() == 0) return;
                        conditionsExported.add(ConditionCompiler.export(condition.getFirst(), args));
                        condition = null;
                        Navigator.goBack();
                        stage = 4;
                    } catch (Exception e) {
                    }
                }
                return;
            }
            if (stage == 6) {
                if (getSlots(false, 0).size() < 3) return;
                TriVariable<String, Integer, Integer> button = getSlots(false, 0).get(2);
                if (button.getFirst().equals("If Actions") && button.getSecond() == 12) {
                    Navigator.click(12);
                    stage = 7;
                    return;
                } else {
                    stage = 1;
                }
                return;
            }
            if (stage == 7) {
                ifActions.addAll(getSlots(false, 0));
                if (ifActions.size() == 0) {
                    stage = 1;
                }
                ItemStack item = container.getSlot(53).getStack();
                if (item != null && GameData.getItemRegistry().getId(item.getItem()) == 262) {
                    Navigator.click(53);
                } else {
                    stage = 8;
                }
                return;
            }
            if (stage == 8) {
                if (ifActions.size() == 0) {
                    ifAction = null;
                    Navigator.goBack();
                    stage = 10;
                    return;
                }
                ifAction = ifActions.get(0);
                if (!Loader.canExport(Loader.loaders.stream().filter(l -> l.name.equals(ifAction.getFirst())).findFirst().orElse(null))) {
                    Navigator.setSelecting(ifAction.getSecond(), ifAction.getThird());
                    stage = 9;
                } else {
                    ifExported.add(Loader.loaders.stream().filter(l -> l.name.equals(ifAction.getFirst())).findFirst().get().export(new ArrayList<>()));
                    ifAction = null;
                }
                ifActions.remove(0);
                return;
            }

            if (stage == 9) {
                if (ifAction != null) {
                    List<String> args = getSlots(true, 2).stream()
                        .map(TriVariable::getFirst).collect(Collectors.toList());
                    if (args.size() == 0) return;
                    Loader.loaders.stream().filter(l -> l.name.equals(ifAction.getFirst())).findFirst()
                        .ifPresent(loader -> ifExported.add(loader.export(args)));
                    ifAction = null;
                    stage = 8;
                    Navigator.goBack();
                    return;
                }
            }

            if (stage == 10) {
                if (getSlots(false, 0).size() < 4) return;
                TriVariable<String, Integer, Integer> button = getSlots(false, 0).get(3);
                if (button.getFirst().equals("Else Actions") && button.getSecond() == 13) {
                    Navigator.click(13);
                    stage = 11;
                    return;
                }
            }
            if (stage == 11) {
                elseActions.addAll(getSlots(false, 0));
                ItemStack item = container.getSlot(53).getStack();
                if (item != null && GameData.getItemRegistry().getId(item.getItem()) == 262) {
                    Navigator.click(53);
                } else {
                    stage = 12;
                }
                return;
            }
            if (stage == 12) {
                if (elseActions.size() == 0) {
                    elseAction = null;
                    Navigator.goBack();
                    stage = 14;
                    return;
                }
                elseAction = elseActions.get(0);
                if (!Loader.canExport(Loader.loaders.stream().filter(l -> l.name.equals(elseAction.getFirst())).findFirst().orElse(null))) {
                    Navigator.setSelecting(elseAction.getSecond(), elseAction.getThird());
                    stage = 13;
                } else {
                    elseExported.add(Loader.loaders.stream().filter(l -> l.name.equals(elseAction.getFirst())).findFirst().get().export(new ArrayList<>()));
                    elseAction = null;
                }
                elseActions.remove(0);
                return;
            }
            if (stage == 13) {
                if (elseAction != null) {
                    List<String> args = getSlots(true, 2).stream()
                        .map(TriVariable::getFirst).collect(Collectors.toList());
                    if (args.size() == 0) return;
                    Loader.loaders.stream().filter(l -> l.name.equals(elseAction.getFirst())).findFirst()
                        .ifPresent(loader -> elseExported.add(loader.export(args)));
                    elseAction = null;
                    stage = 12;
                    Navigator.goBack();
                    return;
                }
            }
            if (stage == 14) {
                if (elseExported.size() == 0) {
                    code += "if " + (orEnabled ? "or (" : "(") + String.join(", ", conditionsExported) + ") {\n" + String.join("\n", ifExported) + "\n}\n";
                } else {
                    code += "if " + (orEnabled ? "or (" : "(") + String.join(", ", conditionsExported) + ") {\n" + String.join("\n", ifExported) + "} else {\n" + String.join("\n", elseExported) + "\n}\n";
                }
                conditionsExportedTotal.add("Conditional");


                conditionsExported.clear();
                ifExported.clear();
                elseExported.clear();
                orEnabled = false;
                Navigator.goBack();
                stage = 1;
            }
            return;
        }
        if (stage == 2) {
            List<String> args = getSlots(true, 2).stream()
                .map(TriVariable::getFirst).collect(Collectors.toList());
            if (args.size() == 0) return;
            Loader.loaders.stream().filter(l -> l.name.equals(currentAction.getFirst())).findFirst()
                .ifPresent(loader -> code += loader.export(args) + "\n");
            currentAction = null;
            Navigator.goBack();
            stage = 1;
            return;
        }
    }

    private void finish() {
        if (fails.size() > 0) {
            UChat.chat("&cFailed to load: &f(" + fails.size() + " error" + (fails.size() > 1 ? "s" : "") + ")");
            for (String fail : fails) {
                UChat.chat("   > " + fail);
            }
            fails.clear();
            UChat.chat("&f" + actions.size() + " &coperation" + (actions.size() != 1 ? "s" : "") + " left in queue.");
        } else {
            String id = BwRanks.randomString(15);
            JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("code", code);
            json.put("type", type);
            json.put("creator", Minecraft.getMinecraft().thePlayer.getName());
            json.put("description", "Exported from Hysentials");
            JSONObject codespace = new JSONObject();
            codespace.put("functions", 1);
            codespace.put("conditions", conditionsExportedTotal.size());
            codespace.put("actions", actionsExportedTotal.size() + ifExportedTotal.size() + elseExportedTotal.size() + randomActionsExportedTotal.size());
            json.put("codespace", codespace);
            if (export.equals("library")) {
                try (InputStreamReader input = new InputStreamReader(Hysentials.post("https://hysentials.redstone.llc/api/action?id=" + id, json), StandardCharsets.UTF_8)) {
                    String s = IOUtils.toString(input);
                    JSONObject object = new JSONObject(s);
                    if (object.getBoolean("success")) {
                        UChat.chat("&3[HTSL] &fExported successfully to &bAction Library!");
                        new UTextComponent("&3[HTSL] &fClick here to edit your action.").setClick(ClickEvent.Action.OPEN_URL, "https://redstone.llc/actions/manage/" + id).chat();
                    } else {
                        UChat.chat("&cFailed to export!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (export.equals("clipboard")) {
                StringSelection selection = new StringSelection(code);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
                UChat.chat("&3[HTSL] &fCopied to clipboard!");
            } else if (export.equals("file")) {
                try {
                    File file = new File(Minecraft.getMinecraft().mcDataDir, "hysentials");
                    if (!file.exists()) file.mkdir();
                    file = new File(file, "exported");
                    if (!file.exists()) file.mkdir();
                    file = new File(file, name + ".htsl");
                    if (!file.exists()) file.createNewFile();
                    FileWriter writer = new FileWriter(file);
                    writer.write(json.toString());
                    writer.close();
                    UChat.chat("&3[HTSL] &fExported to file &b" + file.getAbsolutePath() + "&f!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (export.equals("club")) {
                JSONObject object = new JSONObject();
                object.put("actions", json);
                Multithreading.runAsync(() -> {
                    ClubDashboard.clubData = ClubDashboard.getClub();
                    ClubDashboard.update(object);
                    UChat.chat("&3[HTSL] &fExported to &bClub Dashboard&f!");
                });
            }
        }

        System.out.println(code);
        for (Field field : this.getClass().getFields()) {
            try {
                if (field.get(this) instanceof List)
                    ((List) field.get(this)).clear();
                else if (field.get(this) instanceof Map)
                    ((Map) field.get(this)).clear();
                else if (field.get(this) instanceof Integer)
                    field.set(this, 0);
                else if (field.get(this) instanceof Boolean)
                    field.set(this, false);
                else
                    field.set(this, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        code = "";
    }

    @SubscribeEvent
    public void onOverlayDraw (RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        if (name == null || export == null) return;
        String[] lines = code.split("\n");
        int y = 50;
        for (String line : lines) {
            Minecraft.getMinecraft().fontRendererObj.drawString(line, 50, y, 0xFFFFFF);
            y += 10;
        }
        Minecraft.getMinecraft().fontRendererObj.drawString("Tick: " + timeWithoutOperation, 50, y + 20, 0xFFFFFF);
        Minecraft.getMinecraft().fontRendererObj.drawString("Stage: " + stage, 50, y + 30, 0xFFFFFF);
        Minecraft.getMinecraft().fontRendererObj.drawString("Action: " + (currentAction == null ? "" : currentAction.getFirst()), 50, y + 40, 0xFFFFFF);
    }

    public String undoValidOperator(String operator) {
        switch (operator.toLowerCase()) {
            case "enabled":
                operator = "true";
                break;
            case "disabled":
                operator = "false";
                break;
            case "increment":
                operator = "inc";
                break;
            case "decrement":
                operator = "dec";
                break;
            case "set":
                operator = "=";
                break;
            case "multiply":
                operator = "multi";
                break;
            case "divide":
                operator = "div";
                break;
        }
        if (!Arrays.asList("inc", "dec", "=", "multi", "div").contains(operator))
            return operator;
        return operator;
    }

    public List<TriVariable<String, Integer, Integer>> getSlots(boolean getLore, int loreLine) {
        Container container = Minecraft.getMinecraft().thePlayer.openContainer;
        List<TriVariable<String, Integer, Integer>> slots = new ArrayList<>();
        int page = 1;
        if (Navigator.getContainerName().split("/").length > 1) {
            page = Integer.parseInt(Navigator.getContainerName().split("/")[0].replace("(", ""));
        }
        for (int i = 0; i < container.inventorySlots.size() - 36 - 9; i++) {
            if (container.inventorySlots.get(i).getHasStack()) {
                ItemStack stack = container.inventorySlots.get(i).getStack();
                if (GameData.getItemRegistry().getId(stack.getItem()) == 7) continue;
                String name = ChatColor.Companion.stripControlCodes(stack.getDisplayName());
                List<String> lore = getLore(stack);
                if (getLore) {
                    if (lore.size() <= loreLine) continue;
                    String line = ChatColor.Companion.stripControlCodes(lore.get(loreLine));
                    line = undoValidOperator(undoValidComparator(line));
                    slots.add(new TriVariable<>(line, i, page));
                } else {
                    slots.add(new TriVariable<>(name, i, page));
                }
            }
        }
        return slots;
    }
}
