package llc.redstone.hysentials.htsl.loaders;

import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

import static llc.redstone.hysentials.htsl.Loader.LoaderObject.*;
public class SetGamemode extends Loader {
    public SetGamemode(String gamemode) {
        super("Set Gamemode", "gamemode", gamemode);

        if (gamemode != null) {
            switch(gamemode) {
                case "adventure":
                    add(LoaderObject.click(10));
                    break;
                case "survival":
                    add(LoaderObject.click(11));
                    break;
                case "creative":
                    add(LoaderObject.click(12));
                    break;
            }
        }
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return new SetGamemode(args.get(0));
    }

    @Override
    public String export(List<String> args) {
        return "gamemode " + args.get(0) + "";
    }
}
