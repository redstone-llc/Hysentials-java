package cc.woverflow.hysentials.guis.misc;

import cc.woverflow.hysentials.guis.container.Container;
import net.minecraftforge.client.event.MouseEvent;

import java.util.HashMap;

public class HysentialsLevel extends Container {
    int page;
    HashMap<Integer, Integer> levels = new HashMap<>();

    public HysentialsLevel(int page) {
        super("Hysentials Level", 6);
        this.page = page;
    }

    @Override
    public void setItems() {

    }

    @Override
    public void handleMenu(MouseEvent event) {

    }

    @Override
    public void setClickActions() {

    }
}
