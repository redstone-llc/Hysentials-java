package llc.redstone.hysentials.handlers.redworks;

import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.config.hysentialMods.FormattingConfig;
import llc.redstone.hysentials.util.BUtils;
import llc.redstone.hysentials.utils.StringUtilsKt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FormatPlayerName {
    public static long systemTime;
    public FormatPlayerName () {
        systemTime = Minecraft.getSystemTime();
    }
    @SubscribeEvent
    public void onStep(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START || Minecraft.getMinecraft().thePlayer == null || !BUtils.isHypixelOrSBX()) {
            return;
        }
        final LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();
        if (HypixelUtils.INSTANCE.isHypixel() && locraw != null) {
            if (locraw.getGameType().equals(LocrawInfo.GameType.SKYBLOCK)) {
                return; //Due to how the skyblock formatting is, we don't want to mess with it
                //TODO: Add skyblock formatting
            }
        }
        //1 second delay
        if (Minecraft.getSystemTime() < systemTime) {
            return;
        }
        systemTime = Minecraft.getSystemTime() + 1000;
        if (Minecraft.getMinecraft().theWorld == null) {
            return;
        }

        if (!BwRanksUtils.futuristicRanks(true)) return;

        //Format player name
        Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().forEach(playerInfo -> {
            EntityPlayer player = Minecraft.getMinecraft().theWorld.getPlayerEntityByUUID(playerInfo.getGameProfile().getId());
            String username = playerInfo.getGameProfile().getName();
            String displayName = BwRanksUtils.getPlayerName(playerInfo);
            if (displayName == null || displayName.equals(username)) {
                return;
            }
            String prefixName = StringUtilsKt.substringBefore(displayName, username) + username;
            String suffix = StringUtilsKt.substringAfter(displayName, username);

            String newPrefix = BwRanksUtils.getMessage(prefixName, username, playerInfo.getGameProfile().getId(), true, true);

            playerInfo.setDisplayName(new UTextComponent(newPrefix + suffix));
            if (player != null) {
                player.setCustomNameTag(newPrefix + suffix);
            }
        });
    }


}
