package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class PlaySound extends Loader {
    public PlaySound(String sound, double pitch) {
        super("Play Sound", "sound", sound, pitch);

        if (sound != null) {
            add(click(10));
            add(option(sound));
        }

        if (!Double.isNaN(pitch) && pitch != 1) {
            add(click(11));
            add(anvil(String.valueOf(pitch)));
        }
    }
}
