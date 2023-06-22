package cc.woverflow.hysentials.event.render;

import cc.woverflow.hysentials.event.Event;
import com.google.common.base.Preconditions;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import org.jetbrains.annotations.NotNull;

/**
 * Invoked when a player model is rendered
 */
public final class RenderPlayerEvent extends Event {

    @NotNull
    private final AbstractClientPlayer entity;
    @NotNull
    private final RenderManager renderManager;

    private final double x;
    private final double y;
    private final double z;

    private final float partialTicks;

    public RenderPlayerEvent(@NotNull AbstractClientPlayer entity, @NotNull RenderManager renderManager, double x, double y, double z, float partialTicks) {
        Preconditions.checkNotNull(entity, "entity");
        Preconditions.checkNotNull(renderManager, "renderManager");

        this.entity = entity;
        this.renderManager = renderManager;
        this.x = x;
        this.y = y;
        this.z = z;
        this.partialTicks = partialTicks;
    }

    @NotNull
    public final AbstractClientPlayer getEntity() {
        return entity;
    }

    @NotNull
    public final RenderManager getRenderManager() {
        return renderManager;
    }

    public final double getX() {
        return x;
    }

    public final double getY() {
        return y;
    }

    public final double getZ() {
        return z;
    }

    public final float getPartialTicks() {
        return partialTicks;
    }
}
