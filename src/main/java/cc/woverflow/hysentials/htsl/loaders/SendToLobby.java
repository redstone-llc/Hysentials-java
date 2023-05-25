package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;
public class SendToLobby extends Loader {
    public SendToLobby(String lobby) {
        super("Send to Lobby", lobby);

        if (lobby != null) {
            add(click(10));
            add(option(lobby));
        }
    }
}
