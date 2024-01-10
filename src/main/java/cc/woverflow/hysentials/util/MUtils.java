package cc.woverflow.hysentials.util;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.woverflow.hysentials.util.MUtils;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class MUtils {
    public static List<String> messages = new ArrayList<>();
    @SubscribeEvent
    public void onTickEvent(TickEvent event) {
        if (event.phase != TickEvent.Phase.START || event.type != TickEvent.Type.CLIENT || Minecraft.getMinecraft().thePlayer == null || !BUtils.isHypixelOrSBX()) {
            return;
        }
        if (messages.size() == 0) {
            return;
        }
        if (messages.size() > 5) {
            messages = messages.subList(messages.size() - 5, messages.size());
        }
        UChat.chat(messages.get(0));
        messages.remove(0);
    }

    public static void chat(String message) {
        messages.add(message);
    }
}
