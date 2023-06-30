package cc.woverflow.hysentials.handlers.htsl;

import cc.polyfrost.oneconfig.libs.checker.units.qual.N;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.woverflow.hysentials.event.events.GuiMouseClickEvent;
import cc.woverflow.hysentials.util.MUtils;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.guis.ResolutionUtil;
import cc.woverflow.hysentials.htsl.Loader;
import cc.woverflow.hysentials.util.Input;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static cc.woverflow.hysentials.handlers.htsl.ActionGUIHandler.*;

public class Queue {
    public static List<Loader.LoaderObject> queue = new ArrayList<>();
    List<String> fails = new ArrayList<>();
    public static int timeWithoutOperation = 0;
    long startedTime = 0;
    long totalTime = 0;
    String currentGuiContext = null;

    Input.Button timeRemainingButton;
    Input.Button cancel;
    Loader.LoaderObject current;

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
        if ((greaterThan(timeWithoutOperation, HysentialsConfig.guiTimeout)) && queue.size() > 0 && !HysentialsConfig.htslSafeMode && Navigator.guiToOpen == null) {
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

        if (current.type.equals("setGuiContext")) {
            currentGuiContext = String.valueOf(current.value);
            if (queue.size() == 0) return;
            current = queue.get(0);
            queue.remove(0);
        }
        System.out.println("current: " + current.type + " " + current.value);
        switch (current.type) {
            case "click":
                System.out.println("clicking " + current.value);
                Navigator.click((Integer) current.value);
                break;
            case "anvil":
                System.out.println("inputting anvil");
                Navigator.inputAnvil(String.valueOf(current.value));
                break;
            case "returnToEditActions":
                Navigator.returnToEditActions();
                break;
            case "back":
                Navigator.goBack();
                break;
            case "option":
                Navigator.setSelecting(String.valueOf(current.value));
                break;
            case "chat":
                Navigator.inputChat(String.valueOf(current.value));
                break;
            case "item":
                Navigator.selectItem(String.valueOf(current.value));
                break;
            case "command":
                System.out.println("commanding " + current.value);
                Navigator.command(String.valueOf(current.value));
                break;
            case "done":
                doneLoading();
                break;
            case "manualOpen": {
                Navigator.manualOpen(String.valueOf(current.value), String.valueOf(current.key));
                break;
            }
            case "close": {
                Minecraft.getMinecraft().thePlayer.closeScreen();
                started = false;
                break;
            }
            case "selectOrClick": {
                Navigator.setSelectingOrSlot(String.valueOf(current.key), Integer.parseInt(current.value.toString()));
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
        Minecraft.getMinecraft().fontRendererObj.drawString("Current: " + (current == null ? "null" : current.type + " " + current.value), 50, y + 40, 0xFFFFFF);
        Minecraft.getMinecraft().fontRendererObj.drawString("Context: " + currentGuiContext, 50, y + 50, 0xFFFFFF);
        Minecraft.getMinecraft().fontRendererObj.drawString("Ready: " + Navigator.isReady, 50, y + 60, 0xFFFFFF);
        y += 80;
        List<String> lines = new ArrayList<>();
        for (Loader.LoaderObject object : queue) {
            lines.add(object.type + " - " + object.key + ":" + object.value);
        }
        for (String line : lines) {
            Minecraft.getMinecraft().fontRendererObj.drawString(line, 50, y, 0xFFFFFF);
            y += 10;
        }

    }
}
