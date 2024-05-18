package llc.redstone.hysentials.handlers.htsl;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import llc.redstone.hysentials.config.hysentialmods.HousingConfig;
import llc.redstone.hysentials.event.events.GuiMouseClickEvent;
import llc.redstone.hysentials.guis.ResolutionUtil;
import llc.redstone.hysentials.htsl.LoaderObject;
import llc.redstone.hysentials.util.Input;
import llc.redstone.hysentials.utils.ChatLib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static llc.redstone.hysentials.handlers.htsl.ActionGUIHandler.*;

public class Queue {
    public static List<LoaderObject> queue = new ArrayList<>();
    List<String> fails = new ArrayList<>();
    public static int timeWithoutOperation = 0;
    long startedTime = 0;
    long totalTime = 0;
    String currentGuiContext = null;

    Input.Button timeRemainingButton;
    Input.Button cancel;
    LoaderObject current;

    public Queue() {
        timeRemainingButton = new Input.Button(0, 0, 0, 20, "Time Remaining: ");
        cancel = new Input.Button(0, 0, 0, 20, "Cancel Import");
    }

    @SubscribeEvent
    public void guiRender(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.openContainer == null)
            return;
        if (!(Minecraft.getMinecraft().currentScreen instanceof GuiContainer)) return;
        if (queue.size() == 0) return;
        GlStateManager.pushMatrix();
        int chestGuiTop;
        int chestWidth;
        int chestGuiLeft;
        try {
            chestGuiTop = (int) guiTopField.get(Minecraft.getMinecraft().currentScreen);
            chestGuiLeft = (int) guiLeftField.get(Minecraft.getMinecraft().currentScreen);
            chestWidth = (int) xSizeField.get(Minecraft.getMinecraft().currentScreen);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        int right = chestGuiLeft + chestWidth;
        cancel.setWidth(80);
        cancel.xPosition = right + 10;
        cancel.yPosition = chestGuiTop + 50;

        timeRemainingButton.setWidth(180);
        timeRemainingButton.xPosition = ResolutionUtil.current().getScaledWidth() / 2 - timeRemainingButton.width / 2;
        timeRemainingButton.yPosition = timeRemainingButton.height * 3;

        timeRemainingButton.drawButton(Minecraft.getMinecraft(), event.getMouseX(), event.getMouseY());
        cancel.drawButton(Minecraft.getMinecraft(), event.getMouseX(), event.getMouseY());
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void mouseClick(GuiMouseClickEvent event) {
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.openContainer == null)
            return;
        if (queue.size() == 0) return;
        if (cancel.mousePressed(Minecraft.getMinecraft(), event.getX(), event.getY())) {
            fails.add("&cOperation cancelled.");
            doneLoading();
        }
    }

    public static boolean greaterThan(int i, int j) {
        return Math.max(i, j) == i;
    }

    int ticks = 0;
    boolean started = false;

