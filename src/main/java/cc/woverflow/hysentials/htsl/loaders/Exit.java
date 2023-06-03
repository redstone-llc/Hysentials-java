package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class Exit extends Loader {
    public Exit() {
        super("Exit", "exit");
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return new Exit();
    }
}
