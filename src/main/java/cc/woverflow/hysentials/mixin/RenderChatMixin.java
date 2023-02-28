package cc.woverflow.hysentials.mixin;

import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.handlers.imageicons.ImageIcon;
import cc.woverflow.hysentials.hook.ChatLineHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.regex.Pattern;

/*
This class is used for image symbols in chat.
 */
@Mixin(value = GuiNewChat.class, priority = Integer.MIN_VALUE)
public abstract class RenderChatMixin extends Gui {
    @Shadow
    @Final
    private Minecraft mc;
    @Shadow
    @Final
    private List<ChatLine> drawnChatLines;

    @Shadow
    private int scrollPos;
    @Shadow
    @Final
    private List<ChatLine> chatLines;
    @Shadow
    private boolean isScrolled;

    @Shadow
    public abstract boolean getChatOpen();

    @Shadow
    public abstract float getChatScale();

    @Shadow
    public abstract int getLineCount();

    @Shadow
    public abstract int getChatWidth();

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void drawChat(int updateCounter) {
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int i = this.getLineCount();
            boolean bl = false;
            int j = 0;
            int k = this.drawnChatLines.size();
            float f = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;
            if (k > 0) {
                if (this.getChatOpen()) {
                    bl = true;
                }

                float g = this.getChatScale();
                int l = MathHelper.ceiling_float_int((float) this.getChatWidth() / g);
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0F, 20.0F, 0.0F);
                GlStateManager.scale(g, g, 1.0F);

                int m;
                int n;
                int o;
                int p;
                for (m = 0; m + this.scrollPos < this.drawnChatLines.size() && m < i; ++m) {
                    ChatLine chatLine = this.drawnChatLines.get(m + this.scrollPos);
                    if (chatLine != null) {
                        n = updateCounter - chatLine.getUpdatedCounter();
                        if (n < 200 || bl) {
                            double d = (double) n / 200.0;
                            d = 1.0 - d;
                            d *= 10.0;
                            d = MathHelper.clamp_double(d, 0.0, 1.0);
                            d *= d;
                            o = (int) (255.0 * d);
                            if (bl) {
                                o = 255;
                            }

                            o = (int) ((float) o * f);
                            ++j;
                            if (o > 3) {
                                p = 0;
                                int q = -m * 9;
                                drawRect(p, q - 9, p + l + 4, q, o / 2 << 24);
                                String string = chatLine.getChatComponent().getFormattedText();

                                ChatLineHook hook = (ChatLineHook) chatLine;
                                if (hook.hasDetected() && hook.getPlayerInfo() != null) {
                                    String name = hook.getPlayerInfo().getGameProfile().getName();
                                    String regex1 = "\\[[A-Za-z§0-9+]+] " + name;
                                    String regex2 = "(§r§7|§7)" + name;
                                    String regex3 = "[a-f0-9§]{2}" + name;

                                    String replacement = Hysentials.INSTANCE.getOnlineCache().getPlayerDisplayNames().get(hook.getPlayerInfo().getGameProfile().getId());
                                    if (replacement != null) {
                                        if (Pattern.compile(regex1).matcher(string).find(0)) {
                                            string = string.replaceAll("\\[[A-Za-z§0-9+]+] " + name, replacement).replace("§7:", "§f:");
                                        } else if (Pattern.compile(regex2).matcher(string.split("§7:")[0]).find(0)) {
                                            string = string.replaceAll("(§r§7|§7)" + name, replacement);
                                        } else if (Pattern.compile(regex3).matcher(string).find(0)) {
                                            string = string.replaceAll("[a-f0-9§]{2}" + name, replacement).replace("§7:", "§f:");
                                        }
                                    }
                                }

                                GlStateManager.enableBlend();
                                if (HysentialsConfig.futuristicRanks) {
                                    ImageIcon.shiftRenderText(this.mc.fontRendererObj, string, (float) p, (float) (q - 8), 16777215 + (o << 24), true);
                                } else {
                                    this.mc.fontRendererObj.drawStringWithShadow(string, (float) p, (float) (q - 8), 16777215 + (o << 24));
                                }
                                GlStateManager.disableAlpha();
                                GlStateManager.disableBlend();
                            }
                        }
                    }
                }

                if (bl) {
                    m = this.mc.fontRendererObj.FONT_HEIGHT;
                    GlStateManager.translate(-3.0F, 0.0F, 0.0F);
                    int r = k * m + k;
                    n = j * m + j;
                    int s = this.scrollPos * n / k;
                    int t = n * n / r;
                    if (r != n) {
                        o = s > 0 ? 170 : 96;
                        p = this.isScrolled ? 13382451 : 3355562;
                        drawRect(0, -s, 2, -s - t, p + (o << 24));
                        drawRect(2, -s, 1, -s - t, 13421772 + (o << 24));
                    }
                }

                GlStateManager.popMatrix();
            }
        }
    }

    private static final Pattern pattern = Pattern.compile(":([a-z_\\-0-9]+):", 2);

}
