package cc.woverflow.hysentials.handlers.htsl;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.guis.ResolutionUtil;
import cc.woverflow.hysentials.htsl.Loader;
import cc.woverflow.hysentials.util.Input;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Queue {
    public static List<Loader.LoaderObject> queue = new ArrayList<>();
    List<String> fails = new ArrayList<>();
    int timeWithoutOperation = 0;
    long startedTime = 0;
    long totalTime = 0;
    String currentGuiContext = null;

    Input.Button timeRemainingButton;

    public Queue() {
        timeRemainingButton = new Input.Button(0, 0, 0, 20, "Time Remaining: ");
    }

    @SubscribeEvent
    public void guiRender(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.openContainer == null)
            return;
        if (queue.size() == 0) return;

        timeRemainingButton.setWidth(200);
        timeRemainingButton.xPosition = ResolutionUtil.current().getScaledWidth() / 2 - timeRemainingButton.width / 2;
        timeRemainingButton.yPosition = timeRemainingButton.height * 3;

        timeRemainingButton.drawButton(Minecraft.getMinecraft(), event.getMouseX(), event.getMouseY());
    }

    public static boolean greaterThan(int i, int j) {
        return Math.max(i, j) == i;
    }

    int ticks = 0;
    @SubscribeEvent
    public void tick(TickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (event.type != TickEvent.Type.CLIENT) return;
        if (queue.size() != 0) timeWithoutOperation++;
        if ((greaterThan(timeWithoutOperation, HysentialsConfig.guiTimeout)) && queue.size() > 0 && !HysentialsConfig.htslSafeMode) {
            fails.add("&cOperation timed out. &f(too long without GUI click)");
            doneLoading();
        }
        if (!Navigator.isReady) return;
        if (queue.size() == 0) return;
        timeWithoutOperation = 0;
        if (Navigator.isReturning) {
            Navigator.returnToEditActions();
            return;
        }

        if (Navigator.isSelecting) {
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

        Loader.LoaderObject current = queue.get(0);
        if (current == null) return;
        queue.remove(0);

        if (current.type.equals("setGuiContext")) {
            currentGuiContext = String.valueOf(current.value);
            if (queue.size() == 0) return;
            current = queue.get(0);
            queue.remove(0);
        }

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
//            case "item":
//                Navigator.selectItem();
//                break;
            case "done":
                doneLoading();
                break;
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
        Minecraft.getMinecraft().thePlayer.closeScreen();
    }
}
