package llc.redstone.hysentials.handlers.redworks;

import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.config.hysentialMods.FormattingConfig;
import llc.redstone.hysentials.mixin.ChatCompontentTextAccessor;
import llc.redstone.hysentials.util.BUtils;
import llc.redstone.hysentials.utils.StringUtilsKt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.UUID;

public class FormatPlayerName {
    public static long systemTime;
    public static HashMap<UUID, Long> cooldownUpdate = new HashMap<>();
    public FormatPlayerName () {
        systemTime = Minecraft.getSystemTime();
    }

    @SubscribeEvent
    public void onLoad(WorldEvent.Load event) {
        cooldownUpdate.clear();
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

        for (NetworkPlayerInfo playerInfo : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
            EntityPlayer player = Minecraft.getMinecraft().theWorld.getPlayerEntityByUUID(playerInfo.getGameProfile().getId());
            String username = playerInfo.getGameProfile().getName();
            long lastUpdate = cooldownUpdate.getOrDefault(playerInfo.getGameProfile().getId(), System.currentTimeMillis() - 30000);
            boolean refresh = lastUpdate < System.currentTimeMillis();
            if (!refresh) {
                continue;
            }
            cooldownUpdate.put(playerInfo.getGameProfile().getId(), lastUpdate + 10000);
            String displayName = BwRanksUtils.getPlayerName(playerInfo, true);
            if (displayName == null || displayName.equals(username)) {
                continue;
            }
            String prefixName = StringUtilsKt.substringBefore(displayName, username) + username;
            String suffix = StringUtilsKt.substringAfter(displayName, username);

            String newPrefix = BwRanksUtils.getMessage(prefixName, username, playerInfo.getGameProfile().getId(), true, true);

            if (playerInfo.getDisplayName() instanceof UTextComponent) {
                ((UTextComponent) playerInfo.getDisplayName()).setText(newPrefix + suffix);
            } else {
                playerInfo.setDisplayName(new UTextComponent(newPrefix + suffix));
            }
            if (player != null) {
                player.setCustomNameTag(newPrefix + suffix);
            }
        }
    }


}
