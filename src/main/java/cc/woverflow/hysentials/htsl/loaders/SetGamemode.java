package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;
public class SetGamemode extends Loader {
    public SetGamemode(String gamemode) {
        super("Set Gamemode", "gamemode", gamemode);

        if (gamemode != null) {
            switch(gamemode) {
                case "adventure":
                    add(click(10));
                    break;
                case "survival":
                    add(click(11));
                    break;
                case "creative":
                    add(click(12));
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
