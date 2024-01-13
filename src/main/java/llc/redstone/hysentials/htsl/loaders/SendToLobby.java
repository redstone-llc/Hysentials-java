package llc.redstone.hysentials.htsl.loaders;

import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

import static llc.redstone.hysentials.htsl.Loader.LoaderObject.*;

public class SendToLobby extends Loader {
    public SendToLobby(String lobby) {
        super("Send to Lobby", "lobby", lobby);

        if (lobby != null) {
            add(LoaderObject.click(10));
            add(LoaderObject.option(lobby));
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
