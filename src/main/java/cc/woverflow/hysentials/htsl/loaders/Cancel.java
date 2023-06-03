package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

public class Cancel extends Loader {
    public Cancel() {
        super("Cancel Event", "cancelEvent");
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return new Cancel();
    }
}
