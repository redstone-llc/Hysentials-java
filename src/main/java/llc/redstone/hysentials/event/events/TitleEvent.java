package llc.redstone.hysentials.event.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * TitleEvent is fired when a title is being rendered.
 * If this event is canceled, the title does not appear.
 */
@Cancelable
public class TitleEvent extends Event {

    private final String title;
    private final String subtitle;

    public TitleEvent(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