    @SubscribeEvent
    public void tick(TickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (event.type != TickEvent.Type.CLIENT) return;
        if (queue.size() != 0) timeWithoutOperation++;
        if ((greaterThan(timeWithoutOperation, HousingConfig.guiTimeout)) && queue.size() > 0 && !HousingConfig.htslSafeMode && Navigator.guiToOpen == null) {
            fails.add("&cOperation timed out. &f(too long without GUI click)");
            doneLoading();
        }
        if (queue.size() != 0 && !started) {
            started = true;
            Navigator.isReady = true;
        }
        if (!Navigator.isReady) return;
        if (queue.size() == 0) return;
        timeWithoutOperation = 0;
        if (Navigator.isReturning) {
            Navigator.returnToEditActions();
            return;
        }

        if (Navigator.isSelecting) {
            if (Navigator.optionBeingSelected2 != null) {
                Navigator.selectOptionOrClick(Navigator.optionBeingSelected2, Navigator.optionBeingSelectedSlot);
                return;
            }
            boolean success = Navigator.selectOption(Navigator.optionBeingSelected);
            if (!success) {
                fails.add("&cCouldn't find option &f" + Navigator.optionBeingSelected + "&c in &f" + currentGuiContext + "&c!");
            }
            return;
        }


        if (startedTime == 0) {
            startedTime = System.currentTimeMillis();
        }
        totalTime++;
        timeRemainingButton.displayString = ("Time Remaining: " + Math.round(((System.currentTimeMillis() - startedTime) / totalTime) * queue.size() / 1000) + " seconds");

        current = queue.get(0);
        if (current == null) return;
        queue.remove(0);

        if (current.getType().equals("setGuiContext")) {
            currentGuiContext = String.valueOf(current.getValue());
            if (queue.size() == 0) return;
            current = queue.get(0);
            queue.remove(0);
        }
        System.out.println("current: " + current.getType() + " " + current.getValue());
        switch (current.getType()) {
            case "click":
                System.out.println("clicking " + current.getValue());
                Navigator.click((Integer) current.getValue());
                break;
            case "anvil":
                System.out.println("inputting anvil");
                Navigator.inputAnvil(String.valueOf(current.getValue()));
                break;
            case "returnToEditActions":
                Navigator.returnToEditActions();
                break;
            case "back":
                Navigator.goBack();
                break;
            case "option":
                Navigator.setSelecting(String.valueOf(current.getValue()));
                break;
            case "chat":
                Navigator.inputChat(String.valueOf(current.getValue()), current.getFunc(), current.getCommand() != null ? (Boolean) current.getCommand() : false);
                break;
            case "item":
                Navigator.selectItem(String.valueOf(current.getValue()));
                break;
            case "command":
                System.out.println("commanding " + current.getValue());
                Navigator.command(String.valueOf(current.getValue()));
                break;
            case "export":
                List<ItemStack> items = Minecraft.getMinecraft().thePlayer.openContainer.inventorySlots.stream().map(Slot::getStack).collect(Collectors.toList());
                items = items.subList(0, items.size() - 36 - 9);
                current.func(items);
                break;
            case "done":
                doneLoading();
                current.func();
                break;
            case "doneExport": {
                timeWithoutOperation = 0;
                Navigator.isWorking = false;
                queue = new ArrayList<>();
                startedTime = 0;
                totalTime = 0;
                current.func();
                break;
            }
            case "actionOrder":
            case "doneSub":
            case "donePage":
                current.func();
                break;
            case "chat_input": {
                if (Navigator.getChatInput() == null) {
                    fails.add("&cNo chat input found.");
                    doneLoading();
                    return;
                }
                current.func(Navigator.getChatInput());
                ChatLib.command("chatinput cancel");
                break;
            }
            case "manualOpen": {
                Navigator.manualOpen(String.valueOf(current.getValue()), String.valueOf(current.getKey()));
                break;
            }
            case "close": {
                Minecraft.getMinecraft().thePlayer.closeScreen();
                started = false;
                break;
            }
            case "selectOrClick": {
                Navigator.setSelectingOrSlot(String.valueOf(current.getKey()), Integer.parseInt(current.getValue().toString()));
                break;
            }
        }
    }

    public void doneLoading() {
        if (fails.size() > 0) {
            UChat.chat("&cFailed to load: &f(" + fails.size() + " error" + (fails.size() > 1 ? "s" : "") + ")");
            for (String fail : fails) {
                UChat.chat("   > " + fail);
            }
            fails.clear();
            UChat.chat("&f" + queue.size() + " &coperation" + (queue.size() != 1 ? "s" : "") + " left in queue.");
        } else {
            UChat.chat("&3[HTSL] &fImported successfully!");
        }
        Navigator.isWorking = false;

        timeWithoutOperation = 0;
        queue = new ArrayList<>();
        startedTime = 0;
        totalTime = 0;
        started = false;
        Minecraft.getMinecraft().thePlayer.closeScreen();
    }

    @SubscribeEvent
    public void onOverlayDraw (RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        if (queue.size() == 0) return;
        int y = 50;
        Minecraft.getMinecraft().fontRendererObj.drawString("Tick: " + timeWithoutOperation, 50, y + 20, 0xFFFFFF);
        Minecraft.getMinecraft().fontRendererObj.drawString("Queue Size: " + queue.size(), 50, y + 30, 0xFFFFFF);
        Minecraft.getMinecraft().fontRendererObj.drawString("Current: " + (current == null ? "null" : current.getType() + " " + current.getValue()), 50, y + 40, 0xFFFFFF);
        Minecraft.getMinecraft().fontRendererObj.drawString("Context: " + currentGuiContext, 50, y + 50, 0xFFFFFF);
        Minecraft.getMinecraft().fontRendererObj.drawString("Ready: " + Navigator.isReady, 50, y + 60, 0xFFFFFF);
        y += 80;
        List<String> lines = new ArrayList<>();
        for (LoaderObject object : queue) {
            lines.add(object.getType() + " - " + object.getKey() + ":" + object.getValue());
        }
        for (String line : lines) {
            Minecraft.getMinecraft().fontRendererObj.drawString(line, 50, y, 0xFFFFFF);
            y += 10;
        }

    }

    public static void add(LoaderObject object) {
        if (!Navigator.isWorking) {
            if (object.getType().equals("returnToEditActions")) return;
            Navigator.isReady = true;
        }
        Navigator.isWorking = true;
        queue.add(object);
    }

    public static void forceOperation(LoaderObject object) {
        if (!Navigator.isWorking) {
            if (object.getType().equals("returnToEditActions")) return;
            Navigator.isReady = true;
        }
        Navigator.isWorking = true;
        queue.add(0, object); // add to front of queue
    }

    public static boolean isWorking() {
        return Navigator.isWorking;
    }
}
