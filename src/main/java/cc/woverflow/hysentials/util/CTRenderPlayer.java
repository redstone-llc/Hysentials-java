package cc.woverflow.hysentials.util;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

import java.util.ArrayList;
import java.util.List;

public class CTRenderPlayer extends RenderPlayer {
    private boolean showNametag = true;
    private boolean showArmor = true;
    private boolean showCape = true;
    private boolean showHeldItem = true;
    private boolean showArrows = true;

    public CTRenderPlayer(RenderManager renderManager, boolean useSmallArms) {
        super(renderManager, useSmallArms);
    }

    public void setOptions(boolean showNametag, boolean showArmor, boolean showCape, boolean showHeldItem, boolean showArrows) {
        this.showNametag = showNametag;
        this.showArmor = showArmor;
        this.showCape = showCape;
        this.showHeldItem = showHeldItem;
        this.showArrows = showArrows;

        layerRenderers = new ArrayList<>();

        if (showArmor) addLayer(new LayerBipedArmor(this));
        if (showHeldItem) addLayer(new LayerHeldItem(this));
        if (showArrows) addLayer(new LayerArrow(this));
        addLayer(new LayerDeadmau5Head(this));
        if (showCape) addLayer(new LayerCape(this));
        if (showArmor) addLayer(new LayerCustomHead(getMainModel().bipedHead));
    }

//    @Override
//    private void setModelVisibilities(AbstractClientPlayer clientPlayer) {
//        super.setModelVisibilities(clientPlayer);
//        if (!showHeldItem) getMainModel().heldItemRight = 0;
//    }

    @Override
    protected boolean canRenderName(AbstractClientPlayer entity) {
        return showNametag;
    }

    @Override
    protected void renderOffsetLivingLabel(AbstractClientPlayer entityIn, double x, double y, double z, String str, float p_177069_9_, double p_177069_10_) {
        if (showNametag) super.renderOffsetLivingLabel(entityIn, x, y, z, str, p_177069_9_, p_177069_10_);
    }

    @Override
    public void renderName(AbstractClientPlayer entity, double x, double y, double z) {
        if (showNametag) super.renderName(entity, x, y, z);
    }

    @Override
    protected void renderLivingLabel(AbstractClientPlayer entityIn, String str, double x, double y, double z, int maxDistance) {
        if (showNametag) super.renderLivingLabel(entityIn, str, x, y, z, maxDistance);
    }
}
