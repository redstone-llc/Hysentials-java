package llc.redstone.hysentials.htsl.loaders;

import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

import static llc.redstone.hysentials.htsl.Loader.LoaderObject.*;

public class PlaySound extends Loader {
    public PlaySound(String sound, String pitch) {
        super("Play Sound", "sound", sound, pitch);

        if (sound != null) {
            add(LoaderObject.click(10));
            add(LoaderObject.option(sound));
        }

        if (!isNAN(pitch) && !pitch.equals("1")) {
            add(LoaderObject.click(11));
            add(LoaderObject.anvil(pitch));
        }
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return new PlaySound(args.get(0), args.get(1));
    }

    @Override
    public String export(List<String> args) {
        return "sound \"" + args.get(0) + "\" " + args.get(1);
    }
}
