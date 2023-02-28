package cc.woverflow.hysentials.mixin;

import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.handlers.imageicons.ImageIcon;
import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldSettings;
import org.spongepowered.asm.mixin.*;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

import static cc.woverflow.hysentials.handlers.imageicons.ImageIcon.stringPattern;

@Mixin(value = GuiPlayerTabOverlay.class, priority = Integer.MIN_VALUE)
public abstract class GuiPlayerTabOverlayMixin extends Gui {
    @Final
    @Shadow
    private Minecraft mc;
    @Final
    @Shadow
    private static Ordering<NetworkPlayerInfo> field_175252_a;

    @Shadow
    public String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
        return null;
    }

    @Unique
    public String getPName(NetworkPlayerInfo networkPlayerInfoIn) {
        String name = this.getPlayerName(networkPlayerInfoIn);
        if (LocrawUtil.INSTANCE.getLocrawInfo() != null && LocrawUtil.INSTANCE.getLocrawInfo().getGameType().equals(LocrawInfo.GameType.SKYBLOCK)) {
            return name;
        }
        if (!name.contains("[NPC]")) {
            if (Hysentials.INSTANCE.getOnlineCache().playerDisplayNames.containsKey(networkPlayerInfoIn.getGameProfile().getId())) {
                name = name.replaceAll("(§r§7|§7)" + networkPlayerInfoIn.getGameProfile().getName(), Hysentials.INSTANCE.getOnlineCache().playerDisplayNames.get(networkPlayerInfoIn.getGameProfile().getId()));
                name = name.replaceAll("\\[[A-Za-z§0-9+]+] " + networkPlayerInfoIn.getGameProfile().getName(), Hysentials.INSTANCE.getOnlineCache().playerDisplayNames.get(networkPlayerInfoIn.getGameProfile().getId()));
            }
        }
        if (Hysentials.INSTANCE.getOnlineCache().getOnlinePlayers().containsKey(networkPlayerInfoIn.getGameProfile().getId()))
            name = "§r§a■ §r" + name;

        return name;
    }

    @Unique
    public int getIconWidth(String str, boolean centered) {
        Matcher matcher = stringPattern.matcher(str);
        int additional = 0;
        String length = str;
        while (matcher.find()) {
            String group = matcher.group(1);
            length = length.replace(":" + group + ":", "");
            additional += (ImageIcon.getIcon(group) != null) ? ImageIcon.getIcon(group).getWidth() : 0;
        }

        int j = this.mc.fontRendererObj.getStringWidth(length) / (centered ? 2 : 1);
        j += additional / (centered ? 2 : 1);
        return j;
    }

    @Shadow
    private IChatComponent footer;
    @Shadow
    private IChatComponent header;

    //drawPing
    @Shadow
    protected void drawPing(int i, int j, int k, NetworkPlayerInfo networkPlayerInfoIn) {
    }

    //drawScoreboardValues
    @Shadow
    protected void drawScoreboardValues(ScoreObjective scoreObjectiveIn, int p_175247_2_, String displayNameIn, int p_175247_4_, int p_175247_5_, NetworkPlayerInfo networkPlayerInfoIn) {
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void renderPlayerlist(int width, Scoreboard scoreboardIn, ScoreObjective scoreObjectiveIn) {
        NetHandlerPlayClient netHandlerPlayClient = mc.thePlayer.sendQueue;
        List<NetworkPlayerInfo> list = field_175252_a.sortedCopy(netHandlerPlayClient.getPlayerInfoMap());
        int i = 0;
        int j = 0;
        Iterator iterator = list.iterator();

        int k;
        while (iterator.hasNext()) {
            NetworkPlayerInfo networkPlayerInfo = (NetworkPlayerInfo) iterator.next();
            k = getIconWidth(this.getPName(networkPlayerInfo), false);
            i = Math.max(i, k);
            if (scoreObjectiveIn != null && scoreObjectiveIn.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
                k = this.mc.fontRendererObj.getStringWidth(" " + scoreboardIn.getValueFromObjective(networkPlayerInfo.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
                j = Math.max(j, k);
            }
        }

        list = list.subList(0, Math.min(list.size(), 80));
        int l = list.size();
        int m = l;

        for (k = 1; m > 20; m = (l + k - 1) / k) {
            ++k;
        }

        boolean bl = this.mc.isIntegratedServerRunning() || this.mc.getNetHandler().getNetworkManager().getIsencrypted();
        int n;
        if (scoreObjectiveIn != null) {
            if (scoreObjectiveIn.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
                n = 90;
            } else {
                n = j;
            }
        } else {
            n = 0;
        }

        int o = Math.min(k * ((bl ? 9 : 0) + i + n + 13), width - 50) / k;
        int p = width / 2 - (o * k + (k - 1) * 5) / 2;
        int q = 10;
        int r = o * k + (k - 1) * 5;
        List<String> list2 = null;
        List<String> list3 = null;
        Iterator iterator2;
        String string;
        if (this.header != null) {
            list2 = this.mc.fontRendererObj.listFormattedStringToWidth(this.header.getFormattedText(), width - 50);

            for (iterator2 = list2.iterator(); iterator2.hasNext(); r = Math.max(r, this.mc.fontRendererObj.getStringWidth(string))) {
                string = (String) iterator2.next();
            }
        }

        if (this.footer != null) {
            list3 = this.mc.fontRendererObj.listFormattedStringToWidth(this.footer.getFormattedText(), width - 50);

            for (iterator2 = list3.iterator(); iterator2.hasNext(); r = Math.max(r, this.mc.fontRendererObj.getStringWidth(string))) {
                string = (String) iterator2.next();
            }
        }

        int s;
        if (list2 != null) {
            drawRect(width / 2 - r / 2 - 1, q - 1, width / 2 + r / 2 + 1, q + list2.size() * this.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

            for (iterator2 = list2.iterator(); iterator2.hasNext(); q += this.mc.fontRendererObj.FONT_HEIGHT) {
                string = (String) iterator2.next();

                s = this.mc.fontRendererObj.getStringWidth(string);
                this.mc.fontRendererObj.drawStringWithShadow(string, (float) (width / 2 - s / 2), (float) q, -1);
            }

            ++q;
        }

        drawRect(width / 2 - r / 2 - 1, q - 1, width / 2 + r / 2 + 1, q + m * 9, Integer.MIN_VALUE);

        for (int t = 0; t < l; ++t) {
            int u = t / m;
            s = t % m;
            int v = p + u * o + u * 5;
            int w = q + s * 9;
            drawRect(v, w, v + o, w + 8, 553648127);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            if (t < list.size()) {
                NetworkPlayerInfo networkPlayerInfo2 = (NetworkPlayerInfo) list.get(t);
                String string2 = this.getPName(networkPlayerInfo2);
                GameProfile gameProfile = networkPlayerInfo2.getGameProfile();
                if (bl) {
                    EntityPlayer entityPlayer = this.mc.theWorld.getPlayerEntityByUUID(gameProfile.getId());
                    boolean bl2 = entityPlayer != null && entityPlayer.isWearing(EnumPlayerModelParts.CAPE) && (gameProfile.getName().equals("Dinnerbone") || gameProfile.getName().equals("Grumm"));
                    this.mc.getTextureManager().bindTexture(networkPlayerInfo2.getLocationSkin());
                    int x = 8 + (bl2 ? 8 : 0);
                    int y = 8 * (bl2 ? -1 : 1);
                    Gui.drawScaledCustomSizeModalRect(v, w, 8.0F, (float) x, 8, y, 8, 8, 64.0F, 64.0F);
                    if (entityPlayer != null && entityPlayer.isWearing(EnumPlayerModelParts.HAT)) {
                        int z = 8 + (bl2 ? 8 : 0);
                        int aa = 8 * (bl2 ? -1 : 1);
                        Gui.drawScaledCustomSizeModalRect(v, w, 40.0F, (float) z, 8, aa, 8, 8, 64.0F, 64.0F);
                    }

                    v += 9;
                }

                if (networkPlayerInfo2.getGameType() == WorldSettings.GameType.SPECTATOR) {
                    string2 = EnumChatFormatting.ITALIC + string2;
                    ImageIcon.shiftRenderText(this.mc.fontRendererObj, string2, v, w, -1862270977, true);
                } else {
                    ImageIcon.shiftRenderText(this.mc.fontRendererObj, string2, v, w, -1, true);
                }

                if (scoreObjectiveIn != null && networkPlayerInfo2.getGameType() != WorldSettings.GameType.SPECTATOR) {
                    int ab = v + i + 1;
                    int ac = ab + n;
                    if (ac - ab > 5) {
                        this.drawScoreboardValues(scoreObjectiveIn, w, gameProfile.getName(), ab, ac, networkPlayerInfo2);
                    }
                }

                this.drawPing(o, v - (bl ? 9 : 0), w, networkPlayerInfo2);
            }
        }

        if (list3 != null) {
            q += m * 9 + 1;
            drawRect(width / 2 - r / 2 - 1, q - 1, width / 2 + r / 2 + 1, q + list3.size() * this.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

            for (iterator2 = list3.iterator(); iterator2.hasNext(); q += this.mc.fontRendererObj.FONT_HEIGHT) {
                string = (String) iterator2.next();
                s = this.mc.fontRendererObj.getStringWidth(string);
                this.mc.fontRendererObj.drawStringWithShadow(string, (float) (width / 2 - s / 2), (float) q, -1);
            }
        }

    }

}
