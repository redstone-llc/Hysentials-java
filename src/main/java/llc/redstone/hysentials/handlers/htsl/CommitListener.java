package llc.redstone.hysentials.handlers.htsl;
//
//import llc.redstone.hysentials.event.events.GuiLoadedEvent;
//import llc.redstone.hysentials.event.events.GuiMouseClickEvent;
//import llc.redstone.hysentials.event.events.RenderItemInGuiEvent;
//import llc.redstone.hysentials.htsl.Loader;
//import llc.redstone.hysentials.htsl.compiler.ConditionCompiler;
//import llc.redstone.hysentials.util.C;
//import llc.redstone.hysentials.util.DuoVariable;
//import llc.redstone.hysentials.util.TriVariable;
//import llc.redstone.hysentials.utils.Conditional;
//import llc.redstone.hysentials.utils.StringUtilsKt;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.GuiScreen;
//import net.minecraft.client.gui.inventory.GuiChest;
//import net.minecraft.inventory.Slot;
//import net.minecraft.item.ItemStack;
//import net.minecraftforge.client.event.GuiScreenEvent;
//import net.minecraftforge.client.event.RenderGameOverlayEvent;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//import org.apache.commons.io.FileUtils;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static llc.redstone.hysentials.handlers.htsl.Exporter.getSlots;
//import static llc.redstone.hysentials.htsl.compiler.Compiler.getArgs;
//
public class CommitListener {
//    public static ItemStack lastItem = null;
//    public static int lastSlot = -1;
//    public static int currentPage = 1;
//
//    public static int lastConditionalSlot = -1;
//    public static ItemStack lastConditionalItem = null;
//    public static String currentName = null;
//    public static String lastContainer = null;
//    public static Conditional currentConditional = null;
//    public static HashMap<String, String> fileCode = new HashMap<>();
//    private boolean manualItemClick;
//
//    @SubscribeEvent
//    public void onGuiSlotClick(GuiMouseClickEvent event) {
//        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.openContainer == null)
//            return;
//        if (!Queue.queue.isEmpty()) return;
//        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
//        if (screen == null) return;
//        if (screen instanceof GuiChest) {
//            Slot slot = ((GuiChest) screen).getSlotUnderMouse();
//            if (event.getSlot() != null) {
//                slot = ((GuiChest) screen).inventorySlots.getSlot(event.getSlot());
//            }
//            if (slot == null) return;
//
//            if (slot.getHasStack() && slot.getStack().hasDisplayName()) {
//                if (FunctionsGUIHandler.isInFunctionsGui()) {
//                    lastSlot = slot.getSlotIndex();
//                    lastItem = slot.getStack();
//                    lastContainer = "Functions";
//                    currentName = null;
//                    try {
//                        lookupFile(C.removeColor(lastItem.getDisplayName()));
//                    } catch (IOException ignored) {
//                    }
//                } else if (ActionGUIHandler.isInActionGui()) {
//                    lastSlot = slot.getSlotIndex();
//                    lastItem = slot.getStack();
//                    lastContainer = "Actions";
//                } else if (ActionGUIHandler.isActionSettings()) {
////                    lastSlot = slot.getSlotIndex();
////                    lastItem = slot.getStack();
//                    lastContainer = "Action Settings > " + lastContainer;
//                } else if (ActionGUIHandler.isEditConditions()) {
//                    lastConditionalSlot = slot.getSlotIndex();
//                    lastConditionalItem = slot.getStack();
//                    lastContainer = "Edit Conditions";
//                } else if (ActionGUIHandler.isSettingsConditions()) {
//                    lastContainer = "Settings Conditions > " + lastContainer;
//
//                }
//            }
//        }
//    }
//
//    // WHY DO I CODE LIKE THIS LMFAO
//    // name, page, List of actions
//    HashMap<String, HashMap<Integer, List<String>>> loadedItems = new HashMap<>();
//    // name of file, page, slot, conditional
//    HashMap<String, HashMap<Integer, HashMap<Integer, Conditional>>> loadedConditionals = new HashMap<>();
//
//    @SubscribeEvent
//    public void onGuiLoaded(GuiLoadedEvent event) {
//        if (!(event.gui instanceof GuiChest) || lastItem == null || lastContainer == null || currentName == null) {
//            return;
//        }
//        System.out.println("Loaded gui");
//        System.out.println(
//            "lastItem = " + lastItem.getDisplayName() + ", " +
//                "lastSlot = " + lastSlot + ", " +
//                "lastContainer = " + lastContainer + ", " +
//                "currentName = " + currentName
//        );
//        HashMap<Integer, List<String>> loadedItems = this.loadedItems.get(currentName);
//        if (ActionGUIHandler.isInActionGui()) {
//            if (lastContainer.equals("Functions") || lastContainer.equals("Actions")) {
//                // name, i, page
//                getSlots(false, 2).forEach(slot -> {
//                    loadedItems.computeIfAbsent(slot.getThird(), k -> new ArrayList<>());
//                    Loader.loaders.stream().filter(loader -> loader.name.equals(slot.getFirst())).findFirst().ifPresent(loader -> {
//                        if (loader.name.equals("Conditional")) {
//                            loadedConditionals.computeIfAbsent(currentName, k -> new HashMap<>());
//                            loadedConditionals.get(currentName).computeIfAbsent(slot.getThird(), k -> new HashMap<>());
//                            loadedConditionals.get(currentName).get(slot.getThird()).put(slot.getSecond(), new Conditional(
//                                new ArrayList<>(),
//                                new ArrayList<>(),
//                                new ArrayList<>(),
//                                false,
//                                slot.getSecond(), // index
//                                slot.getThird() // page
//                            ));
//                        }
//                        if (loadedItems.get(slot.getThird()).size() <= slotToIndex(slot.getSecond()))
//                            loadedItems.get(slot.getThird()).add(loader.keyword);
//                    });
//                    currentPage = slot.getThird();
//                });
//            }
//            return;
//        }
//
//        if (ActionGUIHandler.isActionSettings()) {
//            if (lastContainer.startsWith("Action Settings")) {
//                lastContainer = lastContainer.replace("Action Settings > ", "");
//            }
//            if (slotToIndex(lastSlot) == -1) return;
//            List<String> items = loadedItems.get(currentPage);
//            String name = items.get(slotToIndex(lastSlot)).split(" ")[0];
//            if (name != null && !name.equals("condition")) {
//                Loader loader = Loader.loaders.stream().filter(l -> l.keyword.equals(name)).findFirst().orElse(null);
//                if (loader != null) {
//                    List<String> args = getSlots(true, 2).stream()
//                        .map(TriVariable::getFirst).collect(Collectors.toList());
//                    if (args.isEmpty()) return;
//                    if (args.stream().anyMatch(s -> s.equals("Item"))) {
//                        System.out.println("Item was detected, but not yet possible to export");
//                        return;
//                    }
//                    items.set(slotToIndex(lastSlot), loader.export(args));
//                }
//            }
//
//            if (name != null && name.equals("condition")) {
//                Conditional con = loadedConditionals.get(currentName).get(currentPage).get(lastSlot);
//                List<String> args = getSlots(true, 5).stream()
//                    .map(TriVariable::getFirst).collect(Collectors.toList());
//                if (args.isEmpty()) return;
//                if (args.size() < 4) return;
//                con.setAny(Boolean.parseBoolean(args.get(1)));
//            }
//            loadedItems.put(currentPage, items);
//            return;
//        }
//
//        if (ActionGUIHandler.isEditConditions()) {
//            if (slotToIndex(lastSlot) == -1) return;
//            List<String> items = loadedItems.get(currentPage);
//            String name = items.get(slotToIndex(lastSlot)).split(" ")[0];
//            if (name != null && name.equals("condition")) {
//                currentConditional = loadedConditionals.get(currentName).get(currentPage).get(lastSlot);
//                List<String> args = getSlots(false, 2).stream()
//                    .map(TriVariable::getFirst).collect(Collectors.toList());
//                if (args.isEmpty()) return;
//                for (String arg : args) {
//                    currentConditional.getConditions().add(ConditionCompiler.export(arg, new ArrayList<>(), false));
//                }
//
//                // Assemble if string
//                String s = "if " + (currentConditional.getAny() ? "or (" : "(") + String.join(", ", currentConditional.getConditions()) + ") {";
//                items.set(slotToIndex(lastSlot), s);
//            }
//            loadedItems.put(currentPage, items);
//            return;
//        }
//
//        if (ActionGUIHandler.isSettingsConditions()) {
//            if (lastContainer.startsWith("Settings Conditions")) {
//                lastContainer = lastContainer.replace("Settings Conditions > ", "");
//            }
//
//            if (lastContainer.equals("Edit Conditions")) {
//                List<String> args = getSlots(true, 2).stream()
//                    .map(TriVariable::getFirst).collect(Collectors.toList());
//                if (args.isEmpty()) return;
//                List<String> conditions = currentConditional.getConditions();
//                String export = ConditionCompiler.export(C.removeColor(lastConditionalItem.getDisplayName()), args, true);
//                if (!export.isEmpty()) {
//                    if (conditions.size() <= slotToIndex(lastConditionalSlot))
//                        conditions.add(export);
//                    else
//                        conditions.set(slotToIndex(lastConditionalSlot), export);
//                }
//
//                // Assemble if string
//                String s = "if " + (currentConditional.getAny() ? "or (" : "(") + String.join(", ", currentConditional.getConditions()) + ") {";
//                List<String> items = loadedItems.get(currentPage);
//                items.set(slotToIndex(lastSlot), s);
//                loadedItems.put(currentPage, items);
//            }
//            return;
//        }
//
//        if (ActionGUIHandler.isInActionGui() && lastContainer.startsWith("Action Settings")) {
//
//        }
//
//        currentName = null;
//    }
//
//    //Can be between 10-16, 19-25, 28-34, 37-43
//    private int slotToIndex(int slot) {
//        if (slot >= 10 && slot <= 16) {
//            return slot - 10;
//        } else if (slot >= 19 && slot <= 25) {
//            return slot - 19;
//        } else if (slot >= 28 && slot <= 34) {
//            return slot - 28;
//        } else if (slot >= 37 && slot <= 43) {
//            return slot - 37;
//        }
//        return -1;
//    }
//
//    @SubscribeEvent
//    public void onOverlayDraw(GuiScreenEvent.BackgroundDrawnEvent event) {
//        if (currentName == null || !fileCode.containsKey(currentName)) return;
//        String[] lines = fileCode.get(currentName).split("\n");
//        List<String> loadedItems = this.loadedItems.get(currentName).get(currentPage);
//        if (loadedItems == null) return;
//        int y = 50;
//        for (int i = 0, linesLength = lines.length; i < linesLength; i++) {
//            String line = lines[i].trim();
//            if (line.startsWith("//")) continue;
//            String itemLine = null;
//            if (loadedItems.size() > i) {
//                itemLine = loadedItems.get(i);
//            }
//            // Unfound = white
//            // Found = green
//            // Changed = red
//            if (itemLine != null) {
//                List<String> split = getArgs(line, true);
//                List<String> split2 = getArgs(itemLine, true);
//                StringBuilder builder = new StringBuilder();
//                if (!split.get(0).equals(split2.get(0))) {
//                    Minecraft.getMinecraft().fontRendererObj.drawString("§f" + line, 50, y, 0xFFFFFF);
//                    y += 10;
//                    continue;
//                }
//                for (int j = 0; j < split.size(); j++) {
//                    String s = split.get(j);
//                    if (split2.size() > j) {
//                        if (!split2.get(j).equals(s)) {
//                            builder.append("§c").append(split2.get(j)).append(" ");
//                        } else {
//                            builder.append("§a").append(s).append(" ");
//                        }
//                    } else {
//                        builder.append("§f").append(s).append(" ");
//                    }
//                }
//                Minecraft.getMinecraft().fontRendererObj.drawString(builder.toString(), 50, y, 0xFFFFFF);
//            } else {
//                Minecraft.getMinecraft().fontRendererObj.drawString("§f" + line, 50, y, 0xFFFFFF);
//            }
//            y += 10;
//        }
//    }
//
//    private void lookupFile(String name) throws IOException {
//        File file = new File(Minecraft.getMinecraft().mcDataDir, "config/hysentials");
//        if (!file.exists()) file.mkdir();
//        file = new File(file, "htsl");
//        if (!file.exists()) file.mkdir();
//        file = new File(file, name + ".htsl");
//        if (file.exists()) {
//            currentName = name;
//            if (!loadedItems.containsKey(name)) loadedItems.put(name, new HashMap<>());
//            fileCode.put(name, FileUtils.readFileToString(file));
//        }
//    }
}
