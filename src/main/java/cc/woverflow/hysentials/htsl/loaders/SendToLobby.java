package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class SendToLobby extends Loader {
    public SendToLobby(String lobby) {
        super("Send to Lobby", "lobby", lobby);

        if (lobby != null) {
            add(click(10));
            add(option(lobby));
        }
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return new SendToLobby(args.get(0));
    }

    @Override
    public String export(List<String> args) {
        return "lobby \"" + args.get(0) + "\"";
    }
}
