package llc.redstone.hysentials.guis.hsplayerlist;

import llc.redstone.hysentials.util.BlockWAPIUtils;
import llc.redstone.hysentials.websocket.Socket;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import llc.redstone.hysentials.schema.HysentialsSchema;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.IScoreObjectiveCriteria.EnumRenderType;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.imageio.ImageIO;

@SideOnly(Side.CLIENT)
public class GuiOnlineList extends Gui {
    private static final Ordering<HysentialsSchema.User> field_175252_a = Ordering.from(new PlayerComparator());
    private static final ResourceLocation hsob = new ResourceLocation("hysentials:gui/hsob.png");
    private final Minecraft mc;
    private IChatComponent footer;
    private IChatComponent header;

    public GuiOnlineList() {
        this.mc = Minecraft.getMinecraft();
        footer = null;
    }

    public void renderPlayerlist(int width) {
        GlStateManager.pushMatrix();
        List<HysentialsSchema.User> list = field_175252_a.sortedCopy(Socket.cachedUsers.values());
        int i = 0;
        Iterator<HysentialsSchema.User> iterator = list.iterator();
        int k;
        while(iterator.hasNext()) {
            HysentialsSchema.User user = iterator.next();
            k = this.mc.fontRendererObj.getStringWidth(BlockWAPIUtils.getDisplayName(user));
            i = Math.max(i, k);
        }

        list = list.subList(0, Math.min(list.size(), 80));
        int l = list.size();
        int m = l;

        for(k = 1; m > 20; m = (l + k - 1) / k) {
            ++k;
        }

        boolean bl = this.mc.isIntegratedServerRunning() || this.mc.getNetHandler().getNetworkManager().getIsencrypted();
        int n = 0;

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

            for(iterator2 = list2.iterator(); iterator2.hasNext(); r = Math.max(r, this.mc.fontRendererObj.getStringWidth(string))) {
                string = (String)iterator2.next();
            }
        }

        if (this.footer != null) {
            list3 = this.mc.fontRendererObj.listFormattedStringToWidth(this.footer.getFormattedText(), width - 50);

            for(iterator2 = list3.iterator(); iterator2.hasNext(); r = Math.max(r, this.mc.fontRendererObj.getStringWidth(string))) {
                string = (String)iterator2.next();
            }
        }

        int s;
        //draw hsob
        drawRect(width / 2 - r / 2 - 1, 0, width / 2 + r / 2 + 1, (int) ((r/2.688172043) + 10), Integer.MIN_VALUE);
        GlStateManager.pushAttrib();
        this.mc.getTextureManager().bindTexture(hsob);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        drawModalRectWithCustomSizedTexture(width / 2 - (r + 10) / 2, 0, 0, 0, r + 10, (int) ((r/2.688172043) + 10), r + 10, r/3f + 10);
        GlStateManager.popAttrib();
        q += (int) (r/2.688172043);


        drawRect(width / 2 - r / 2 - 1, q - 1, width / 2 + r / 2 + 1, q + m * 9, Integer.MIN_VALUE);

        for(int t = 0; t < l; ++t) {
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
                HysentialsSchema.User user = list.get(t);
                String string2 = BlockWAPIUtils.getDisplayName(user);
                if (bl && user.getSkin() != null && !user.getSkin().isEmpty()) {
                    byte[] bytes = Base64.getDecoder().decode(user.getSkin());
                    try {
                        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
                        DynamicTexture dynamicTexture = new DynamicTexture(image);
                        dynamicTexture.updateDynamicTexture();
                    } catch (IOException e) {
                    }

                    int x = 8;
                    int y = 8;
                    Gui.drawScaledCustomSizeModalRect(v, w, 8.0F, (float)x, 8, y, 8, 8, 64.0F, 64.0F);

                    v += 9;
                }
                this.mc.fontRendererObj.drawStringWithShadow(string2, (float)v, (float)w, -1);
            }
        }

        if (list3 != null) {
            q += m * 9 + 1;
            drawRect(width / 2 - r / 2 - 1, q - 1, width / 2 + r / 2 + 1, q + list3.size() * this.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

            for(iterator2 = list3.iterator(); iterator2.hasNext(); q += this.mc.fontRendererObj.FONT_HEIGHT) {
                string = (String)iterator2.next();
                s = this.mc.fontRendererObj.getStringWidth(string);
                this.mc.fontRendererObj.drawStringWithShadow(string, (float)(width / 2 - s / 2), (float)q, -1);
            }
        }
        GlStateManager.popMatrix();
    }

    public void setFooter(IChatComponent footerIn) {
        this.footer = footerIn;
    }

    public void setHeader(IChatComponent headerIn) {
        this.header = headerIn;
    }

    public void resetFooterHeader() {
        this.header = null;
        this.footer = null;
    }

    @SideOnly(Side.CLIENT)
    static class PlayerComparator implements Comparator<HysentialsSchema.User> {
        private PlayerComparator() {
        }

        @Override
        public int compare(HysentialsSchema.User o1, HysentialsSchema.User o2) {
            BlockWAPIUtils.Rank rank1 = BlockWAPIUtils.Rank.valueOF(o1.getRank().toUpperCase());
            BlockWAPIUtils.Rank rank2 = BlockWAPIUtils.Rank.valueOF(o2.getRank().toUpperCase());
            return ComparisonChain.start().compare(rank2.index, rank1.index).compare(o1.getUsername(), o2.getUsername()).result();
        }
    }
}
