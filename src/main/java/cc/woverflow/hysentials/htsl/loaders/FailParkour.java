package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class FailParkour extends Loader {
    public FailParkour(String reason) {
        super("Fail Parkour", reason);

        if (reason != null && !reason.equals("Failed!")) {
            add(click(10));
            add(chat(reason));
        }
    }
}
