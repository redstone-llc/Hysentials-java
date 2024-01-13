package llc.redstone.hysentials.htsl.loaders;

import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

import static llc.redstone.hysentials.htsl.Loader.LoaderObject.*;

public class Exit extends Loader {
    public Exit() {
        super("Exit", "exit");
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return new Exit();
    }
}
