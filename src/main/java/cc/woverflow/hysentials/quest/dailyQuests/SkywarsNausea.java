package cc.woverflow.hysentials.quest.dailyQuests;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hysentials.handlers.misc.QuestHandler;
import cc.woverflow.hysentials.quest.Quest;
import cc.woverflow.hysentials.util.C;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SkywarsNausea extends Quest {
    public SkywarsNausea() {
        super("Skywars Nausea Challenge", "Complete any skywars game whilst having the nausea affect.", "SKYWARS_NAUSEA", true);
    }

    private static float portalCounter = 0.0f;
    private static float prevPortalCounter = 0.0f;

    @Override
    public void onTick() {
        if (LocrawUtil.INSTANCE.getLocrawInfo() == null || !LocrawUtil.INSTANCE.getLocrawInfo().getGameType().equals(LocrawInfo.GameType.SKYWARS) || LocrawUtil.INSTANCE.getLocrawInfo().getGameMode().equals("lobby")) {
            return;
        }
        if (!isActive) return;
        Minecraft.getMinecraft().thePlayer.addPotionEffect(new PotionEffect(Potion.confusion.id, 100, 0));
    }

    @Override
    public void onMessageReceive(String message) {
        super.onMessageReceive(message);
        if (LocrawUtil.INSTANCE.getLocrawInfo() == null || !LocrawUtil.INSTANCE.getLocrawInfo().getGameType().equals(LocrawInfo.GameType.SKYWARS) || LocrawUtil.INSTANCE.getLocrawInfo().getGameMode().equals("lobby")) {
            return;
        }
        if (!isActive) return;
        Matcher matcher = Pattern.compile("( .+ )§eWinner §7- §7(.*)").matcher(message.replace(C.RESET, ""));
        if (matcher.find()) {
            String name = C.removeColor(matcher.group(1));
            if (name.equals(Minecraft.getMinecraft().thePlayer.getName())) {
                UChat.chat("&a&lQuest Completed! &7You have completed the &e" + getName() + " &7quest!");
                UChat.chat("&7We will validate your completion in 60 seconds once the api has updated...");
                Multithreading.schedule(() -> {
                    QuestHandler.checkQuest(this);
                }, 60, TimeUnit.SECONDS);

            }
        }
    }

    @Override
    public void onQuestStart() {

    }

    @Override
    public void onQuestEnd() {

    }

    protected void renderPortal(float timeInPortal, ScaledResolution scaledRes) {
        if (timeInPortal < 1.0F) {
            timeInPortal *= timeInPortal;
            timeInPortal *= timeInPortal;
            timeInPortal = timeInPortal * 0.8F + 0.2F;
        }

        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, timeInPortal);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        TextureAtlasSprite textureAtlasSprite = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.portal.getDefaultState());
        float f = textureAtlasSprite.getMinU();
        float g = textureAtlasSprite.getMinV();
        float h = textureAtlasSprite.getMaxU();
        float i = textureAtlasSprite.getMaxV();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(0.0, (double) scaledRes.getScaledHeight(), -90.0).tex((double) f, (double) i).endVertex();
        worldRenderer.pos((double) scaledRes.getScaledWidth(), (double) scaledRes.getScaledHeight(), -90.0).tex((double) h, (double) i).endVertex();
        worldRenderer.pos((double) scaledRes.getScaledWidth(), 0.0, -90.0).tex((double) h, (double) g).endVertex();
        worldRenderer.pos(0.0, 0.0, -90.0).tex((double) f, (double) g).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
