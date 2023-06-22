package cc.woverflow.hysentials.handlers.lobby;

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.regex.Pattern;

public class LobbyChecker {
    private final Pattern lobbyPattern = Pattern.compile("(.+)?lobby(.+)");
    private boolean lobbyStatus;
    private int tick;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        final LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();
        if (event.phase != TickEvent.Phase.START || Minecraft.getMinecraft().thePlayer == null || !HypixelUtils.INSTANCE.isHypixel() || this.tick >= 52 || locraw == null) {
            return;
        }

        if (++this.tick == 50) {
            this.lobbyStatus = this.lobbyPattern.matcher(locraw.getServerId()).matches();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        this.tick = 0;
    }

    public boolean playerIsInLobby() {
        return this.lobbyStatus;
    }
}
