package llc.redstone.hysentials.handlers.guis;

import cc.polyfrost.oneconfig.hud.Hud;
import cc.polyfrost.oneconfig.hud.Position;
import cc.polyfrost.oneconfig.internal.gui.HudGui;
import cc.polyfrost.oneconfig.internal.hud.HudCore;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.event.events.GuiCloseEvent;
import llc.redstone.hysentials.event.events.GuiMouseClickEvent;
import llc.redstone.hysentials.guis.utils.SBBoxesHud;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.json.JSONArray;
import org.json.JSONObject;

public class OneConfigHudClickHandler {
    @SubscribeEvent
    public void onMouseClick(GuiMouseClickEvent event) {
        if (Minecraft.getMinecraft().currentScreen instanceof HudGui) {
            if (event.getButton() == 1) {
                event.getCi().cancel();
                for (Hud hud : HudCore.huds.values()) {
                    if (hud instanceof SBBoxesHud) {
                        SBBoxesHud sbBoxesHud = (SBBoxesHud) hud;
                        if (mouseClickedHud(hud, event.getX(), event.getY())) {
                            sbBoxesHud.mouseClick(event);
                            break;
                        }

                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onGuiClose(GuiCloseEvent event) {
        if (Minecraft.getMinecraft().currentScreen instanceof HudGui) {
            JSONArray array = new JSONArray();
            HudCore.huds.values().forEach(hud -> {
                if (hud instanceof SBBoxesHud) {
                    SBBoxesHud sbBoxesHud = (SBBoxesHud) hud;
                    array.put(sbBoxesHud.box.save());
                }
            });
            JSONObject object = new JSONObject();
            object.put("lines", array);
            Hysentials.INSTANCE.sbBoxes.jsonObject = object;
            Hysentials.INSTANCE.sbBoxes.save();
        }
    }

    private boolean mouseClickedHud(Hud hud, float mouseX, float mouseY) {
        Position position = hud.position;
        return mouseX >= position.getX() && mouseX <= position.getRightX() &&
            mouseY >= position.getY() && mouseY <= position.getBottomY();
    }
}
