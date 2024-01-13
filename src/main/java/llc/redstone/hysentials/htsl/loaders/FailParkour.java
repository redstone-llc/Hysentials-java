package llc.redstone.hysentials.htsl.loaders;

import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

import static llc.redstone.hysentials.htsl.Loader.LoaderObject.*;

public class FailParkour extends Loader {
    public FailParkour(String reason) {
        super("Fail Parkour", "failParkour", reason);

        if (reason != null && !reason.equals("Failed!")) {
            add(LoaderObject.click(10));
            add(LoaderObject.chat(reason));
        }
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return new FailParkour(args.get(0));
    }
}
