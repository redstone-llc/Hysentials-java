package llc.redstone.hysentials.event.render;


import llc.redstone.hysentials.event.Event;

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
