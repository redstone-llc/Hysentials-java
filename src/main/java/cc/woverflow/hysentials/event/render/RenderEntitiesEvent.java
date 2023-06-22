package cc.woverflow.hysentials.event.render;


import cc.woverflow.hysentials.event.Event;

/**
 * Called when entities are about to be rendered in the world
 */
public final class RenderEntitiesEvent extends Event {

    private final float partialTicks;

    public RenderEntitiesEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public final float getPartialTicks() {
        return partialTicks;
    }
}
